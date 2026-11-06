"""Generates the bulky mech armor models + their textures.

The generator is the single source of truth: it lays out every cube on a
128x128 texture, paints it, and emits `ArmorSpecs.java` so the in-game model
matches the pixels exactly.
"""

import os

import content as C
from pixel import Canvas, hexcol, lighten, darken, mix, paint_cube

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
TEX_OUT = os.path.join(ROOT, "src", "main", "resources", "assets", C.MODID, "textures", "models", "armor")
JAVA_OUT = os.path.join(ROOT, "src", "main", "java", "com", "octoarms", "client", "model", "ArmorSpecs.java")
TEX_SIZE = 256

# part pivots must match the vanilla player rig so poses carry over
PIVOTS = {
    "head": (0.0, 24.0, 0.0),
    "hat": (0.0, 24.0, 0.0),
    "body": (0.0, 24.0, 0.0),
    "right_arm": (-5.0, 22.0, 0.0),
    "left_arm": (5.0, 22.0, 0.0),
    "right_leg": (-1.9, 12.0, 0.0),
    "left_leg": (1.9, 12.0, 0.0),
}


def box(part, size, offset, style="plate", name=None):
    return {"part": part, "size": tuple(size), "offset": tuple(offset), "style": style, "name": name}


# --------------------------------------------------------------------------- #
# base rig shared by every suit
# --------------------------------------------------------------------------- #

def base_boxes(defl=0.0):
    d = defl
    return [
        # head
        box("head", (10, 10, 10), (-5.0, -9.0, -5.0), "plate"),
        box("head", (9, 3, 1), (-4.5, -7.0, -5.6), "visor"),
        box("head", (11, 2, 11), (-5.5, -1.2, -5.5), "dark"),          # neck seal
        # torso
        box("body", (9, 13, 5), (-4.5, -0.6, -2.5), "plate"),
        box("body", (3, 3, 1), (-1.5, 3.2, -3.2), "glow"),             # reactor core
        box("body", (10, 3, 7), (-5.0, -1.8, -3.5), "dark"),           # collar
        box("body", (9, 8, 4), (-4.5, 0.6, 2.4), "accent"),            # backpack
        box("body", (3, 3, 2), (-1.5, 9.4, 3.2), "dark"),              # waist servo
        # arms
        box("right_arm", (6, 5, 6), (-3.6, -2.8, -3.0), "accent"),     # pauldron
        box("right_arm", (5, 7, 5), (-3.2, 1.6, -2.5), "plate"),
        box("right_arm", (6, 6, 6), (-3.6, 7.6, -3.0), "dark"),        # forearm housing
        box("left_arm", (6, 5, 6), (-2.4, -2.8, -3.0), "accent"),
        box("left_arm", (5, 7, 5), (-1.8, 1.6, -2.5), "plate"),
        box("left_arm", (6, 6, 6), (-2.4, 7.6, -3.0), "dark"),
        # legs
        box("right_leg", (6, 7, 6), (-3.7, -1.2, -3.2), "plate"),      # thigh
        box("left_leg", (6, 7, 6), (-2.3, -1.2, -3.2), "plate"),
        box("right_leg", (3, 3, 3), (-3.0, 4.6, -4.6), "accent"),      # knee piston
        box("left_leg", (3, 3, 3), (0.0, 4.6, -4.6), "accent"),
        box("right_leg", (6, 7, 6), (-3.7, 5.2, -3.2), "accent"),      # shin guard
        box("left_leg", (6, 7, 6), (-2.3, 5.2, -3.2), "accent"),
        box("right_leg", (1, 5, 4), (-4.5, 5.6, -2.0), "dark"),        # shin vent out
        box("left_leg", (1, 5, 4), (3.5, 5.6, -2.0), "dark"),
        box("right_leg", (7, 3, 9), (-4.0, 9.0, -5.0), "dark"),        # boot
        box("left_leg", (7, 3, 9), (-3.0, 9.0, -5.0), "dark"),
        box("right_leg", (7, 2, 2), (-4.0, 10.0, -6.4), "accent"),     # toe plate
        box("left_leg", (7, 2, 2), (-3.0, 10.0, -6.4), "accent"),
        box("right_leg", (2, 2, 2), (-3.4, 10.0, 3.6), "dark"),        # heel actuator
        box("left_leg", (2, 2, 2), (-0.5, 10.0, 3.6), "dark"),
    ]


