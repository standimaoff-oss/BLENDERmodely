#!/usr/bin/env bash
# Headless in-game smoke test driver (iteration 2).
# Requires: Xvfb on DISPLAY (default :99), xdotool, and a Minecraft client
# starting up in the background (gradlew runClient).
#
# Title -> Mods list -> Singleplayer -> Create New World (Creative + cheats,
# best effort) -> wait for the integrated server log line "Done (" -> spawn,
# equip mech armor via commands, F2 screenshots after every step.
# No Escape presses while the world is loading (that aborts generation).
set -u
export DISPLAY="${DISPLAY:-:99}"
SHOT_DIR="run/screenshots"
GAME_LOG="run/logs/latest.log"
LOG="ci-play.log"

log() { echo "[play] $*" | tee -a "$LOG"; }

key() {
  xdotool windowfocus "$WIN" 2>/dev/null || true
  xdotool key --clearmodifiers "$@" 2>/dev/null || true
  sleep 2
}

shot() {
  key F2
  sleep 5
}

type_cmd() {
  key slash
  xdotool windowfocus "$WIN" 2>/dev/null || true
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

# --- title screen (llvmpipe needs ~90 s to stabilise) ---
sleep 90
shot; log "shot: title screen"

# --- mods list (proves the mod is loaded in-game) ---
key Tab; sleep 3      # -> Multiplayer
key Tab; sleep 3      # -> Mods
key Return; sleep 20
shot; log "shot: mods list"
key Escape; sleep 20  # back to title (safe here)

# --- Singleplayer -> world list ---
key Return; sleep 45
shot; log "shot: world list"

# --- Create New World screen ---
key Return; sleep 45
shot; log "shot: create-world screen"

# name field focused; Tab -> Game Mode; Enter -> Creative
key Tab; sleep 3
key Return; sleep 5
shot; log "shot: create-world, game mode toggled"
# Tab -> Difficulty, Tab -> Allow Cheats; Enter -> ON
key Tab; sleep 3
key Tab; sleep 3
key Return; sleep 5
shot; log "shot: create-world, cheats toggled"

# --- create the world; wait for the server 'Done (' line in the log ---
key Return
log "waiting for world generation (log line 'Done (') up to 10 min..."
READY=0
for i in $(seq 1 60); do
  if grep -q "Done (" "$GAME_LOG" 2>/dev/null; then READY=1; break; fi
  sleep 10
  if [ $((i % 6)) -eq 0 ]; then
    shot; log "shot: world loading progress ($((i * 10)) s)"
  fi
done
if [ "$READY" -ne 1 ]; then
  log "WARN: no 'Done (' line after 10 min; shooting anyway"
fi
sleep 45   # let the first frames render on llvmpipe
shot; log "shot: in-world (first person)"

# --- third person ---
key F5; sleep 8
shot; log "shot: in-world (third person)"

# --- commands (only effective with cheats; harmless otherwise) ---
type_cmd "/gamemode creative"
type_cmd "/give @s octoarms:spider_mech_chestplate 1"
type_cmd "/give @s octoarms:diamond_mech_helmet 1"
type_cmd "/give @s octoarms:arm_cannon 1"
type_cmd "/give @s octoarms:railgun 1"
type_cmd "/item replace entity @s armor.chest with octoarms:spider_mech_chestplate"
type_cmd "/item replace entity @s armor.head with octoarms:diamond_mech_helmet"
sleep 10
key F5; sleep 6   # front view so the mech rig shows
shot; log "shot: wearing mech armor (3rd person front)"

# --- inventory / creative items ---
key e; sleep 25
shot; log "shot: inventory / creative items"

# --- pause menu (safe now, world exists) ---
key Escape; sleep 10
shot; log "shot: pause menu"

log "automation finished; screenshots:"
ls -la "$SHOT_DIR" 2>/dev/null | tee -a "$LOG" || true
