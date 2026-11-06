"""Generates every 16x16 item texture for Octo Arms."""

import math
import os
import random

import content as C
from pixel import Canvas, hexcol, lighten, darken, mix, with_alpha

OUT = os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources",
                   "assets", C.MODID, "textures", "item")
OUT = os.path.abspath(OUT)


# --------------------------------------------------------------------------- #
# painters
# --------------------------------------------------------------------------- #

def p_plate(cv, base):
    b = hexcol(base)
    cv.metal(2, 2, 12, 12, b, seed=7, amp=12)
    cv.frame(2, 2, 12, 12, darken(b, 0.55))
    cv.line(3, 3, 12, 3, lighten(b, 0.5))
    cv.line(3, 3, 3, 12, lighten(b, 0.32))
    cv.line(12, 4, 12, 12, darken(b, 0.45))
    cv.line(4, 12, 12, 12, darken(b, 0.5))
    # rivets
    for (x, y) in ((4, 4), (11, 4), (4, 11), (11, 11)):
        cv.px(x, y, lighten(b, 0.7))
        cv.px(x, y + 1, darken(b, 0.5))
    # diagonal brushed streaks
    rnd = random.Random(3)
    for i in range(9):
        x = 4 + i
        y = 5 + rnd.randint(0, 6)
        cv.line(x, y, x + 2, y, lighten(b, 0.18))


def p_gear(cv, base):
    b = hexcol(base)
    cx = cy = 8.0
    for i in range(8):
        a = i * math.pi / 4
        x0 = cx + math.cos(a) * 4.2
        y0 = cy + math.sin(a) * 4.2
        x1 = cx + math.cos(a) * 7.2
        y1 = cy + math.sin(a) * 7.2
        cv.line(x0 - 1, y0, x1, y1, darken(b, 0.15))
        cv.line(x0, y0, x1, y1, b)
        cv.line(x0 + 1, y0, x1, y1, darken(b, 0.4))
    cv.circle(cx, cy, 5.2, b)
    cv.circle(cx, cy, 5.2, lighten(b, 0.25), filled=False)
    cv.circle(cx, cy, 4.2, darken(b, 0.35))
    cv.circle(cx - 1.2, cy - 1.2, 1.6, lighten(b, 0.4))
    cv.circle(cx, cy, 2.0, (25, 25, 30, 255))
    cv.circle(cx, cy, 2.0, darken(b, 0.6), filled=False)


def p_actuator(cv, base):
    b = hexcol(base)
    dark = hexcol(C.TIERS["iron_mech"]["dark"])
    # body cylinder
    cv.metal(2, 5, 8, 6, b, seed=11, amp=10)
    cv.frame(2, 5, 8, 6, darken(b, 0.6))
    for x in (4, 6, 8):
        cv.line(x, 6, x, 10, darken(b, 0.35))
    # rod
    cv.rect(10, 7, 4, 2, lighten(b, 0.35))
    cv.frame(10, 7, 4, 2, darken(b, 0.55))
    # end caps
    cv.metal(1, 4, 2, 8, darken(b, 0.2), seed=5)
    cv.px(13, 7, hexcol("#FFE98A"))
    cv.px(13, 8, hexcol("#FF9B3C"))


def p_piston(cv, base):
    b = hexcol(base)
    cv.metal(4, 2, 8, 4, b, seed=13)
    cv.frame(4, 2, 8, 4, darken(b, 0.6))
    cv.rect(7, 6, 2, 5, lighten(b, 0.45))
    cv.frame(7, 6, 2, 5, darken(b, 0.5))
    cv.metal(3, 10, 10, 4, darken(b, 0.15), seed=17)
    cv.frame(3, 10, 10, 4, darken(b, 0.65))
    for x in (4, 6, 8, 10):
        cv.px(x, 11, hexcol("#FFC24A"))
    cv.px(7, 8, hexcol("#8CF0FF"))


def p_circuit(cv, base):
    b = hexcol(base)
    cv.metal(1, 3, 14, 10, b, seed=19, amp=8)
    cv.frame(1, 3, 14, 10, darken(b, 0.55))
    gold = hexcol("#E8C25A")
    # traces
    cv.line(3, 5, 8, 5, gold)
    cv.line(8, 5, 8, 9, gold)
    cv.line(8, 9, 12, 9, gold)
    cv.line(3, 11, 6, 11, gold)
    cv.line(6, 11, 6, 7, gold)
    cv.line(10, 4, 13, 4, gold)
    # pads
    for (x, y) in ((3, 5), (12, 9), (6, 7), (13, 4), (3, 11)):
        cv.px(x, y, lighten(gold, 0.5))
    # chip
    cv.rect(5, 6, 4, 4, hexcol("#22252B"))
    cv.frame(5, 6, 4, 4, hexcol("#4A5058"))
    cv.px(6, 7, hexcol("#8CF0FF"))


