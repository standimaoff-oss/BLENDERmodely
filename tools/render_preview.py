#!/usr/bin/env python3
"""Offline isometric preview renderer for Octo Arms models (Pillow, no JVM).

Renders the animated octo-limb rig (client/model/OctoArmsModel.java) and every
mech suit's armor boxes (client/model/ArmorSpecs.java) as textured isometric
previews using the REAL generated textures, with the vanilla Minecraft cube
UV unwrap for per-face color sampling. Output: shots/preview/*.png
"""
from __future__ import annotations

import math
import re
from pathlib import Path

from PIL import Image, ImageDraw

RES = Path("src/main/resources/assets/octoarms/textures")
OUT = Path("shots/preview")
OUT.mkdir(parents=True, exist_ok=True)

YAW = math.radians(-35.0)  # camera on the model's front-right
PITCH = math.radians(28.0)
SA, CA = math.sin(YAW), math.cos(YAW)
SP, CP = math.sin(PITCH), math.cos(PITCH)
VIEW = (SA * CP, -SP, CA * CP)  # scene -> camera direction

# ----------------------------------------------------------------- math utils
def ident():
    return [[1.0, 0.0, 0.0], [0.0, 1.0, 0.0], [0.0, 0.0, 1.0]]

def rot_matrix(rx, ry, rz):
    """Minecraft part rotation: R = Rz(rz) * Ry(ry) * Rx(rx)."""
    def mul(a, b):
        return [[sum(a[i][k] * b[k][j] for k in range(3)) for j in range(3)] for i in range(3)]
    m = ident()
    if rx:
        c, s = math.cos(rx), math.sin(rx)
        m = mul(m, [[1, 0, 0], [0, c, -s], [0, s, c]])
    if ry:
        c, s = math.cos(ry), math.sin(ry)
        m = mul(m, [[c, 0, s], [0, 1, 0], [-s, 0, c]])
    if rz:
        c, s = math.cos(rz), math.sin(rz)
        m = mul(m, [[c, -s, 0], [s, c, 0], [0, 0, 1]])
    return m

def apply(m, v):
    return tuple(m[i][0] * v[0] + m[i][1] * v[1] + m[i][2] * v[2] for i in range(3))

def add(a, b):
    return tuple(x + y for x, y in zip(a, b))

def project(x, y, z, scale, cx, cy):
    sx = (x * CA - z * SA) * scale + cx
    sy = ((x * SA + z * CA) * SP + y * CP) * scale + cy
    depth = -(x * SA + z * CA) * CP + y * SP
    return sx, sy, depth

# --------------------------------------------------------------------- faces
# vanilla cube unwrap for a box (w,h,d) at texture origin (u,v):
#   down (u+d, v, w, d) | up (u+d+w, v, w, d)
#   west (u, v+d, d, h) | north (u+d, v+d, w, h) | east (u+d+w, v+d, d, h) | south (u+2d+w, v+d, w, h)
FACE_SHADE = {"up": 1.45, "north": 1.25, "east": 1.05, "west": 0.90, "south": 0.78, "down": 0.60}

def face_uv(u, v, w, h, d, face):
    if face == "down":  return (u + d, v, w, d)
    if face == "up":    return (u + d + w, v, w, d)
    if face == "west":  return (u, v + d, d, h)
    if face == "north": return (u + d, v + d, w, h)
    if face == "east":  return (u + d + w, v + d, d, h)
    return (u + 2 * d + w, v + d, w, h)  # south

class TexSampler:
    def __init__(self, path: Path):
        self.img = Image.open(path).convert("RGB")
        self.cache: dict[tuple, tuple] = {}

    def color(self, rect):
        x, y, w, h = (int(v) for v in rect)
        w, h = max(1, w), max(1, h)
        key = (x, y, w, h)
        if key not in self.cache:
            self.cache[key] = self.img.crop((x, y, x + w, y + h)).resize((1, 1)).getpixel((0, 0))
        return self.cache[key]

def shade(rgb, f):
    return tuple(max(0, min(255, int(c * f))) for c in rgb)

def cuboid_local(ox, oy, oz, w, h, d):
    keys = {}
    for xi, x in (("x0", ox), ("x1", ox + w)):
        for yi, y in (("y0", oy), ("y1", oy + h)):
            for zi, z in (("z0", oz), ("z1", oz + d)):
                keys[xi + yi + zi] = (x, y, z)
    return keys