TIER_EXTRA = {
    "copper_mech": [
        box("head", (1, 4, 1), (-6.4, -12.0, 1.0), "accent"),          # antenna
        box("head", (1, 1, 1), (-6.4, -12.6, 1.0), "glow"),
        box("body", (2, 2, 3), (-5.6, 3.0, -3.6), "glow"),
        box("body", (2, 2, 3), (3.6, 3.0, -3.6), "glow"),
    ],
    "iron_mech": [
        box("right_arm", (7, 3, 7), (-4.2, -3.4, -3.5), "plate"),      # heavy pauldron cap
        box("left_arm", (7, 3, 7), (-2.8, -3.4, -3.5), "plate"),
        box("body", (9, 2, 2), (-4.5, 6.4, -3.4), "dark"),             # chest vent
        box("body", (4, 6, 3), (-6.6, 1.0, 2.8), "dark"),              # hip pods
        box("body", (4, 6, 3), (2.6, 1.0, 2.8), "dark"),
    ],
    "gold_mech": [
        box("head", (2, 4, 8), (-1.0, -12.4, -3.0), "accent"),         # crest
        box("head", (2, 1, 8), (-1.0, -13.0, -3.0), "glow"),
        box("body", (11, 4, 6), (-5.5, 9.0, -3.0), "accent"),          # skirt plates
        box("right_arm", (2, 6, 2), (-4.4, 2.0, -3.4), "glow"),
        box("left_arm", (2, 6, 2), (2.4, 2.0, -3.4), "glow"),
    ],
    "diamond_mech": [
        box("body", (2, 9, 5), (-7.2, -1.0, 2.6), "accent"),           # thruster fin L
        box("body", (2, 9, 5), (5.2, -1.0, 2.6), "accent"),            # thruster fin R
        box("body", (3, 3, 3), (-5.4, 7.0, 3.4), "glow"),              # nozzle L
        box("body", (3, 3, 3), (2.4, 7.0, 3.4), "glow"),               # nozzle R
        box("head", (10, 1, 2), (-5.0, -9.6, -1.0), "glow"),
    ],
    "netherite_mech": [
        box("head", (2, 4, 2), (-5.6, -11.4, -3.0), "accent"),         # horn L
        box("head", (2, 4, 2), (3.6, -11.4, -3.0), "accent"),          # horn R
        box("right_arm", (8, 4, 8), (-4.8, -3.8, -4.0), "plate"),      # massive pauldrons
        box("left_arm", (8, 4, 8), (-3.2, -3.8, -4.0), "plate"),
        box("body", (4, 8, 4), (-6.8, 0.0, 3.0), "dark"),              # exhaust stacks
        box("body", (4, 8, 4), (2.8, 0.0, 3.0), "dark"),
        box("body", (3, 2, 3), (-6.2, -2.4, 3.0), "glow"),
        box("body", (3, 2, 3), (3.2, -2.4, 3.0), "glow"),
        box("right_leg", (6, 4, 6), (-3.7, 4.4, -3.4), "plate"),       # knee cap
        box("left_leg", (6, 4, 6), (-2.3, 4.4, -3.4), "plate"),
        box("right_leg", (2, 6, 2), (-5.4, 2.0, -1.0), "dark"),        # thigh hydraulics
        box("left_leg", (2, 6, 2), (3.4, 2.0, -1.0), "dark"),
    ],
    "spider_mech": [
        box("head", (9, 3, 3), (-4.5, -2.6, -5.6), "dark"),            # fang housing
        box("head", (2, 4, 2), (-3.6, -1.0, -6.0), "accent"),          # fang L
        box("head", (2, 4, 2), (1.6, -1.0, -6.0), "accent"),           # fang R
        box("head", (1, 1, 1), (-3.4, -7.4, -5.4), "eyes"),            # eyes
        box("head", (1, 1, 1), (-1.4, -7.4, -5.4), "eyes"),
        box("head", (1, 1, 1), (0.6, -7.4, -5.4), "eyes"),
        box("head", (1, 1, 1), (2.6, -7.4, -5.4), "eyes"),
        box("body", (11, 8, 6), (-5.5, -1.0, 2.2), "accent"),          # abdomen housing
        box("body", (9, 6, 5), (-4.5, 0.0, 6.0), "dark"),              # abdomen rear
        box("body", (2, 2, 2), (-5.6, 2.0, 10.2), "eyes"),
        box("body", (2, 2, 2), (3.6, 2.0, 10.2), "eyes"),
        box("body", (3, 3, 3), (-6.6, 3.0, 2.6), "dark"),              # octo-arm mount L
        box("body", (3, 3, 3), (3.6, 3.0, 2.6), "dark"),               # octo-arm mount R
        box("right_arm", (5, 5, 5), (-3.4, 8.0, -2.8), "accent"),      # claw gauntlet
        box("left_arm", (5, 5, 5), (-1.6, 8.0, -2.8), "accent"),
        box("right_leg", (2, 6, 2), (-5.4, 1.6, -1.0), "dark"),        # leg hydraulics
        box("left_leg", (2, 6, 2), (3.4, 1.6, -1.0), "dark"),
        box("right_leg", (8, 2, 8), (-4.4, 8.6, -4.4), "accent"),      # wide stance plate
        box("left_leg", (8, 2, 8), (-3.6, 8.6, -4.4), "accent"),
    ],
}