def p_core(cv, glow):
    g = hexcol(glow)
    frame = hexcol("#6E7885")
    cv.frame(3, 3, 10, 10, darken(frame, 0.3))
    cv.line(3, 3, 12, 3, lighten(frame, 0.3))
    cv.line(3, 12, 12, 12, darken(frame, 0.5))
    cv.line(3, 3, 3, 12, lighten(frame, 0.15))
    cv.line(12, 3, 12, 12, darken(frame, 0.45))
    cv.glow_blob(8, 8, 3.4, g, seed=5)
    for (x, y) in ((4, 4), (11, 4), (4, 11), (11, 11)):
        cv.px(x, y, lighten(frame, 0.55))


def p_frame(cv, base):
    b = hexcol(base)
    cv.frame(2, 2, 12, 12, darken(b, 0.35))
    cv.rect(2, 2, 12, 1, lighten(b, 0.3))
    cv.rect(2, 13, 12, 1, darken(b, 0.55))
    cv.line(3, 3, 12, 12, b)
    cv.line(12, 3, 3, 12, b)
    cv.line(3, 3, 12, 12, lighten(b, 0.25))
    for (x, y) in ((2, 2), (13, 2), (2, 13), (13, 13), (7, 7)):
        cv.px(x, y, lighten(b, 0.6))
        cv.px(x + 1, y + 1, darken(b, 0.5))


def p_mesh(cv, base):
    b = hexcol(base)
    cv.px(8, 8, lighten(b, 0.6))
    for i in range(8):
        a = i * math.pi / 4
        cv.line(8, 8, 8 + math.cos(a) * 7, 8 + math.sin(a) * 7, with_alpha(b, 200))
    for r in (2.5, 4.5, 6.5):
        pts = [(8 + math.cos(i * math.pi / 8) * r, 8 + math.sin(i * math.pi / 8) * r) for i in range(17)]
        for i in range(16):
            cv.line(pts[i][0], pts[i][1], pts[i + 1][0], pts[i + 1][1], with_alpha(lighten(b, 0.3), 220))
    for i in range(8):
        a = i * math.pi / 4
        cv.px(8 + math.cos(a) * 7, 8 + math.sin(a) * 7, hexcol("#8CF0FF"))


def p_octoarm(cv, base):
    b = hexcol(base)
    glow = hexcol(C.TIERS["spider_mech"]["glow"])
    cv.metal(2, 6, 5, 5, darken(b, 0.2), seed=23)
    cv.frame(2, 6, 5, 5, darken(b, 0.65))
    cv.px(3, 7, glow)
    cv.px(4, 8, glow)
    # three segmented legs
    for (dy, spread) in ((-3, 1), (0, 0), (3, -1)):
        cv.line(7, 8 + dy * 0.4, 10, 6 + dy, b)
        cv.line(10, 6 + dy, 13, 9 + dy * 1.4, lighten(b, 0.2))
        cv.px(13, int(9 + dy * 1.4), glow)
        cv.px(10, int(6 + dy), darken(b, 0.6))


def p_thruster(cv, base):
    b = hexcol(base)
    cv.metal(4, 2, 8, 6, b, seed=29)
    cv.frame(4, 2, 8, 6, darken(b, 0.6))
    cv.tri((3, 8), (13, 8), (8, 14), darken(b, 0.25))
    cv.tri((5, 8), (11, 8), (8, 13), hexcol("#FF9B3C"))
    cv.tri((6, 9), (10, 9), (8, 12), hexcol("#FFE98A"))
    cv.px(8, 4, hexcol("#8CF0FF"))


def p_cell(cv, glow):
    g = hexcol(glow)
    body = hexcol("#5A616B")
    cv.metal(4, 3, 8, 11, body, seed=31)
    cv.frame(4, 3, 8, 11, darken(body, 0.6))
    cv.rect(6, 1, 4, 2, hexcol("#C8CED6"))
    cv.frame(6, 1, 4, 2, darken(body, 0.5))
    for i, y in enumerate((11, 9, 7, 5)):
        col = lighten(g, 0.15 * i) if i < 3 else darken(g, 0.35)
        cv.rect(6, y, 4, 1, col)
    cv.line(5, 4, 5, 13, lighten(body, 0.3))
    cv.px(11, 4, darken(body, 0.5))


