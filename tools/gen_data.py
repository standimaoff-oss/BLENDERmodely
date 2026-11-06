"""Generates every data-driven resource: models, blockstates, tags, recipes, lang.

Run `python3 tools/generate.py` after changing content tables.
"""

import json
import os

import content as C
from pixel import Canvas, hexcol, lighten, darken, mix

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
RES = os.path.join(ROOT, "src", "main", "resources")
ASSETS = os.path.join(RES, "assets", C.MODID)
DATA = os.path.join(RES, "data", C.MODID)
MCTAGS = os.path.join(RES, "data", "minecraft", "tags", "blocks")


def write_json(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as fh:
        json.dump(obj, fh, ensure_ascii=False, indent=2)
        fh.write("\n")
    return path


def rl(name):
    return f"{C.MODID}:{name}"


def mc(name):
    return f"minecraft:{name}"


# --------------------------------------------------------------------------- #
# item models
# --------------------------------------------------------------------------- #

def item_models():
    made = []
    handheld = set(C.WEAPONS)
    for name in list(C.COMPONENTS) + list(C.WEAPONS) + C.armor_items():
        parent = mc("item/handheld") if name in handheld else mc("item/generated")
        made.append(write_json(
            os.path.join(ASSETS, "models", "item", f"{name}.json"),
            {"parent": parent, "textures": {"layer0": f"{C.MODID}:item/{name}"}}))

    for name in list(C.BLOCKS_SIMPLE) + list(C.MACHINES):
        made.append(write_json(
            os.path.join(ASSETS, "models", "item", f"{name}.json"),
            {"parent": f"{C.MODID}:block/{name}"}))
    return made


# --------------------------------------------------------------------------- #
# block models + blockstates
# --------------------------------------------------------------------------- #

def block_models():
    made = []
    for name in C.BLOCKS_SIMPLE:
        made.append(write_json(
            os.path.join(ASSETS, "models", "block", f"{name}.json"),
            {"parent": mc("block/cube_all"), "textures": {"all": f"{C.MODID}:block/{name}"}}))

    for name in C.MACHINES:
        tex = {"parent": mc("block/orientable_with_bottom"), "textures": {
            "top": f"{C.MODID}:block/{name}_top",
            "bottom": f"{C.MODID}:block/{name}_bottom",
            "side": f"{C.MODID}:block/{name}_side",
            "front": f"{C.MODID}:block/{name}_front",
        }}
        made.append(write_json(os.path.join(ASSETS, "models", "block", f"{name}.json"), tex))
        tex_on = json.loads(json.dumps(tex))
        tex_on["textures"]["front"] = f"{C.MODID}:block/{name}_front_active"
        made.append(write_json(os.path.join(ASSETS, "models", "block", f"{name}_on.json"), tex_on))
    return made


def blockstates():
    made = []
    for name in C.BLOCKS_SIMPLE:
        made.append(write_json(
            os.path.join(ASSETS, "blockstates", f"{name}.json"),
            {"variants": {"": {"model": rl(f"block/{name}")}}}))

    for name in C.MACHINES:
        variants = {}
        for facing, rot in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
            for lit, suffix in ((False, ""), (True, "_on")):
                key = f"facing={facing},lit={str(lit).lower()}"
                entry = {"model": rl(f"block/{name}{suffix}")}
                if rot:
                    entry["y"] = rot
                variants[key] = entry
        made.append(write_json(os.path.join(ASSETS, "blockstates", f"{name}.json"), {"variants": variants}))
    return made


# --------------------------------------------------------------------------- #
# tags
# --------------------------------------------------------------------------- #

def tags():
    made = []
    machines = list(C.MACHINES)
    allblocks = machines + list(C.BLOCKS_SIMPLE)

    made.append(write_json(os.path.join(MCTAGS, "mineable", "pickaxe.json"),
                           {"replace": False, "values": [rl(b) for b in allblocks]}))
    made.append(write_json(os.path.join(MCTAGS, "needs_stone_tool.json"),
                           {"replace": False, "values": [rl(b) for b in ("steel_casing", "coil_block")]}))
    made.append(write_json(os.path.join(MCTAGS, "needs_iron_tool.json"),
                           {"replace": False, "values": [rl(b) for b in machines]}))

    # surface behaviour tags used by the climbing physics
    made.append(write_json(os.path.join(DATA, "tags", "blocks", "slippery.json"), {"replace": False, "values": [
        mc("ice"), mc("packed_ice"), mc("blue_ice"), mc("frosted_ice"),
    ]}))
    made.append(write_json(os.path.join(DATA, "tags", "blocks", "sticky.json"), {"replace": False, "values": [
        mc("honey_block"), mc("slime_block"), mc("mud"), mc("clay"), mc("soul_sand"), mc("wet_sponge"),
    ]}))
    made.append(write_json(os.path.join(DATA, "tags", "blocks", "loose.json"), {"replace": False, "values": [
        mc("sand"), mc("red_sand"), mc("gravel"), mc("soul_soil"), mc("snow_block"),
        mc("dirt"), mc("coarse_dirt"), mc("podzol"), mc("mycelium"), mc("netherrack"),
    ]}))
    made.append(write_json(os.path.join(DATA, "tags", "blocks", "metallic.json"), {"replace": False, "values": [
        mc("iron_block"), mc("copper_block"), mc("gold_block"), mc("diamond_block"),
        mc("netherite_block"), mc("emerald_block"), mc("chain"), mc("anvil"),
        mc("iron_bars"), mc("lightning_rod"), mc("raw_iron_block"), mc("raw_copper_block"),
        mc("raw_gold_block"), mc("lapis_block"), mc("redstone_block"),
    ]}))
    made.append(write_json(os.path.join(DATA, "tags", "blocks", "no_grip.json"), {"replace": False, "values": [
        mc("water"), mc("lava"), mc("bubble_column"), mc("cobweb"), mc("powder_snow"),
        mc("magma_block"), mc("cactus"), mc("sweet_berry_bush"), mc("fire"), mc("soul_fire"),
    ]}))

    # convenience item tags
    made.append(write_json(os.path.join(RES, "data", C.MODID, "tags", "items", "mech_armor.json"),
                           {"replace": False, "values": [rl(a) for a in C.armor_items()]}))
    made.append(write_json(os.path.join(RES, "data", C.MODID, "tags", "items", "power_cells.json"),
                           {"replace": False, "values": [rl(c) for c in
                                                         ("power_cell", "advanced_power_cell", "nether_power_cell")]}))
    return made


# --------------------------------------------------------------------------- #
# recipes
# --------------------------------------------------------------------------- #

CRAFTING = {
    "hydraulic_press": {
        "pattern": ["III", "IPI", "CFC"],
        "key": {"I": mc("iron_ingot"), "P": mc("piston"), "C": mc("copper_ingot"), "F": mc("furnace")},
        "result": (rl("hydraulic_press"), 1),
    },
    "charging_dock": {
        "pattern": ["CRC", "IDI", "IFI"],
        "key": {"C": mc("copper_ingot"), "R": mc("redstone_block"), "I": mc("iron_ingot"),
                "D": mc("redstone"), "F": mc("furnace")},
        "result": (rl("charging_dock"), 1),
    },
    "mech_assembler": {
        "pattern": ["IPI", "CRC", "SBS"],
        "key": {"I": mc("iron_ingot"), "P": mc("piston"), "C": mc("crafting_table"),
                "R": mc("redstone_block"), "S": rl("steel_casing"), "B": mc("iron_block")},
        "result": (rl("mech_assembler"), 1),
    },
    "steel_casing": {
        "pattern": ["PPP", "PIP", "PPP"],
        "key": {"P": rl("iron_plate"), "I": mc("iron_ingot")},
        "result": (rl("steel_casing"), 1),
    },
    "coil_block": {
        "pattern": ["CCC", "CRC", "CCC"],
        "key": {"C": rl("copper_plate"), "R": mc("redstone_block")},
        "result": (rl("coil_block"), 1),
    },
    "actuator": {
        "pattern": [" P ", "GIG", " R "],
        "key": {"P": rl("iron_plate"), "G": rl("copper_gear"), "I": mc("iron_ingot"), "R": mc("redstone")},
        "result": (rl("actuator"), 2),
    },
    "heavy_actuator": {
        "pattern": ["APA", "PHP", "APA"],
        "key": {"A": rl("actuator"), "P": rl("iron_plate"), "H": rl("hydraulic_piston")},
        "result": (rl("heavy_actuator"), 1),
    },
    "circuit_board": {
        "shapeless": [rl("copper_plate"), mc("redstone"), mc("redstone")],
        "result": (rl("circuit_board"), 1),
    },
    "advanced_circuit": {
        "shapeless": [rl("circuit_board"), rl("gold_plate"), mc("redstone_block")],
        "result": (rl("advanced_circuit"), 1),
    },
    "power_core": {
        "pattern": ["IRI", "RCR", "IRI"],
        "key": {"I": rl("iron_plate"), "R": mc("redstone_block"), "C": rl("circuit_board")},
        "result": (rl("power_core"), 1),
    },
    "nether_power_core": {
        "shapeless": [rl("power_core"), mc("netherite_ingot"), mc("blaze_rod"), mc("blaze_rod")],
        "result": (rl("nether_power_core"), 1),
    },
    "reinforced_frame": {
        "pattern": ["PIP", "AFA", "PIP"],
        "key": {"P": rl("iron_plate"), "I": mc("iron_ingot"), "A": rl("actuator"), "F": rl("obsidian_plate")},
        "result": (rl("reinforced_frame"), 1),
    },
    "thruster_module": {
        "pattern": [" P ", "BCB", " P "],
        "key": {"P": rl("iron_plate"), "B": mc("blaze_powder"), "C": rl("copper_gear")},
        "result": (rl("thruster_module"), 1),
    },
    "octo_arm": {
        "pattern": ["SHS", "AMA", "SCS"],
        "key": {"S": rl("silk_mesh"), "H": rl("heavy_actuator"), "A": rl("actuator"),
                "M": rl("advanced_circuit"), "C": rl("netherite_plate")},
        "result": (rl("octo_arm"), 1),
    },
    "power_cell": {
        "pattern": ["PCP", "GRG", "PCP"],
        "key": {"P": rl("copper_plate"), "C": mc("copper_ingot"), "G": rl("copper_gear"), "R": mc("redstone_block")},
        "result": (rl("power_cell"), 1),
    },
    "advanced_power_cell": {
        "pattern": ["PGP", "CIC", "PGP"],
        "key": {"P": rl("gold_plate"), "G": mc("glowstone"), "C": rl("advanced_circuit"), "I": rl("power_cell")},
        "result": (rl("advanced_power_cell"), 1),
    },
    "nether_power_cell": {
        "pattern": ["PNP", "CRC", "PNP"],
        "key": {"P": rl("netherite_plate"), "N": rl("nether_power_core"), "C": rl("advanced_power_cell"),
                "R": rl("reinforced_frame")},
        "result": (rl("nether_power_cell"), 1),
    },
}

PRESSING = [
    ([ (mc("iron_ingot"), 1) ], (rl("iron_plate"), 1), 120),
    ([ (mc("copper_ingot"), 1) ], (rl("copper_plate"), 1), 100),
    ([ (mc("gold_ingot"), 1) ], (rl("gold_plate"), 1), 120),
    ([ (mc("diamond"), 1) ], (rl("diamond_plate"), 1), 200),
    ([ (mc("netherite_ingot"), 1) ], (rl("netherite_plate"), 1), 300),
    ([ (mc("obsidian"), 1) ], (rl("obsidian_plate"), 1), 200),
    ([ (mc("iron_ingot"), 2) ], (rl("iron_gear"), 1), 140),
    ([ (mc("copper_ingot"), 2) ], (rl("copper_gear"), 1), 120),
    ([ (mc("diamond"), 2) ], (rl("diamond_gear"), 1), 220),
    ([ (mc("netherite_ingot"), 2) ], (rl("netherite_gear"), 1), 320),
    ([ (mc("coal"), 2), (mc("blaze_powder"), 1) ], (rl("enriched_coal"), 2), 160),
    ([ (mc("coal_block"), 1), (mc("blaze_powder"), 2) ], (rl("enriched_coal"), 8), 240),
    ([ (mc("piston"), 1), (rl("iron_plate"), 2) ], (rl("hydraulic_piston"), 1), 180),
    ([ (mc("glowstone_dust"), 2), (mc("redstone"), 2) ], (rl("energy_orb"), 2), 100),
    ([ (rl("iron_plate"), 1), (rl("copper_plate"), 1) ], (rl("heavy_slug"), 2), 120),
    ([ (mc("string"), 4), (mc("slime_ball"), 1) ], (rl("silk_mesh"), 1), 140),
]

# tier -> (vanilla base armor piece ids, plate, gear, module, extra)
TIER_PARTS = {
    "copper_mech": dict(base=["leather_helmet", "leather_chestplate", "leather_leggings", "leather_boots"],
                        plate="copper_plate", gear="copper_gear", servo="actuator",
                        circuit="circuit_board", core="power_core"),
    "iron_mech": dict(base=["iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots"],
                      plate="iron_plate", gear="iron_gear", servo="actuator",
                      circuit="circuit_board", core="power_core"),
    "gold_mech": dict(base=["golden_helmet", "golden_chestplate", "golden_leggings", "golden_boots"],
                      plate="gold_plate", gear="copper_gear", servo="thruster_module",
                      circuit="advanced_circuit", core="power_core"),
    "diamond_mech": dict(base=["diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots"],
                         plate="diamond_plate", gear="diamond_gear", servo="heavy_actuator",
                         circuit="advanced_circuit", core="power_core"),
    "netherite_mech": dict(base=["netherite_helmet", "netherite_chestplate", "netherite_leggings", "netherite_boots"],
                           plate="netherite_plate", gear="netherite_gear", servo="heavy_actuator",
                           circuit="advanced_circuit", core="nether_power_core"),
    "spider_mech": dict(base=["netherite_helmet", "netherite_chestplate", "netherite_leggings", "netherite_boots"],
                        plate="netherite_plate", gear="netherite_gear", servo="heavy_actuator",
                        circuit="advanced_circuit", core="nether_power_core"),
}

# piece index -> (plate count, gear count, servo count, energy)
PIECE_COST = {
    0: dict(plate=3, gear=1, servo=2, extra=[], energy=12000),
    1: dict(plate=6, gear=2, servo=3, extra=["frame", "core2", "thruster"], energy=28000),
    2: dict(plate=5, gear=2, servo=2, extra=["core"], energy=20000),
    3: dict(plate=3, gear=1, servo=2, extra=["piston"], energy=14000),
}

TIER_MULT = {"copper_mech": 0.5, "iron_mech": 1.0, "gold_mech": 1.2, "diamond_mech": 2.0,
             "netherite_mech": 3.5, "spider_mech": 4.0}


def assembly_recipes():
    out = []
    for tier in C.TIER_ORDER:
        parts = TIER_PARTS[tier]
        mult = TIER_MULT[tier]
        for idx, (piece, _en, _ru) in enumerate(C.PIECES):
            cost = PIECE_COST[idx]
            ings = [(mc(parts["base"][idx]), 1)]
            ings.append((rl(parts["plate"]), max(1, int(cost["plate"] * mult))))
            if cost["gear"]:
                ings.append((rl(parts["gear"]), max(1, int(cost["gear"] * (1 if mult < 2 else 2)))))
            ings.append((rl(parts["servo"]), max(1, int(cost["servo"] * (1 if mult < 2 else 2)))))
            ings.append((rl(parts["circuit"]), max(1, int(mult))))
            for extra in cost["extra"]:
                if extra == "frame":
                    ings.append((rl("reinforced_frame"), max(1, int(mult))))
                elif extra == "core":
                    ings.append((rl(parts["core"]), 1))
                elif extra == "core2":
                    ings.append((rl(parts["core"]), max(1, int(mult))))
                elif extra == "thruster":
                    if tier in ("gold_mech", "diamond_mech", "netherite_mech", "spider_mech"):
                        ings.append((rl("thruster_module"), max(1, int(mult))))
                elif extra == "piston":
                    ings.append((rl("hydraulic_piston"), max(1, int(mult))))
            if tier == "spider_mech":
                ings.append((rl("octo_arm"), [2, 4, 3, 3][idx]))
                ings.append((rl("silk_mesh"), [2, 4, 3, 3][idx]))
            if tier == "netherite_mech":
                ings.append((rl("obsidian_plate"), max(1, int(mult))))
            energy = int(cost["energy"] * mult)
            out.append((f"{tier}_{piece}", ings, (rl(f"{tier}_{piece}"), 1), energy))

    # weapons
    out.append(("plasma_blade", [
        (mc("diamond_sword"), 1), (rl("energy_orb"), 3), (rl("power_core"), 1),
        (rl("advanced_circuit"), 1), (rl("diamond_plate"), 2),
    ], (rl("plasma_blade"), 1), 40000))
    out.append(("arm_cannon", [
        (rl("iron_plate"), 3), (rl("power_core"), 1), (rl("energy_orb"), 2),
        (rl("circuit_board"), 1), (rl("actuator"), 2),
    ], (rl("arm_cannon"), 1), 20000))
    out.append(("railgun", [
        (rl("heavy_slug"), 3), (rl("advanced_circuit"), 2), (rl("nether_power_core"), 1),
        (rl("heavy_actuator"), 2), (rl("obsidian_plate"), 2),
    ], (rl("railgun"), 1), 70000))
    return out


def recipes():
    made = []
    rdir = os.path.join(DATA, "recipes")

    for name, spec in CRAFTING.items():
        result = {"item": spec["result"][0], "count": spec["result"][1]}
        if "shapeless" in spec:
            obj = {
                "type": mc("crafting_shapeless"),
                "ingredients": [{"item": i} for i in spec["shapeless"]],
                "result": result,
            }
        else:
            obj = {
                "type": mc("crafting_shaped"),
                "pattern": spec["pattern"],
                "key": {k: {"item": v} for k, v in spec["key"].items()},
                "result": result,
            }
        made.append(write_json(os.path.join(rdir, f"{name}.json"), obj))

    for i, (ings, result, time) in enumerate(PRESSING):
        obj = {
            "type": rl("pressing"),
            "ingredients": [{"item": item, "count": count} for item, count in ings],
            "result": {"item": result[0], "count": result[1]},
            "process_time": time,
        }
        made.append(write_json(os.path.join(rdir, f"pressing_{i:02d}_{os.path.basename(result[0].split(':')[-1])}.json"), obj))

    for name, ings, result, energy in assembly_recipes():
        obj = {
            "type": rl("assembly"),
            "ingredients": [{"item": item, "count": count} for item, count in ings],
            "result": {"item": result[0], "count": result[1]},
            "energy": energy,
        }
        made.append(write_json(os.path.join(rdir, f"assembly_{name}.json"), obj))

    return made


# --------------------------------------------------------------------------- #
# lang
# --------------------------------------------------------------------------- #

def lang():
    entries = C.lang_entries()
    made = []
    for code, idx in (("en_us", 0), ("ru_ru", 1)):
        obj = {}
        for key, pair in entries.items():
            obj[key] = pair[idx]
        # language self-reference
        obj["language.name"] = "English" if code == "en_us" else "Русский"
        obj["language.region"] = "US" if code == "en_us" else "RU"
        obj["language.code"] = code
        made.append(write_json(os.path.join(ASSETS, "lang", f"{code}.json"), obj))
    return made


# --------------------------------------------------------------------------- #
# logo
# --------------------------------------------------------------------------- #

def logo():
    size = 128
    cv = Canvas(size, size)
    bg = hexcol("#141821")
    cv.rect(0, 0, size, size, bg)
    for i in range(size):
        cv.rect(0, i, size, 1, mix(bg, hexcol("#223047"), i / size))

    steel = hexcol("#57606E")
    accent = hexcol("#93A0B2")
    glow = hexcol("#B4FF3C")
    red = hexcol("#FF3B30")

    # octagon ring
    cx = cy = 64.0
    for i in range(360):
        a = math.radians(i) if False else __import__("math").radians(i)
        r = 46 + 4 * __import__("math").cos(8 * a)
        x = cx + __import__("math").cos(a) * r
        y = cy + __import__("math").sin(a) * r
        cv.circle(x, y, 2.2, accent if i % 9 else steel)

    # mech head
    cv.metal(38, 40, 52, 44, steel, seed=7)
    cv.frame(38, 40, 52, 44, darken(steel, 0.6))
    cv.rect(44, 56, 40, 12, red)
    cv.frame(44, 56, 40, 12, hexcol("#2A0A08"))
    for k in range(4):
        cv.px(50 + k * 10, 60, hexcol("#FFD0CC"))
    cv.rect(58, 34, 12, 8, accent)
    cv.px(62, 36, glow)
    cv.px(66, 36, glow)
    # eight legs hint
    for k in range(4):
        y = 46 + k * 12
        cv.line(36, y, 14, y - 10 + k * 6, steel)
        cv.line(14, y - 10 + k * 6, 6, y + 6 + k * 4, accent)
        cv.line(92, y, 114, y - 10 + k * 6, steel)
        cv.line(114, y - 10 + k * 6, 122, y + 6 + k * 4, accent)
        cv.px(6, int(y + 6 + k * 4), glow)
        cv.px(122, int(y + 6 + k * 4), glow)

    path = os.path.join(RES, "octoarms_logo.png")
    cv.save(path)
    print(f"[logo] {path}")
    return path


import math  # noqa: E402  (used by logo())


def generate():
    counts = []
    counts.append(len(item_models()))
    counts.append(len(block_models()))
    counts.append(len(blockstates()))
    counts.append(len(tags()))
    counts.append(len(recipes()))
    counts.append(len(lang()))
    logo()
    print(f"[data] wrote {sum(counts)} json files: items={counts[0]} blockmodels={counts[1]} "
          f"blockstates={counts[2]} tags={counts[3]} recipes={counts[4]} lang={counts[5]}")


if __name__ == "__main__":
    generate()
