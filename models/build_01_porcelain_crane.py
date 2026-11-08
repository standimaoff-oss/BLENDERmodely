"""01 — Фарфоровый журавль / Porcelain Crane.

A porcelain figurine crane: glazed white ceramic body, sage-green folded wings
with a gold flight bar, antique-gold bill, kintsugi stitch lines, long thin legs.

Build: python3 models/build_01_porcelain_crane.py
Writes models/01_porcelain_crane.bbmodel + textures/01_porcelain_crane.png
"""
import math
import os
import sys

import numpy as np

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "tools"))
from bb import Model, hx, mix, shade          # noqa: E402
import mats                                   # noqa: E402

ROOT = os.path.join(os.path.dirname(__file__), "..")
NAME = "01_porcelain_crane"

GOLD, GOLD_L, GOLD_D = mats.GOLD, mats.GOLD_L, mats.GOLD_D
POR = mats.POR
EYE = "#22232c"
GREEN = "#9ab5a3"


def por(f, seed=0):
    mats.porcelain(f, seed)


def rotz(v, ang):
    a = math.radians(ang)
    return (v[0] * math.cos(a) - v[1] * math.sin(a),
            v[0] * math.sin(a) + v[1] * math.cos(a), v[2])


m = Model("01 — Фарфоровый журавль", 256, 256, tex=NAME)

body = m.group("body", origin=(0, 24, 0))
legs = m.group("legs", origin=(0, 16, 0))
neck = m.group("neck", origin=(8, 32, 0))
wings = m.group("wings", origin=(0, 27, 0))

# ---------------------------------------------------------------- torso ----
m.cube("belly", (-6.4, 17.9, -5.2), (6.2, 31.2, 5.2), parent=body, base=POR,
       paint=lambda f: por(f, seed=11))
m.cube("back", (-5.8, 31.2, -4.6), (5.6, 32.4, 4.6), parent=body, base=POR,
       paint=lambda f: por(f, seed=12))
m.cube("keel", (-5.4, 16.6, -4.3), (5.2, 17.9, 4.3), parent=body, base=POR,
       paint=lambda f: por(f, seed=13))
m.cube("tail", (-8.8, 20.8, -3.2), (-6.4, 28.4, 3.2), parent=body, base=POR,
       paint=lambda f: por(f, seed=14))
def breast_paint(f):
    por(f, seed=15)
    if f.name in ("south", "north"):
        mats.dash_diag(f, 1, f.h - 1, f.h, GOLD, dx=1, dy=-1, on=1, off=1)


m.cube("breast", (6.2, 19.6, -4.1), (8.0, 29.8, 4.1), parent=body, base=POR,
       paint=breast_paint)





# ---------------------------------------------------------------- legs -----
def build_leg(side):
    c = side * 4.5
    tag = "r" if side > 0 else "l"
    g = m.group("leg_%s" % tag, parent=legs, origin=(-0.8, 16.0, c))
    m.cube("thigh_%s" % tag, (-1.35, 10.4, c - 0.7), (0.1, 17.4, c + 0.7), parent=g,
           base=POR, paint=lambda f: por(f, seed=21))
    m.cube("knee_%s" % tag, (-1.7, 7.6, c - 0.85), (0.45, 10.4, c + 0.85), parent=g,
           base=POR, paint=lambda f: por(f, seed=22))
    m.cube("shin_%s" % tag, (-1.12, 1.0, c - 0.40), (-0.32, 7.6, c + 0.40), parent=g,
           base=POR, paint=lambda f: por(f, seed=23))
    m.cube("ankle_%s" % tag, (-1.9, 0.0, c - 0.6), (-0.1, 0.9, c + 0.6), parent=g,
           base=POR, paint=lambda f: por(f, seed=24))
    for i, (dz, yaw) in enumerate(((-1.2, -17), (0.0, 0), (1.2, 17))):
        m.cube("toe%d_%s" % (i, tag), (0.0, 0.0, c + dz - 0.33), (3.0, 0.55, c + dz + 0.33),
               parent=g, origin=(0.0, 0.28, c + dz), rot=(0, yaw, 0), base=POR,
               paint=lambda f: por(f, seed=30 + i))
    m.cube("toe_back_%s" % tag, (-3.7, 0.0, c - 0.33), (-2.0, 0.55, c + 0.33), parent=g,
           base=POR, paint=lambda f: por(f, seed=34))
    m.cube("hip_%s" % tag, (-2.4, 15.6, c - 1.25), (0.8, 18.6, c + 1.25), parent=legs,
           base=POR, paint=lambda f: (por(f, seed=35),
                                      mats.dash_diag(f, 0, f.h - 1, f.h + 1, GOLD,
                                                     dx=1, dy=-1, on=1, off=1)
                                      if f.name in ("south", "north") else None))


