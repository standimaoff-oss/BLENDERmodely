"""02 — Базальтовый жук-углеед / Basalt Ember Beetle.

A heavy dome of black basalt plates with molten seams glowing between them,
two ember mandibles and six thick stone legs.

Build: python3 models/build_02_basalt_beetle.py
"""
import math
import os
import random
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "tools"))
from bb import Model, hx, mix, shade          # noqa: E402
import mats                                   # noqa: E402

ROOT = os.path.join(os.path.dirname(__file__), "..")
NAME = "02_basalt_beetle"

ROCK = "#241c19"
ROCK_L = "#33292560"[:7]
EMBER = "#ff6b0a"


def rock(f, seed=0, base=ROCK):
    mats.basalt(f, seed, base)


def plate(f, seed=0, hot=0.0):
    """basalt plate: dark stone with a lit bevel edge so each plate reads"""
    rock(f, seed)
    if f.name == "up":
        for y in range(f.h):
            for x in range(f.w):
                c = tuple(int(v) for v in f.a.img[f.y0 + y, f.x0 + x])
                f._set(x, y, shade(c, 1.3))
    elif f.name == "down":
        f.fill(mix(ROCK, "#000000", 0.35))
    # bevel: lighter lip around the plate
    # a hairline lava crack or two across the plate
    r = random.Random(seed * 31 + 7)
    for _ in range(2 if f.w > 4 and f.h > 4 else 0):
        x, y = r.randrange(f.w), r.randrange(f.h)
        for k in range(min(4, f.w - x)):
            f._set(x + k, y + (k % 2), mix(EMBER, ROCK, 0.25))
            f._set(x + k, y + (k % 2) + 1, mix(EMBER, ROCK, 0.62))
    lip = mix(ROCK, "#6b5c53", 0.55 if f.name in ("up", "east", "west") else 0.35)
    if f.w > 2 and f.h > 2:
        for x in range(f.w):
            f._set(x, 0, lip)
        for y in range(f.h):
            f._set(0, y, mix(lip, ROCK, 0.25))
    if hot:
        # glowing crack bleeding onto the plate edge
        for i in range(f.w):
            if (i * 7 + seed) % 11 < 2:
                f._set(i, f.h - 1, mix(EMBER, ROCK, 0.45))
                f._set(i, f.h - 2, mix(EMBER, ROCK, 0.72))


def ember(f, seed=0):
    mats.ember(f, seed)


m = Model("02 — Базальтовый жукед", 256, 256, tex=NAME)
m.name = "02 — Базальтовый жук-углеед"

root = m.group("beetle", origin=(0, 14, 0))
shell = m.group("shell", parent=root, origin=(0, 22, 0))
head = m.group("head", parent=root, origin=(13, 14, 0))
legs = m.group("legs", parent=root, origin=(0, 14, 0))

DY = 3.4
# ---------------------------------------------------------------- core -----
# dark interior; thin molten seams are added on top of it as glowing strips
m.cube("core", (-13.2, 10.6 + DY, -11.2), (9.2, 26.4 + DY, 11.2), parent=shell,
       base=ROCK, paint=lambda f: mats.basalt(f, 3))
m.cube("core_ridge", (-9.8, 26.4 + DY, -4.4), (8.2, 27.4 + DY, 4.4), parent=shell,
       base=ROCK, paint=lambda f: mats.basalt(f, 4))