def make_box(pivot, matrix, ox, oy, oz, w, h, d, uv):
    local = cuboid_local(ox, oy, oz, w, h, d)
    world = {k: add(pivot, apply(matrix, p)) for k, p in local.items()}
    return {"world": world, "uv": (uv[0], uv[1], w, h, d), "m": matrix}

# ------------------------------------------------------------------- painter
def render(all_boxes, tex, path, scale=9.0, bg=(16, 18, 24), grid=True):
    pts = [c for b in all_boxes for c in b["world"].values()]
    mx = (min(p[0] for p in pts) + max(p[0] for p in pts)) / 2
    my = (min(p[1] for p in pts) + max(p[1] for p in pts)) / 2
    mz = (min(p[2] for p in pts) + max(p[2] for p in pts)) / 2
    W, H = 1200, 900
    cx, cy = W / 2, H / 2
    img = Image.new("RGB", (W, H), bg)
    d = ImageDraw.Draw(img)
    if grid:
        gy = max(p[1] for p in pts) - my + 0.5
        for g in range(-16, 17, 2):
            a = project(g - mx, gy, -16 - mz, scale, cx, cy)[:2]
            b = project(g - mx, gy, 16 - mz, scale, cx, cy)[:2]
            d.line([a, b], fill=(30, 34, 44))
            a = project(-16 - mx, gy, g - mz, scale, cx, cy)[:2]
            b = project(16 - mx, gy, g - mz, scale, cx, cy)[:2]
            d.line([a, b], fill=(30, 34, 44))

    faces = []
    for b in all_boxes:
        wld = {k: (v[0] - mx, v[1] - my, v[2] - mz) for k, v in b["world"].items()}
        defs = [
            ("up",    ("x0y0z1", "x1y0z1", "x1y0z0", "x0y0z0")),
            ("north", ("x0y0z0", "x1y0z0", "x1y1z0", "x0y1z0")),
            ("east",  ("x1y0z0", "x1y0z1", "x1y1z1", "x1y1z0")),
            ("south", ("x1y0z1", "x0y0z1", "x0y1z1", "x1y1z1")),
            ("west",  ("x0y0z1", "x0y0z0", "x0y1z0", "x0y1z1")),
            ("down",  ("x0y1z0", "x1y1z0", "x1y1z1", "x0y1z1")),
        ]
        for name, keys in defs:
            a, bb, c = wld[keys[0]], wld[keys[1]], wld[keys[2]]
            e1 = tuple(bb[i] - a[i] for i in range(3))
            e2 = tuple(c[i] - bb[i] for i in range(3))
            n = (e1[1] * e2[2] - e1[2] * e2[1],
                 e1[2] * e2[0] - e1[0] * e2[2],
                 e1[0] * e2[1] - e1[1] * e2[0])
            nl = math.sqrt(sum(v * v for v in n)) or 1.0
            n = tuple(v / nl for v in n)
            if n[0] * VIEW[0] + n[1] * VIEW[1] + n[2] * VIEW[2] <= 0.0:
                continue  # back-facing
            proj = [project(*wld[k], scale, cx, cy) for k in keys]
            depth = sum(p[2] for p in proj) / len(proj)
            rect = face_uv(*b["uv"], name)
            col = shade(tex.color(rect), FACE_SHADE[name])
            faces.append((depth, [p[:2] for p in proj], col))
    faces.sort(key=lambda f: f[0])
    for _, poly, col in faces:
        d.polygon(poly, fill=col, outline=shade(col, 0.5))
    img.save(path)
    print(f"saved {path} ({len(faces)} faces)")

