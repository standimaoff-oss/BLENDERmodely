#!/usr/bin/env bash
# Headless in-game smoke test driver (iteration 3: mouse-driven).
# Requires: Xvfb on DISPLAY (default :99), xdotool, and a Minecraft client
# starting up in the background (gradlew runClient).
#
# All menu navigation is done with xdotool MOUSE CLICKS at fixed physical
# coordinates (1280x720, guiScale 2) measured from earlier CI screenshots:
#   onboarding Continue      (640, 666)
#   title Singleplayer       (640, 354)   title Mods (455, 492)
#   mod list Done            (640, 664)
#   world list Create        (798, 636)
#   create screen: GameMode  (640, 264), Allow Cheats (640, 434),
#                  Create New World (396, 666)
# Keyboard is only used for F2 screenshots and chat commands.
set -u
export DISPLAY="${DISPLAY:-:99}"
SHOT_DIR="run/screenshots"
GAME_LOG="run/logs/latest.log"
LOG="ci-play.log"

log() { echo "[play] $*" | tee -a "$LOG"; }

click() {
  xdotool mousemove --sync "$1" "$2" 2>/dev/null || true
  sleep 1
  xdotool click --clearmodifiers 1 2>/dev/null || true
  sleep 2
}

key() {
  xdotool key --clearmodifiers "$@" 2>/dev/null || true
  sleep 2
}

shot() {
  key F2
  sleep 5
}

type_cmd() {
  key slash
  xdotool type --clearmodifiers --delay 35 "$1" 2>/dev/null || true
  sleep 1
  key Return
  sleep 5
}

log "waiting for the Minecraft window (up to 25 min, llvmpipe is slow)..."
WIN=""
for _ in $(seq 1 300); do
  WIN="$(xdotool search --name 'Minecraft' 2>/dev/null | head -n 1 || true)"
  if [ -n "$WIN" ]; then break; fi
  sleep 5
done
if [ -z "$WIN" ]; then
  log "FATAL: no Minecraft window appeared"
  exit 1
fi
log "window id: $WIN"
xdotool windowactivate --sync "$WIN" 2>/dev/null || true
xdotool windowfocus "$WIN" 2>/dev/null || true

sleep 90   # llvmpipe title-screen stabilisation
shot; log "shot: title screen"

# dismiss onboarding dialog if it is up (harmless click on title otherwise)
click 640 666
sleep 8
shot; log "shot: after onboarding dismiss"

# --- mods list (proves the mod is loaded in-game) ---
click 455 492
sleep 20
shot; log "shot: mods list"
click 640 664   # Done
sleep 15

# --- Singleplayer -> world list -> create screen ---
click 640 354
sleep 30
shot; log "shot: world list"
click 798 636   # Create New World
sleep 30
shot; log "shot: create-world screen"

# --- creative + cheats ---
click 640 264   # Game Mode: Survival -> Creative
sleep 5
shot; log "shot: game mode toggled"
click 640 434   # Allow Cheats: OFF -> ON
sleep 5
shot; log "shot: cheats toggled"

# --- create; wait for the integrated server 'Done (' log line ---
click 396 666
log "waiting for world generation (log line 'Done (') up to 12 min..."
READY=0
for i in $(seq 1 72); do
  if grep -q "Done (" "$GAME_LOG" 2>/dev/null; then READY=1; break; fi
  sleep 10
  if [ $((i % 6)) -eq 0 ]; then
    shot; log "shot: world loading progress ($((i * 10)) s)"
  fi
done
if [ "$READY" -ne 1 ]; then
  log "WARN: no 'Done (' line after 12 min; shooting anyway"
fi
sleep 45
shot; log "shot: in-world (first person)"

key F5; sleep 8
shot; log "shot: in-world (third person)"

# --- commands (cheats should be on now) ---
type_cmd "/gamemode creative"
type_cmd "/give @s octoarms:spider_mech_chestplate 1"
type_cmd "/give @s octoarms:diamond_mech_helmet 1"
type_cmd "/give @s octoarms:arm_cannon 1"
type_cmd "/give @s octoarms:railgun 1"
type_cmd "/item replace entity @s armor.chest with octoarms:spider_mech_chestplate"
type_cmd "/item replace entity @s armor.head with octoarms:diamond_mech_helmet"
sleep 10
key F5; sleep 6
shot; log "shot: wearing mech armor (3rd person front)"

key e; sleep 25
shot; log "shot: inventory / creative items"

key Escape; sleep 10
shot; log "shot: pause menu"

log "automation finished; screenshots:"
ls -la "$SHOT_DIR" 2>/dev/null | tee -a "$LOG" || true
