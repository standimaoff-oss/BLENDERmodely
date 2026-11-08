"""03 — Пепельная моль / Ash Moth.

Grey dust-winged moth with ember wingtips, eyespots on the hindwings,
furry body and kinked antennae.

Build: python3 models/build_03_ash_moth.py
"""
import math
import os
import random
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "tools"))
from bb import Model, hx, mix, shade          # noqa: E402
import mats                                   # noqa: E402

ROOT = os.path.join(os.path.dirname(__file__), "..")
NAME = "03_ash_moth"

ASH = "#b6aea4"
ASH_L = "#cdc6bc"
ASH_D = "#8e857b"
EMBER = "#e2560e"
EMBER_H = "#ff9c2a"
DARK = "#241d17"


def wing(f, seed=0, base=ASH):
    """dusty grey wing membrane: gentle mottle, pale leading edge, dark rim"""
    b = hx(base)
    pale = mix(b, "#e8e1d6", 0.45)
    dark = mix(b, ASH_D, 0.45)
    mats._spk(f, b, [pale, dark, mix(pale, b, 0.5), mix(dark, b, 0.5)],
              [3, 2, 2, 2], seed, chance=0.34)
    # pale leading edge along the body side, dark rim on the outer edge
    for y in range(f.h):
        f._set(0, y, mix(pale, b, 0.15))
        f._set(f.w - 1, y, mix(dark, b, 0.25))
    for x in range(f.w):
        f._set(x, f.h - 1, mix(dark, b, 0.35))


def ember_wing(f, seed=0, base=EMBER):
    base_c = hx(base) if isinstance(base, str) else base
    f.fill(base_c)
    r = random.Random(seed)
    for y in range(f.h):
        for x in range(f.w):
            v = r.random()
            if v < 0.30:
                f._set(x, y, mix(EMBER, EMBER_H, 0.65))
            elif v < 0.55:
                f._set(x, y, mix(EMBER, "#a83406", 0.5))
            elif v < 0.66:
                f._set(x, y, mix(EMBER_H, "#ffd489", 0.5))


def body_fur(f, seed=0, base=ASH):
    mats.ash(f, seed, base)
    r = random.Random(seed + 11)
    for y in range(f.h):
        for x in range(f.w):
            if r.random() < 0.30:
                f._set(x, y, mix(ASH_D, (0, 0, 0), r.random() * 0.3))


m = Model("03 — Пепельная моль", 256, 256, tex=NAME)

body = m.group("body", origin=(0, 11, 0))
head = m.group("head", parent=body, origin=(6, 11, 0))
wings = m.group("wings", origin=(0, 12, 0))

# ---------------------------------------------------------------- torso ----
m.cube("thorax", (-4.0, 8.0, -3.8), (4.0, 15.2, 3.8), parent=body, base=ASH,
       paint=lambda f: body_fur(f, 10))
m.cube("collar", (-1.8, 14.8, -3.4), (3.0, 16.4, 3.4), parent=body, base=ASH_L,
       paint=lambda f: body_fur(f, 11, ASH_L))
m.cube("waist", (-5.4, 7.6, -2.8), (-3.2, 13.4, 2.8), parent=body, base=ASH,
       paint=lambda f: body_fur(f, 12))

# segmented abdomen, tapering back and down
for i in range(4):
    x1 = -5.2 - i * 2.1
    x0 = x1 - 2.3
    y1 = 12.6 - i * 1.15
    y0 = y1 - 5.0 + i * 0.35
    w = 2.7 - i * 0.42
    m.cube("abdo%d" % i, (x0, y0, -w), (x1, y1, w), parent=body, base=ASH,
           paint=lambda f, i=i: body_fur(f, 20 + i))