build_leg(1)
build_leg(-1)

# ---------------------------------------------------------------- neck -----
# chunky stepped porcelain column that swells back, then straightens
SEGS = [  # (height, width(x), depth(z), rotation, forward-offset)
    (1.10, 3.70, 3.10, -3, 0.10),
    (1.10, 3.62, 3.02, -5, 0.24),
    (1.05, 3.52, 2.95, -6, 0.30),
    (1.05, 3.42, 2.88, -4, 0.28),
    (1.05, 3.32, 2.82, -1, 0.20),
    (1.00, 3.26, 2.78, 2, 0.08),
    (1.00, 3.22, 2.75, 5, -0.08),
    (0.95, 3.20, 2.72, 7, -0.18),
    (0.90, 3.22, 2.70, 6, -0.20),
]
pos = np.array([7.6, 31.6, 0.0])


def neck_paint(i, f):
    por(f, seed=40 + i)
    if i == 0 and f.name in ("east", "west", "south", "north"):
        mats.gold_band(f, 1, max(2, f.h - 1), seed=i)
    if f.name in ("south", "north") and f.h >= 3:
        bx = 0 if f.name == "south" else f.w - 2
        f.rect(bx, 1, 2, 1, GOLD)


for i, (h, wdt, dep, ang, back) in enumerate(SEGS):
    x0, y0 = float(pos[0]), float(pos[1])
    m.cube("neck_%d" % i, (x0 - wdt / 2, y0, -dep / 2), (x0 + wdt / 2, y0 + h, dep / 2),
           parent=neck, origin=(x0, y0, 0), rot=(0, 0, ang), base=POR,
           paint=lambda f, i=i: neck_paint(i, f), uv_scale=(3, 3))
    pos = pos + np.array(rotz((0, h, 0), ang)) + np.array([back, 0, 0])

# ---------------------------------------------------------------- head -----
hx_, hy_ = float(pos[0]) + 0.3, float(pos[1])
hg = m.group("head", parent=neck, origin=(hx_, hy_, 0))


def skull_paint(f):
    por(f, seed=50)
    if f.name in ("south", "north"):
        w, h = f.w, f.h
        f.rect(w - 6, 3, 3, 3, EYE)
        f.rect(w - 6, 3, 1, 1, "#c3c6d2")


m.cube("skull", (hx_ - 2.7, hy_, -2.6), (hx_ + 2.7, hy_ + 5.2, 2.6), parent=hg,
       base=POR, uv_scale=(2, 2), paint=skull_paint)
m.cube("crown", (hx_ - 2.2, hy_ + 5.2, -2.1), (hx_ + 2.4, hy_ + 6.0, 2.1), parent=hg,
       base=POR, paint=lambda f: por(f, seed=51))
m.cube("throat", (hx_ - 2.5, hy_ - 1.1, -2.3), (hx_ + 2.8, hy_, 2.3), parent=hg,
       base=POR, paint=lambda f: por(f, seed=52))

# ------- gold bill: long, slim, stepped tip -----
m.cube("bill", (hx_ + 2.4, hy_ + 1.7, -0.9), (hx_ + 6.8, hy_ + 2.9, 0.9), parent=hg,
       base=GOLD, paint=lambda f: mats.gold(f, seed=60))