# ------------------------------------------------------------- octo limbs rig
def build_rig():
    boxes = []
    root = (0.0, 0.0, 0.0)
    R0 = ident()

    def add_part(pivot, pmatrix, offset, rot, box, uv):
        m = [[sum(pmatrix[i][k] * rot_matrix(*rot)[k][j] for k in range(3)) for j in range(3)] for i in range(3)]
        p = add(pivot, apply(pmatrix, offset))
        boxes.append(make_box(p, m, *box[:3], *box[3:], uv))
        return p, m

    # backpack spine + shoulder mounts
    add_part(root, R0, (0, 1, 2), (-0.5, 0, 0), (-1.5, -1.5, 0, 3, 3, 8), (0, 46))
    add_part(root, R0, (4.5, 3, 2.5), (0, 0, 0), (-2.5, -2.5, -2, 5, 5, 4), (20, 0))
    add_part(root, R0, (-4.5, 3, 2.5), (0, 0, 0), (-2.5, -2.5, -2, 5, 5, 4), (20, 0))
    # cannons with barrels
    for side in (1, -1):
        p, m = add_part(root, R0, (side * 6.5, 4, 1), (0, 0, 0), (-2, -2, -3, 4, 4, 6), (20, 12))
        add_part(p, m, (0, 0, 0), (0, 0, 0), (-1, -1, -7, 2, 2, 7), (20, 24))
    # thruster + flame + eyes
    p, m = add_part(root, R0, (0, 6, 4), (0, 0, 0), (-2, -2, -2, 4, 4, 4), (40, 0))
    add_part(p, m, (0, 2, 0), (0, 0, 0), (-1.5, 0, -1.5, 3, 5, 3), (40, 10))
    add_part(root, R0, (0, -1, -3.2), (0, 0, 0), (-2, -1, -0.5, 4, 2, 1), (40, 20))
    # eight servo legs: coxa -> femur -> tibia -> claw
    for i in range(8):
        j = i % 4
        side = 1.0 if i < 4 else -1.0
        yaw = side * (0.55 + j * 0.42)
        p0, m0 = add_part(root, R0, (side * 3.2, 2, -2.0 + j * 2.6), (-0.45, yaw, 0),
                          (-1.5, -1.5, 0, 3, 3, 5), (0, 0))
        p1, m1 = add_part(p0, m0, (0, 0, 5), (0.95, 0, 0), (-1, -1, 0, 2, 2, 8), (0, 12))
        p2, m2 = add_part(p1, m1, (0, 0, 8), (-1.35, 0, 0), (-1, -1, 0, 2, 2, 9), (0, 24))
        add_part(p2, m2, (0, 0, 9), (0.6, 0, 0), (-1, -1.5, 0, 2, 3, 2), (0, 38))
    return boxes, RES / "entity/octo_arms.png"

# ------------------------------------------------------------------- armors
def parse_armor_specs():
    src = Path("src/main/java/com/octoarms/client/model/ArmorSpecs.java").read_text()
    pivots = {m.group(1): tuple(float(x) for x in m.group(2, 3, 4))
              for m in re.finditer(r'PIVOTS\.put\("(\w+)", new float\[\]\{([-\d.]+)F, ([-\d.]+)F, ([-\d.]+)F\}\)', src)}
    suits = {}
    for m in re.finditer(r'SUITS\.put\("(\w+)", new Box\[\]\{(.*?)\}\);', src, re.S):
        entries = []
        for b in re.finditer(r'new Box\("(\w+)", (\d+), (\d+), ([-\d.]+)F, ([-\d.]+)F, ([-\d.]+)F, ([-\d.]+)F, ([-\d.]+)F, ([-\d.]+)F\)', m.group(2)):
            entries.append((b.group(1), int(b.group(2)), int(b.group(3)),
                            *(float(b.group(i)) for i in range(4, 10))))
        suits[m.group(1)] = entries
    return pivots, suits

def build_suit(pivots, entries):
    boxes = []
    for part, u, v, w, h, d, ox, oy, oz in entries:
        if w <= 0 or h <= 0 or d <= 0:
            continue
        pivot = pivots.get(part, (0, 24, 0))
        boxes.append(make_box(pivot, ident(), ox, oy, oz, w, h, d, (u, v)))
    return boxes

def main():
    rig_boxes, rig_tex_path = build_rig()
    render(rig_boxes, TexSampler(rig_tex_path), OUT / "spider_rig_iso.png", scale=13.0)

    pivots, suits = parse_armor_specs()
    for name, entries in sorted(suits.items()):
        boxes = build_suit(pivots, entries)
        tex = TexSampler(RES / f"models/armor/{name}_layer_1.png")
        render(boxes, tex, OUT / f"{name}_armor_iso.png", scale=11.0)

if __name__ == "__main__":
    main()
