#!/usr/bin/env bash
# Headless in-game smoke test driver.
# Requires: Xvfb on DISPLAY (default :99), xdotool, and a Minecraft client
# that is starting up in the background (gradlew runClient).
#
# Navigates: title screen -> Singleplayer -> Create New World (Creative +
# cheats, best effort) -> spawns -> equips the spider mech via commands ->
# presses F2 after every step so run/screenshots/ fills with evidence.
set -u
export DISPLAY="${DISPLAY:-:99}"
SHOT_DIR="run/screenshots"
LOG="ci-play.log"

log() { echo "[play] $*" | tee -a "$LOG"; }

key() {
  xdotool windowfocus "$WIN" 2>/dev/null || true
  xdotool key --clearmodifiers "$@" 2>/dev/null || true
  sleep 2
}

shot() {
  key F2
  sleep 4
}

type_cmd() {
  key slash
  xdotool windowfocus "$WIN" 2>/dev/null || true
  xdotool type --clearmodifiers --delay 35 "$1" 2>/dev/null || true
  sleep 1
  key Return
  sleep 4
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

# --- title screen ---
sleep 100
shot; log "shot: title screen"
key Escape; sleep 4
shot; log "shot: title screen (clean)"

# --- Singleplayer -> world list ---
key Return; sleep 30
shot; log "shot: world list"

# --- Create New World screen ---
key Return; sleep 20
shot; log "shot: create-world screen"

# name field is focused by default; Tab -> Game Mode button; Enter -> Creative
key Tab; sleep 2
key Return; sleep 3
shot; log "shot: create-world, game mode toggled (hopefully creative)"
# Tab -> Difficulty, Tab -> Allow Cheats; Enter -> ON (best effort, layout may vary)
key Tab; sleep 2
key Tab; sleep 2
key Return; sleep 3
shot; log "shot: create-world, cheats toggled (hopefully on)"

# --- create + spawn ---
key Return
log "waiting 150 s for world generation and chunk loads..."
sleep 150
shot; log "shot: in-world (first person)"

# --- third person ---
key F5; sleep 6
shot; log "shot: in-world (third person)"

# --- commands (only effective if cheats got enabled; harmless otherwise) ---
type_cmd "/gamemode creative"
type_cmd "/give @s octoarms:spider_mech_chestplate 1"
type_cmd "/give @s octoarms:netherite_mech_helmet 1"
type_cmd "/give @s octoarms:arm_cannon 1"
type_cmd "/give @s octoarms:railgun 1"
type_cmd "/item replace entity @s armor.chest with octoarms:spider_mech_chestplate"
type_cmd "/item replace entity @s armor.head with octoarms:diamond_mech_helmet"
sleep 8
key F5; sleep 4   # third-person front so the mech rig is visible
shot; log "shot: wearing spider mech chestplate + diamond helmet (3rd person)"

# --- creative inventory (shows the Octo Arms creative tab) ---
key e; sleep 20
shot; log "shot: inventory / creative items"
key Escape; sleep 6
shot; log "shot: pause menu"

log "automation finished; screenshots:"
ls -la "$SHOT_DIR" 2>/dev/null | tee -a "$LOG" || true