m.cube("bill_tip", (hx_ + 6.8, hy_ + 1.85, -0.65), (hx_ + 9.0, hy_ + 2.75, 0.65),
       parent=hg, base=GOLD, paint=lambda f: mats.gold(f, seed=61))
m.cube("bill_tip2", (hx_ + 9.0, hy_ + 1.95, -0.42), (hx_ + 9.8, hy_ + 2.65, 0.42),
       parent=hg, base=GOLD_L, paint=lambda f: mats.gold(f, seed=62))

# ---------------------------------------------------------------- wings ----
# A folded crane wing: two green plates swept back-and-out, under a serrated
# white covert plate.  The yaw group makes the plates fan away from the flank.
def build_wing(side):
    s = 1 if side > 0 else -1
    tag = "r" if s > 0 else "l"
    pitch = m.group("wing_%s" % tag, parent=wings, origin=(3.0, 28.4, s * 5.4),
                    rot=(0, 0, 12))
    yaw = m.group("wing_yaw_%s" % tag, parent=pitch, origin=(3.0, 28.4, s * 5.4),
                  rot=(0, 33 * s, 0))

    def slab(f, seed, stripe=True):
        mats.wing_green(f, seed=seed)
        if stripe and f.name in ("south", "north", "up", "down"):
            f.rect(f.w - 4, 0, 1, f.h, GOLD_L)
            f.rect(f.w - 3, 0, 1, f.h, GOLD_D)

    zi, zo = sorted((s * 5.4, s * 6.6))
    m.cube("flight_%s" % tag, (-10.6, 20.6, zi), (4.2, 28.4, zo), parent=yaw,
           origin=(-3.2, 24.1, s * 6.0), rot=(-6 * s, 0, 0), base=GREEN,
           paint=lambda f: slab(f, 70), uv_scale=(2, 2))
    z2i, z2o = sorted((s * 5.2, s * 6.4))
    m.cube("flight2_%s" % tag, (-11.8, 19.2, z2i), (2.6, 26.8, z2o), parent=yaw,
           origin=(-4.6, 23.0, s * 5.8), rot=(-6 * s, 0, 0),
           base=mats.mix(GREEN, "#86a392", .35),
           paint=lambda f: slab(f, 72, stripe=False), uv_scale=(2, 2))

    # two smaller shingles trailing the main plate -> scalloped wing edge
    for i, (dx, dy, g) in enumerate(((-1.5, -1.4, 0.45), (-2.7, -2.6, 0.62))):
        a3, b3 = sorted((s * (5.1 - 0.12 * i), s * (6.3 - 0.12 * i)))
        m.cube("flight%d_%s" % (3 + i, tag),
               (-11.9 + dx, 19.4 + dy, a3), (2.2 + dx * 0.4, 26.6 + dy, b3),
               parent=yaw, origin=(-4.6, 23.0, s * 5.8), rot=(-6 * s, 0, 0),
               base=mats.mix(GREEN, "#8ea99a", g),
               paint=lambda f, i=i: slab(f, 74 + i, stripe=False), uv_scale=(2, 2))

    # serrated white covert plate (shingled) peeking over the wing shoulder
    for i in range(7):
        x0 = 3.4 - 0.40 * i
        ya = 29.9 - 0.34 * i
        a, b = sorted((s * 5.26, s * 5.96))
        m.cube("covert%d_%s" % (i, tag), (x0, ya - 0.34, a), (6.2, ya, b), parent=pitch,
               base=POR, paint=lambda f, i=i: por(f, seed=80 + i))


build_wing(1)
build_wing(-1)

# ---------------------------------------------------------------- save -----
for d in ("models", "textures", "previews"):
    os.makedirs(os.path.join(ROOT, d), exist_ok=True)
bb = m.save(os.path.join(ROOT, "models", NAME + ".bbmodel"),
            os.path.join(ROOT, "textures", NAME + ".png"))
print("wrote", bb, "elements:", len(m.elements))