def p_coal(cv, base):
    b = hexcol(base)
    rnd = random.Random(37)
    for y in range(3, 13):
        for x in range(3, 13):
            d = math.hypot(x - 8, y - 8)
            if d < 5 + rnd.randint(-1, 1):
                d2 = rnd.randint(-16, 16)
                cv.px(x, y, (max(0, b[0] + d2), max(0, b[1] + d2), max(0, b[2] + d2), 255))
    hot = hexcol("#FF6A3C")
    cv.line(5, 6, 9, 10, hot)
    cv.line(9, 6, 6, 9, hot)
    cv.px(7, 8, hexcol("#FFE98A"))


def p_orb(cv, glow):
    g = hexcol(glow)
    cv.glow_blob(8, 8, 5.0, g, seed=41)
    cv.circle(8, 8, 5.0, lighten(g, 0.4), filled=False)
    cv.px(6, 6, (255, 255, 255, 255))


def p_slug(cv, base):
    b = hexcol(base)
    cv.metal(3, 6, 10, 4, b, seed=43)
    cv.frame(3, 6, 10, 4, darken(b, 0.6))
    cv.tri((13, 6), (13, 10), (15, 8), lighten(b, 0.3))
    cv.line(4, 7, 12, 7, lighten(b, 0.45))
    cv.line(4, 9, 12, 9, darken(b, 0.4))
    cv.px(5, 6, hexcol("#FFC24A"))
    cv.px(8, 6, hexcol("#FFC24A"))


def p_blade(cv, glow):
    g = hexcol(glow)
    hilt = hexcol("#4A5058")
    cv.metal(2, 11, 4, 4, hilt, seed=47)
    cv.frame(2, 11, 4, 4, darken(hilt, 0.6))
    cv.line(4, 12, 6, 10, lighten(hilt, 0.4))
    cv.px(3, 12, g)
    # blade
    for i in range(11):
        x = 5 + i
        y = 10 - i
        cv.line(x, y, x, y - 1, g)
        cv.px(x, y - 2, lighten(g, 0.55))
        cv.px(x, y + 1, darken(g, 0.35))
    cv.px(15, 0, (255, 255, 255, 255))


def p_cannon(cv, base):
    b = hexcol(base)
    glow = hexcol("#8CF0FF")
    cv.metal(2, 6, 6, 5, darken(b, 0.15), seed=53)
    cv.frame(2, 6, 6, 5, darken(b, 0.6))
    cv.metal(8, 7, 6, 3, b, seed=59)
    cv.frame(8, 7, 6, 3, darken(b, 0.55))
    cv.rect(3, 11, 3, 3, darken(b, 0.35))
    cv.px(13, 8, glow)
    cv.px(12, 8, lighten(glow, 0.4))
    cv.px(4, 7, glow)


def p_railgun(cv, base):
    b = hexcol(base)
    glow = hexcol("#B4FF3C")
    cv.metal(1, 7, 12, 3, b, seed=61)
    cv.frame(1, 7, 12, 3, darken(b, 0.6))
    cv.line(2, 7, 12, 7, lighten(b, 0.4))
    cv.line(2, 9, 12, 9, darken(b, 0.45))
    for x in (3, 6, 9):
        cv.rect(x, 6, 1, 5, darken(b, 0.25))
        cv.px(x, 5, glow)
    cv.rect(13, 7, 2, 3, hexcol("#2A2E36"))
    cv.px(14, 8, glow)
    cv.metal(4, 10, 3, 4, darken(b, 0.3), seed=67)
    cv.rect(8, 5, 3, 2, hexcol("#2A2E36"))
    cv.px(9, 5, glow)


PAINTERS = {
    "plate": p_plate, "gear": p_gear, "actuator": p_actuator, "piston": p_piston,
    "circuit": p_circuit, "core": p_core, "frame": p_frame, "mesh": p_mesh,
    "octoarm": p_octoarm, "thruster": p_thruster, "cell": p_cell, "coal": p_coal,
    "orb": p_orb, "slug": p_slug, "blade": p_blade, "cannon": p_cannon,
    "railgun": p_railgun,
}


# --------------------------------------------------------------------------- #
# armor item icons
# --------------------------------------------------------------------------- #

def _armor_base(cv, tier):
    t = C.TIERS[tier]
    return hexcol(t["base"]), hexcol(t["dark"]), hexcol(t["accent"]), hexcol(t["glow"]), hexcol(t["visor"])