TIER_DEFL = {
    "copper_mech": 0.0,
    "iron_mech": 0.35,
    "gold_mech": -0.15,
    "diamond_mech": 0.55,
    "netherite_mech": 0.85,
    "spider_mech": 0.25,
}


def suit_boxes(tier):
    boxes = [{"part": "hat", "size": (0, 0, 0), "offset": (0.0, 0.0, 0.0), "style": "empty"}]
    boxes += base_boxes(TIER_DEFL[tier])
    boxes += TIER_EXTRA[tier]
    return boxes


# --------------------------------------------------------------------------- #
# uv packing
# --------------------------------------------------------------------------- #

def pack_uvs(boxes):
    x, y, row_h = 0, 0, 0
    for b in boxes:
        w, h, d = b["size"]
        need_w = 2 * (d + w)
        need_h = d + h
        if need_w == 0:
            b["uv"] = (0, 0)
            continue
        if x + need_w > TEX_SIZE:
            x = 0
            y += row_h + 2
            row_h = 0
        b["uv"] = (x, y)
        b["uvsize"] = (need_w, need_h)
        x += need_w + 2
        row_h = max(row_h, need_h)
    bottom = y + row_h
    if bottom > TEX_SIZE:
        raise SystemExit(f"[armor] FATAL: uv layout overflows {TEX_SIZE}px (bottom={bottom})")
    return bottom


# --------------------------------------------------------------------------- #
# painting
# --------------------------------------------------------------------------- #

def style_colors(tier, style):
    t = C.TIERS[tier]
    base = hexcol(t["base"])
    dark = hexcol(t["dark"])
    accent = hexcol(t["accent"])
    glow = hexcol(t["glow"])
    visor = hexcol(t["visor"])
    if style == "plate":
        return base, accent, None
    if style == "dark":
        return darken(base, 0.45), dark, None
    if style == "accent":
        return accent, lighten(accent, 0.3), None
    if style == "glow":
        return darken(base, 0.6), dark, glow
    if style == "visor":
        return visor, lighten(visor, 0.35), None
    if style == "eyes":
        return hexcol("#2A0A08"), hexcol("#1A0505"), visor
    return base, accent, None


def paint_suit(tier, boxes):
    cv = Canvas(TEX_SIZE, TEX_SIZE)
    t = C.TIERS[tier]
    for i, b in enumerate(boxes):
        w, h, d = b["size"]
        if w == 0:
            continue
        u, v = b["uv"]
        base, accent, glow = style_colors(tier, b["style"])
        paint_cube(cv, u, v, w, h, d, base, accent=accent, glow=glow, seed=101 + i * 7,
                   face_detail=b["style"] in ("plate", "accent", "dark"), rivet=(w >= 5 and h >= 5))
        if b["style"] == "visor":
            faces = __import__("pixel").cube_faces(u, v, w, h, d)
            fx, fy, fw, fh = faces["north"]
            for k in range(fh):
                cv.line(fx, fy + k, fx + fw - 1, fy + k, mix(visor_col(t), hexcol("#FFFFFF"), 0.35 * (1 - k / max(fh, 1))))
            cv.frame(fx, fy, fw, fh, hexcol("#101418"))
        if b["style"] == "eyes":
            faces = __import__("pixel").cube_faces(u, v, w, h, d)
            fx, fy, fw, fh = faces["north"]
            cv.rect(fx, fy, fw, fh, hexcol(t["visor"]))
    return cv