# ---------------------------------------------------------------- shell ----
# Big basalt plates with thin molten seams glowing in the joints.
PLATES = [
    # (name,          x0,    x1,   y0,    y1,   z0,    z1,  rotX, rotZ)
    ("ridge",       -10.2,  8.4, 26.6, 29.2,  -3.9,  3.9,    0,   0),
    ("top_r",       -10.2,  8.4, 25.4, 28.2,   4.6,  9.2,   -8,   0),
    ("top_l",       -10.2,  8.4, 25.4, 28.2,  -9.2, -4.6,    8,   0),
    ("flank_r",      -9.8,  8.0, 19.4, 26.4,   9.8, 14.0,  -14,   0),
    ("flank_l",      -9.8,  8.0, 19.4, 26.4, -14.0, -9.8,   14,   0),
    ("skirt_r",      -9.0,  7.2, 12.2, 21.0,  14.2, 16.8,  -24,   0),
    ("skirt_l",      -9.0,  7.2, 12.2, 21.0, -16.8, -14.2,   24,   0),
    ("cap_front",     7.8, 12.4, 14.6, 27.6,  -8.8,  8.8,    0, -16),
    ("cap_rear",    -14.6, -9.4, 12.6, 26.2,  -8.4,  8.4,    0,  18),
    ("skirt_front_r", 3.0, 11.0, 11.0, 16.2,   6.8, 14.4,   30,   0),
    ("skirt_front_l", 3.0, 11.0, 11.0, 16.2, -14.4, -6.8,  -30,   0),
    ("belly",        -8.6,  7.4, 10.2, 12.6,  -8.6,  8.6,    0,   0),
    ("chip0",        -5.4,  0.6, 29.0, 30.4,  -2.8,  2.8,    0,   0),
    ("chip1",         2.2,  7.8, 28.2, 29.4,  -3.2,  3.2,    0,   0),
    ("chip2",        -9.6, -4.4, 28.4, 29.6,  -3.2,  3.2,    0,   0),
    ("chip3",        -3.0,  3.4, 22.0, 26.0,  14.2, 17.4,  -18,   0),
    ("chip4",        -3.0,  3.4, 22.0, 26.0, -17.4, -14.2,   18,   0),
]
for i, (nm, x0, x1, y0, y1, z0, z1, rx, rz) in enumerate(PLATES):
    y0, y1 = y0 + DY, y1 + DY
    origin = ((x0 + x1) / 2, (y0 + y1) / 2, (z0 + z1) / 2)
    m.cube(nm, (x0, y0, z0), (x1, y1, z1), parent=shell, origin=origin,
           rot=(rx, 0, rz), base=ROCK, paint=lambda f, i=i: plate(f, i))

# --- molten seams (thin glowing strips sunk between the plates) ------------
SEAMS = [
    # (x0, x1, y0, y1, z0, z1)
    (-10.0, 8.2, 26.0, 28.6, -4.45, -3.75),      # ridge / top_l
    (-10.0, 8.2, 26.0, 28.6, 3.75, 4.45),        # ridge / top_r
    (-9.8, 8.0, 24.8, 27.8, 9.25, 9.95),         # top_r / flank_r
    (-9.8, 8.0, 24.8, 27.8, -9.95, -9.25),       # top_l / flank_l
    (-9.4, 7.6, 18.6, 25.6, 14.05, 14.75),       # flank_r / skirt_r
    (-9.4, 7.6, 18.6, 25.6, -14.75, -14.05),     # flank_l / skirt_l
    (7.9, 8.45, 16.4, 26.2, -5.4, 5.4),          # front cap joint
    (-10.2, -9.4, 13.4, 25.6, -7.0, 7.0),        # rear cap joint
]
for i, (x0, x1, y0, y1, z0, z1) in enumerate(SEAMS):
    m.cube("seam%d" % i, (x0, y0 + DY, z0), (x1, y1 + DY, z1), parent=shell,
           base=EMBER, paint=lambda f, i=i: ember(f, 90 + i))

# bright molten crack that runs over the crown, like a cooled lava fissure
for i, (x0, x1, z0, z1) in enumerate(((1.0, 4.2, 1.2, 2.4), (2.6, 6.0, 2.0, 3.2),
                                      (-3.6, -0.4, -2.0, -0.8))):
    m.cube("fissure%d" % i, (x0, 29.0 + DY, z0), (x1, 29.6 + DY, z1), parent=shell,
           base=EMBER, paint=lambda f, i=i: ember(f, 95 + i))