# ---------------------------------------------------------------- head -----
def face(f):
    body_fur(f, 30, ASH_L if f.name in ("east", "up") else ASH)
    if f.name == "east":                       # two big dark eyes on the face
        f.rect(1, 2, 2, 3, DARK)
        f.rect(f.w - 3, 2, 2, 3, DARK)
        f.rect(1, 2, 1, 1, mix(DARK, "#ffffff", 0.30))
        f.rect(f.w - 3, 2, 1, 1, mix(DARK, "#ffffff", 0.30))
        f.rect(f.w // 2 - 1, f.h - 3, 2, 2, mix(ASH_D, DARK, 0.35))
    elif f.name in ("south", "north"):
        f.rect(f.w - 3, 2, 2, 3, DARK)


m.cube("skull", (4.2, 10.0, -3.0), (8.2, 14.4, 3.0), parent=head, base=ASH,
       uv_scale=(2, 2), paint=face)
m.cube("brow", (4.6, 14.4, -2.4), (7.8, 15.4, 2.4), parent=head, base=ASH_L,
       paint=lambda f: body_fur(f, 31, ASH_L))

# antennae: two slender hinged rods sweeping up and forward
for side in (1, -1):
    t = "r" if side > 0 else "l"
    za, zb = sorted((side * 1.0, side * 2.0))
    m.cube("ant_base_" + t, (6.0, 15.0, za), (6.9, 18.2, zb), parent=head, base=ASH_D,
           origin=(6.45, 15.2, side * 1.5), rot=(0, 0, 26),
           paint=lambda f: body_fur(f, 40, ASH_D))
    m.cube("ant_tip_" + t, (6.5, 17.6, za), (7.4, 21.0, zb), parent=head, base=ASH_D,
           origin=(6.95, 17.9, side * 1.5), rot=(0, 0, -10),
           paint=lambda f: body_fur(f, 41, ASH_D))

# small legs tucked under the thorax
for side in (1, -1):
    t = "r" if side > 0 else "l"
    for i, x0 in enumerate((2.2, -1.0)):
        za, zb = sorted((side * 1.2, side * 2.4))
        m.cube("leg%d_%s" % (i, t), (x0, 0.4, za), (x0 + 1.2, 8.6, zb), parent=body,
               base=ASH_D, origin=(x0 + 0.6, 8.4, side * 1.8), rot=(10 * side, 0, 0),
               paint=lambda f: body_fur(f, 50 + i, ASH_D))

# ---------------------------------------------------------------- wings ----
# Flat cards facing the viewer, radiating from the thorax: forewings sweep
# up-and-out, hindwings down-and-out (spread moth pose).
def build_wings(side):
    s = 1 if side > 0 else -1
    tag = "r" if s > 0 else "l"
    zr = 3.2                                  # wing root
    cy = 13.4                                 # forewing chord centre

    fg = m.group("fore_%s" % tag, parent=wings, origin=(0.6, cy, s * zr),
                 rot=(-47 * s, 0, 0))
    FSEG = ((0.0, 4.8, 6.4, 5.6), (4.6, 8.6, 5.6, 5.6), (8.4, 11.8, 4.8, 5.6))
    for i, (z0, z1, c0, c1) in enumerate(FSEG):
        a, b = sorted((s * (zr + z0), s * (zr + z1)))
        m.cube("fore%d_%s" % (i, tag), (-2.4, cy - c0, a), (-1.0, cy + c1, b),
               parent=fg, base=ASH, paint=lambda f, i=i: wing(f, 60 + i))
    a, b = sorted((s * (zr + 11.6), s * (zr + 16.4)))
    m.cube("foretip_%s" % tag, (-2.4, cy - 4.0, a), (-1.0, cy + 5.6, b), parent=fg,
           base=EMBER, paint=lambda f: ember_wing(f, 61))
    a, b = sorted((s * (zr + 10.4), s * (zr + 11.8)))
    m.cube("foreheat_%s" % tag, (-2.4, cy - 4.2, a), (-1.0, cy + 5.6, b), parent=fg,
           base=EMBER, paint=lambda f: ember_wing(f, 62, mix(EMBER, ASH, 0.5)))

    hy = 12.2
    hg = m.group("hind_%s" % tag, parent=wings, origin=(-2.4, hy, s * zr),
                 rot=(24 * s, 0, 0))
    HSEG = ((0.0, 5.2, 6.6, 6.0), (5.0, 9.8, 5.8, 6.0), (9.6, 13.2, 5.0, 6.0))

    def eyespot(f):
        wing(f, 81)
        cx, cyy = f.w // 2, f.h // 2
        f.rect(cx - 3, cyy - 2, 5, 4, DARK)
        f.rect(cx - 2, cyy - 1, 3, 2, mix(DARK, ASH, 0.32))
        f.rect(cx - 2, cyy - 1, 1, 1, mix(DARK, "#ffffff", 0.32))

    for i, (z0, z1, c0, c1) in enumerate(HSEG):
        a, b = sorted((s * (zr + z0), s * (zr + z1)))
        paint = eyespot if i == 1 else (lambda f, i=i: wing(f, 80 + i))
        m.cube("hind%d_%s" % (i, tag), (-2.4, hy - c0, a), (-1.0, hy + c1, b),
               parent=hg, base=ASH, paint=paint)


build_wings(1)
build_wings(-1)

# ---------------------------------------------------------------- save -----
for d in ("models", "textures", "previews"):
    os.makedirs(os.path.join(ROOT, d), exist_ok=True)
bb = m.save(os.path.join(ROOT, "models", NAME + ".bbmodel"),
            os.path.join(ROOT, "textures", NAME + ".png"))
print("wrote", bb, "elements:", len(m.elements))
