#!/usr/bin/env python3
"""Create a minimal but valid MC 1.20.1 world save (level.dat only).

Chunks are generated on demand by the game; `initialized: 0` makes the server
run first-load spawn setup. GameType=1 (creative) + allowCommands=1 give the
headless test player cheats without any menu clicks.

Usage: python3 tools/make_test_world.py <saves-dir> [world-name]
"""
import gzip
import struct
import sys
from pathlib import Path

def tag(t: int, name: str, payload: bytes) -> bytes:
    return bytes([t]) + struct.pack(">H", len(name)) + name.encode() + payload

def b(v: int) -> bytes:  return struct.pack(">b", v)
def i(v: int) -> bytes:  return struct.pack(">i", v)
def l(v: int) -> bytes:  return struct.pack(">q", v)
def s(v: str) -> bytes:  return struct.pack(">H", len(v)) + v.encode()

def compound(entries: list[tuple[int, str, bytes]]) -> bytes:
    out = b"".join(tag(t, n, p) for t, n, p in entries)
    return out + b"\x00"

def main() -> None:
    saves = Path(sys.argv[1] if len(sys.argv) > 1 else "run/saves")
    name = sys.argv[2] if len(sys.argv) > 2 else "OctoTest"
    seed = 20261106

    version = compound([
        (0x03, "Id", i(3463)),
        (0x08, "Name", s("1.20.1")),
        (0x08, "Series", s("main")),
        (0x01, "Snapshot", b(0)),
        (0x03, "Target", i(3463)),
    ])
    generator = compound([
        (0x01, "bonus_chest", b(0)),
        (0x01, "generate_features", b(1)),
        (0x04, "seed", l(seed)),
    ])
    data = compound([
        (0x01, "allowCommands", b(1)),       # cheats ON
        (0x03, "GameType", i(1)),            # creative
        (0x01, "hardcore", b(0)),
        (0x01, "Difficulty", b(2)),
        (0x01, "initialized", b(0)),         # run first-load spawn setup
        (0x01, "raining", b(0)),
        (0x01, "thundering", b(0)),
        (0x03, "rainTime", i(0)),
        (0x03, "thunderTime", i(0)),
        (0x04, "Time", l(0)),
        (0x04, "DayTime", l(1000)),
        (0x04, "RandomSeed", l(seed)),
        (0x04, "LastPlayed", l(0)),
        (0x03, "SpawnX", i(0)),
        (0x03, "SpawnY", i(64)),
        (0x03, "SpawnZ", i(0)),
        (0x08, "LevelName", s(name)),
        (0x0A, "Version", version),
        (0x0A, "generator", generator),
    ])
    root = tag(0x0A, "", tag(0x0A, "Data", data) + b"\x00")

    world = saves / name
    world.mkdir(parents=True, exist_ok=True)
    with gzip.open(world / "level.dat", "wb") as fh:
        fh.write(root)
    # empty datapacks dir keeps the loader quiet
    (world / "datapacks").mkdir(exist_ok=True)
    print(f"wrote {world / 'level.dat'} ({(world / 'level.dat').stat().st_size} bytes gz)")

if __name__ == "__main__":
    main()