# ---------------------------------------------------------------- head -----
m.cube("skull", (11.8, 11.0, -4.8), (17.6, 19.0, 4.8), parent=head, base=ROCK,
       paint=lambda f: (mats.basalt(f, 30),
                        f.rect(f.w - 4, 2, 2, 2, EMBER) if f.name in ("south", "north") else None,
                        f.rect(f.w // 2 - 1, 2, 2, 2, EMBER) if f.name == "east" else None))
m.cube("clypeus", (17.6, 11.6, -3.6), (19.4, 15.8, 3.6), parent=head, base=ROCK,
       paint=lambda f: mats.basalt(f, 31))
m.cube("brow", (12.2, 19.0, -4.2), (17.0, 20.2, 4.2), parent=head, base=ROCK,
       paint=lambda f: plate(f, 32))

# ember mandibles: heavy pincer horns rising forward from the head
for side in (1, -1):
    t = "r" if side > 0 else "l"
    za, zb = sorted((side * 3.0, side * 5.6))
    m.cube("mand_base_" + t, (17.0, 11.0, za), (19.4, 13.6, zb), parent=head,
           base=EMBER, paint=lambda f: ember(f, 40))
    m.cube("mand_arm_" + t, (18.0, 12.2, za), (22.4, 14.0, zb), parent=head,
           origin=(18.2, 13.1, side * 4.3), rot=(0, 0, 48), base=EMBER,
           paint=lambda f: ember(f, 41))
    m.cube("mand_tip_" + t, (21.4, 15.4, za + side * 0.1), (24.2, 17.4, zb - side * 0.1),
           parent=head, origin=(21.4, 16.2, side * 4.3), rot=(0, 0, 24), base=EMBER,
           paint=lambda f: ember(f, 42))

# ---------------------------------------------------------------- legs -----
def build_leg(x0, side, i):
    tag = "%d_%s" % (i, "r" if side > 0 else "l")
    g = m.group("leg_" + tag, parent=legs, origin=(x0, 14.0, side * 11.0))
    a, b = sorted((side * 9.6, side * 14.0))
    m.cube("coxa_" + tag, (x0 - 2.4, 13.0, a), (x0 + 2.4, 20.0, b), parent=g,
           origin=(x0, 16.0, side * 11.8), rot=(20 * side, 0, 0), base=ROCK,
           paint=lambda f: plate(f, 50 + i))
    a, b = sorted((side * 13.2, side * 17.2))
    m.cube("femur_" + tag, (x0 - 2.0, 6.4, a), (x0 + 2.0, 15.0, b), parent=g,
           origin=(x0, 14.4, side * 14.4), rot=(20 * side, 0, 0), base=ROCK,
           paint=lambda f: plate(f, 60 + i))
    a, b = sorted((side * 16.0, side * 19.2))
    m.cube("tibia_" + tag, (x0 - 1.7, 0.8, a), (x0 + 1.7, 8.0, b), parent=g,
           origin=(x0, 8.0, side * 17.4), rot=(-9 * side, 0, 0), base=ROCK,
           paint=lambda f: plate(f, 70 + i))
    m.cube("foot_" + tag, (x0 - 2.1, 0.0, a - 0.7), (x0 + 2.1, 1.6, b + 0.9), parent=g,
           base=ROCK, paint=lambda f: plate(f, 80 + i))


for i, x0 in enumerate((7.4, -1.4, -9.6)):
    build_leg(x0, 1, i)
    build_leg(x0, -1, i)

# ---------------------------------------------------------------- save -----
for d in ("models", "textures", "previews"):
    os.makedirs(os.path.join(ROOT, d), exist_ok=True)
bb = m.save(os.path.join(ROOT, "models", NAME + ".bbmodel"),
            os.path.join(ROOT, "textures", NAME + ".png"))
print("wrote", bb, "elements:", len(m.elements))