def a_helmet(cv, tier):
    b, d, a, g, visor = _armor_base(cv, tier)
    # dome
    for y in range(2, 12):
        half = 6 if y < 9 else 5
        cv.rect(8 - half, y, half * 2, 1, b)
    cv.metal(3, 3, 10, 8, b, seed=71)
    # outline
    cv.line(3, 4, 3, 10, d)
    cv.line(12, 4, 12, 10, d)
    cv.line(4, 2, 11, 2, a)
    # visor slit
    cv.rect(4, 7, 8, 2, visor)
    cv.px(4, 7, lighten(visor, 0.5))
    cv.px(11, 8, darken(visor, 0.4))
    # side pods + crest
    cv.metal(1, 6, 2, 4, darken(b, 0.2), seed=73)
    cv.metal(13, 6, 2, 4, darken(b, 0.2), seed=74)
    cv.rect(7, 1, 2, 2, a)
    cv.px(7, 1, g)
    # neck guard
    cv.rect(4, 11, 8, 2, darken(b, 0.3))
    cv.line(4, 13, 11, 13, d)


def a_chest(cv, tier):
    b, d, a, g, visor = _armor_base(cv, tier)
    cv.metal(4, 4, 8, 10, b, seed=79)
    cv.frame(4, 4, 8, 10, d)
    # pauldrons
    cv.metal(1, 4, 3, 5, lighten(b, 0.12), seed=81)
    cv.frame(1, 4, 3, 5, d)
    cv.metal(12, 4, 3, 5, lighten(b, 0.12), seed=82)
    cv.frame(12, 4, 3, 5, d)
    # chest split + core
    cv.line(8, 5, 8, 13, darken(b, 0.45))
    cv.glow_blob(8, 8, 1.8, g, seed=83)
    # collar
    cv.rect(5, 3, 6, 1, a)
    cv.px(5, 3, d)
    cv.px(10, 3, d)
    # vents
    for y in (10, 11):
        cv.px(5, y, darken(b, 0.55))
        cv.px(6, y, darken(b, 0.55))
        cv.px(10, y, darken(b, 0.55))
        cv.px(9, y, darken(b, 0.55))


def a_leggings(cv, tier):
    b, d, a, g, visor = _armor_base(cv, tier)
    cv.metal(3, 2, 10, 4, b, seed=89)
    cv.frame(3, 2, 10, 4, d)
    cv.rect(7, 2, 2, 4, darken(b, 0.4))
    # legs
    cv.metal(3, 6, 4, 8, b, seed=91)
    cv.frame(3, 6, 4, 8, d)
    cv.metal(9, 6, 4, 8, b, seed=92)
    cv.frame(9, 6, 4, 8, d)
    # knee plates
    cv.rect(3, 9, 4, 2, a)
    cv.rect(9, 9, 4, 2, a)
    cv.px(4, 9, g)
    cv.px(11, 9, g)
    cv.line(4, 6, 4, 13, lighten(b, 0.3))
    cv.line(12, 6, 12, 13, darken(b, 0.35))


def a_boots(cv, tier):
    b, d, a, g, visor = _armor_base(cv, tier)
    for x0 in (2, 9):
        cv.metal(x0, 4, 5, 6, b, seed=97 + x0)
        cv.frame(x0, 4, 5, 6, d)
        cv.metal(x0 - 1 if x0 == 2 else x0, 10, 6, 3, darken(b, 0.15), seed=101 + x0)
        cv.frame(x0 - 1 if x0 == 2 else x0, 10, 6, 3, d)
        cv.rect(x0, 6, 5, 1, a)
        cv.px(x0 + 1, 12, g)
        cv.px(x0 + 3, 12, g)
    cv.line(3, 4, 3, 9, lighten(b, 0.35))
    cv.line(12, 4, 12, 9, darken(b, 0.35))


ARMOR_PAINTERS = {"helmet": a_helmet, "chestplate": a_chest, "leggings": a_leggings, "boots": a_boots}


# --------------------------------------------------------------------------- #
# driver
# --------------------------------------------------------------------------- #

def generate():
    os.makedirs(OUT, exist_ok=True)
    made = []

    for name, (_en, _ru, painter, pal) in C.COMPONENTS.items():
        cv = Canvas(16, 16)
        PAINTERS[painter](cv, pal)
        made.append(cv.save(os.path.join(OUT, f"{name}.png")))

    for name, (_en, _ru, painter, pal) in C.WEAPONS.items():
        cv = Canvas(16, 16)
        PAINTERS[painter](cv, pal)
        made.append(cv.save(os.path.join(OUT, f"{name}.png")))

    for tier in C.TIER_ORDER:
        for piece, _en, _ru in C.PIECES:
            cv = Canvas(16, 16)
            ARMOR_PAINTERS[piece](cv, tier)
            made.append(cv.save(os.path.join(OUT, f"{tier}_{piece}.png")))

    # power cell variants show charge bars tinted by tier glow
    print(f"[items] wrote {len(made)} textures -> {OUT}")
    return made


if __name__ == "__main__":
    generate()