def visor_col(t):
    return hexcol(t["visor"])


# --------------------------------------------------------------------------- #
# java emission
# --------------------------------------------------------------------------- #

JAVA_TEMPLATE = """package com.octoarms.client.model;

import java.util.HashMap;
import java.util.Map;

/**
 * GENERATED FILE - do not edit by hand.
 * Produced by tools/gen_armor.py together with the armor textures in
 * assets/{modid}/textures/models/armor/.
 *
 * Every box below maps 1:1 onto pixels of the {tex}x{tex} mech armor texture,
 * which is what keeps the in-game model and the texture perfectly in sync.
 */
public final class ArmorSpecs {{

    /** One cube of a mech suit. */
    public static final class Box {{
        public final String part;
        public final int u;
        public final int v;
        public final float w;
        public final float h;
        public final float d;
        public final float ox;
        public final float oy;
        public final float oz;

        Box(String part, int u, int v, float w, float h, float d, float ox, float oy, float oz) {{
            this.part = part;
            this.u = u;
            this.v = v;
            this.w = w;
            this.h = h;
            this.d = d;
            this.ox = ox;
            this.oy = oy;
            this.oz = oz;
        }}
    }}

    public static final int TEXTURE_SIZE = {tex};

    /** Pivot offsets of the humanoid rig (must match the vanilla player model). */
    public static final Map<String, float[]> PIVOTS = new HashMap<>();

    private static final Map<String, Box[]> SUITS = new HashMap<>();

    static {{
{pivots}
{suits}
    }}

    public static Box[] forTier(String tierId) {{
        Box[] boxes = SUITS.get(tierId);
        return boxes == null ? SUITS.get("{fallback}") : boxes;
    }}

    public static String[] tierIds() {{
        return SUITS.keySet().toArray(new String[0]);
    }}

    private ArmorSpecs() {{
    }}
}}
"""


def emit_java(suits):
    pivots = []
    for name, (x, y, z) in PIVOTS.items():
        pivots.append(f'        PIVOTS.put("{name}", new float[]{{{x}F, {y}F, {z}F}});')

    suit_lines = []
    for tier, boxes in suits.items():
        entries = []
        for b in boxes:
            w, h, d = b["size"]
            ox, oy, oz = b["offset"]
            u, v = b["uv"]
            entries.append(
                f'            new Box("{b["part"]}", {u}, {v}, {w}F, {h}F, {d}F, {ox}F, {oy}F, {oz}F)'
            )
        suit_lines.append(f'        SUITS.put("{tier}", new Box[]{{\n' + ",\n".join(entries) + "\n        });")

    java = JAVA_TEMPLATE.format(
        modid=C.MODID,
        tex=TEX_SIZE,
        fallback="copper_mech",
        pivots="\n".join(pivots),
        suits="\n".join(suit_lines),
    )
    os.makedirs(os.path.dirname(JAVA_OUT), exist_ok=True)
    with open(JAVA_OUT, "w", encoding="utf-8") as fh:
        fh.write(java)
    return JAVA_OUT


def generate():
    os.makedirs(TEX_OUT, exist_ok=True)
    suits = {}
    for tier in C.TIER_ORDER:
        boxes = suit_boxes(tier)
        pack_uvs(boxes)
        suits[tier] = boxes
        cv = paint_suit(tier, boxes)
        for layer in (1, 2):
            cv.save(os.path.join(TEX_OUT, f"{tier}_layer_{layer}.png"))
    path = emit_java(suits)
    total = sum(len(v) for v in suits.values())
    print(f"[armor] {len(suits)} suits / {total} boxes, textures -> {TEX_OUT}")
    print(f"[armor] java spec -> {path}")
    return suits


if __name__ == "__main__":
    generate()
