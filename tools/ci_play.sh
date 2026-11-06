#!/usr/bin/env bash
# Headless in-game smoke test driver (iteration 4: quickPlay, no menu clicks).
# The client is launched with --quickPlaySingleplayer OctoTest into a
# pre-made world whose level.dat already has cheats + creative enabled
# (tools/make_test_world.py). This script only waits for the player to log
# in, then screenshots and types chat commands.
set -u
export DISPLAY="${DISPLAY:-:99}"
SHOT_DIR="run/screenshots"
GAME_LOG="run/logs/latest.log"
LOG="ci-play.log"

log() { echo "[play] $*" | tee -a "$LOG"; }

key() {
  xdotool key --clearmodifiers "$@" 2>/dev/null || true
  sleep 2
}

shot() {
  key F2
  sleep 5
}

type_cmd() {
  # 't' opens chat WITHOUT the slash prefix; the slash key would prefill '/'
  # and the typed leading slash would double up (//cmd -> server sees /cmd).
  key t
  xdotool type --clearmodifiers --delay 35 "$1" 2>/dev/null || true
  sleep 1
  key Return
  sleep 6
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

# --- wait for the player to join the pre-made world ---
log "waiting for 'logged in' (quickPlay world load) up to 14 min..."
READY=0
for i in $(seq 1 84); do
  if grep -q "logged in with entity id" "$GAME_LOG" 2>/dev/null; then READY=1; break; fi
  sleep 10
  if [ $((i % 6)) -eq 0 ]; then
    shot; log "shot: world loading progress ($((i * 10)) s)"
  fi
done
if [ "$READY" -ne 1 ]; then
  log "WARN: no login line after 14 min; shooting anyway"
fi
sleep 60   # first frames + chunk meshing on llvmpipe
shot; log "shot: in-world (first person)"

key F5; sleep 10
shot; log "shot: in-world (third person back)"

# --- commands (cheats in level.dat + ops.json level 4) ---
type_cmd "/help"
shot; log "shot: chat feedback for /help (tree diagnostics)"
type_cmd "/gamemode creative"
type_cmd "/give @s octoarms:spider_mech_chestplate 1"
type_cmd "/give @s octoarms:diamond_mech_helmet 1"
type_cmd "/give @s octoarms:netherite_mech_leggings 1"
type_cmd "/give @s octoarms:iron_mech_boots 1"
type_cmd "/give @s octoarms:arm_cannon 1"
type_cmd "/give @s octoarms:railgun 1"
type_cmd "/give @s octoarms:plasma_blade 1"
type_cmd "/time set noon"
type_cmd "/give @s octoarms:advanced_power_cell 2"
type_cmd "/item replace entity @s armor.head with octoarms:spider_mech_helmet"
type_cmd "/item replace entity @s armor.chest with octoarms:spider_mech_chestplate"
type_cmd "/item replace entity @s armor.legs with octoarms:spider_mech_leggings"
type_cmd "/item replace entity @s armor.feet with octoarms:spider_mech_boots"
sleep 15
key F5; sleep 8
shot; log "shot: wearing full mixed mech set (3rd person front)"

# --- first person: look down at own huge mech boots ---
key F5; sleep 4
type_cmd "/tp @s ~ ~ ~ ~360 55"
sleep 3
shot; log "shot: first-person view of own mech boots/legs"
type_cmd "/tp @s ~ ~ ~ ~360 0"
sleep 2

# --- close-up: spider suit on an armor stand at (+2,+2), aim with /tp ---
type_cmd "/summon minecraft:armor_stand ~2 ~ ~2 {ShowArms:1b,NoBasePlate:1b}"
sleep 3
type_cmd "/item replace entity @e[type=minecraft:armor_stand,sort=nearest,limit=1] armor.head with octoarms:spider_mech_helmet"
type_cmd "/item replace entity @e[type=minecraft:armor_stand,sort=nearest,limit=1] armor.chest with octoarms:spider_mech_chestplate"
type_cmd "/item replace entity @e[type=minecraft:armor_stand,sort=nearest,limit=1] armor.legs with octoarms:spider_mech_leggings"
type_cmd "/item replace entity @e[type=minecraft:armor_stand,sort=nearest,limit=1] armor.feet with octoarms:spider_mech_boots"
type_cmd "/tp @s ~0.5 ~ ~0.5 -45 5"
sleep 5
shot; log "shot: armor-stand close-up of spider mech (colossal legs)"

# --- scale comparison: player beside the suited stand, 3rd person front ---
key F5; sleep 4
key F5; sleep 6
shot; log "shot: scale comparison player vs suited armor stand"

# --- flight + cling showcase: double jump starts hover on suits that can ---
# spider core cannot fly; borrow a diamond core for the hover demo, then swap back
type_cmd "/item replace entity @s armor.chest with octoarms:diamond_mech_chestplate"
sleep 3
key F5; sleep 4
key F5; sleep 6
key space; sleep 2
key space; sleep 8
shot; log "shot: hover/jetpack attempt (diamond core)"
type_cmd "/item replace entity @s armor.chest with octoarms:spider_mech_chestplate"
sleep 2

# --- creative inventory (shows item grids; mod tab reachable by clicks later) ---
key e; sleep 25
shot; log "shot: creative inventory"

key Escape; sleep 10
shot; log "shot: pause menu"

log "automation finished; screenshots:"
ls -la "$SHOT_DIR" 2>/dev/null | tee -a "$LOG" || true
log "command feedback from game log:"
grep -E "Given |Replaced|Game mode" "$GAME_LOG" 2>/dev/null | tail -15 | tee -a "$LOG" || true
