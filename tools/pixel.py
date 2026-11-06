"""Tiny pixel-art helpers used by the Octo Arms asset generators.

Everything is drawn procedurally so the repository never needs binary art
from outside the tree.  Textures are written with Pillow (RGBA).
"""

import math
import os
import random

from PIL import Image

# --------------------------------------------------------------------------- #
# colour helpers
# --------------------------------------------------------------------------- #


def hexcol(value, alpha=255):
    """'#rrggbb' -> (r, g, b, a)"""
    value = value.lstrip("#")
    return (int(value[0:2], 16), int(value[2:4], 16), int(value[4:6], 16), alpha)


def mix(a, b, t):
    return tuple(int(round(a[i] + (b[i] - a[i]) * t)) for i in range(4))


def lighten(c, t=0.25):
    return mix(c, (255, 255, 255, c[3]), t)


def darken(c, t=0.25):
    return mix(c, (0, 0, 0, c[3]), t)


def with_alpha(c, a):
    return (c[0], c[1], c[2], int(a))


# --------------------------------------------------------------------------- #
# canvas
# --------------------------------------------------------------------------- #


class Canvas:
    def __init__(self, w, h):
        self.w = int(w)
        self.h = int(h)
        self.img = Image.new("RGBA", (self.w, self.h), (0, 0, 0, 0))

    # -- primitives -------------------------------------------------------- #
    def px(self, x, y, c):
        x, y = int(x), int(y)
        if 0 <= x < self.w and 0 <= y < self.h:
            self.img.putpixel((x, y), tuple(int(v) for v in c))

    def rect(self, x, y, w, h, c):
        for j in range(int(h)):
            for i in range(int(w)):
                self.px(x + i, y + j, c)

    def frame(self, x, y, w, h, c):
        for i in range(int(w)):
            self.px(x + i, y, c)
            self.px(x + i, y + h - 1, c)
        for j in range(int(h)):
            self.px(x, y + j, c)
            self.px(x + w - 1, y + j, c)

    def line(self, x0, y0, x1, y1, c):
        steps = int(max(abs(x1 - x0), abs(y1 - y0))) or 1
        for i in range(steps + 1):
            t = i / steps
            self.px(x0 + (x1 - x0) * t, y0 + (y1 - y0) * t, c)

    def circle(self, cx, cy, r, c, filled=True):
        r = float(r)
        for y in range(int(math.floor(cy - r - 1)), int(math.ceil(cy + r + 2))):
            for x in range(int(math.floor(cx - r - 1)), int(math.ceil(cx + r + 2))):
                d = math.hypot(x + 0.5 - cx, y + 0.5 - cy)
                if filled:
                    if d <= r:
                        self.px(x, y, c)
                else:
                    if abs(d - r) <= 0.75:
                        self.px(x, y, c)

    def ellipse(self, cx, cy, rx, ry, c, filled=True):
        for y in range(int(cy - ry - 1), int(cy + ry + 2)):
            for x in range(int(cx - rx - 1), int(cx + rx + 2)):
                d = ((x + 0.5 - cx) / max(rx, 0.01)) ** 2 + ((y + 0.5 - cy) / max(ry, 0.01)) ** 2
                if filled:
                    if d <= 1.0:
                        self.px(x, y, c)
                else:
                    if abs(d - 1.0) <= 0.35:
                        self.px(x, y, c)

    def tri(self, p0, p1, p2, c):
        minx = int(min(p0[0], p1[0], p2[0]))
        maxx = int(max(p0[0], p1[0], p2[0])) + 1
        miny = int(min(p0[1], p1[1], p2[1]))
        maxy = int(max(p0[1], p1[1], p2[1])) + 1

        def sign(a, b, p):
            return (p[0] - b[0]) * (a[1] - b[1]) - (a[0] - b[0]) * (p[1] - b[1])

        for y in range(miny, maxy):
            for x in range(minx, maxx):
                p = (x + 0.5, y + 0.5)
                d1 = sign(p, p0, p1)
                d2 = sign(p, p1, p2)
                d3 = sign(p, p2, p0)
                neg = (d1 < 0) or (d2 < 0) or (d3 < 0)
                pos = (d1 > 0) or (d2 > 0) or (d3 > 0)
                if not (neg and pos):
                    self.px(x, y, c)

    # -- texture effects --------------------------------------------------- #
    def noise(self, x, y, w, h, base, amp=14, seed=1, alpha=255):
        rnd = random.Random(seed)
        for j in range(int(h)):
            for i in range(int(w)):
                d = rnd.randint(-amp, amp)
                col = (
                    max(0, min(255, base[0] + d)),
                    max(0, min(255, base[1] + d)),
                    max(0, min(255, base[2] + d)),
                    alpha,
                )
                self.px(x + i, y + j, col)

    def metal(self, x, y, w, h, base, seed=1, brushed=True, amp=13):
        """Brushed-metal plate: vertical/horizontal grain + edge shading."""
        rnd = random.Random(seed)
        for j in range(int(h)):
            for i in range(int(w)):
                if brushed:
                    d = rnd.randint(-amp, amp)
                else:
                    d = rnd.randint(-amp // 2, amp // 2)
                col = (
                    max(0, min(255, base[0] + d)),
                    max(0, min(255, base[1] + d)),
                    max(0, min(255, base[2] + d)),
                    255,
                )
                self.px(x + i, y + j, col)
        # top highlight / bottom shadow
        for i in range(int(w)):
            self.px(x + i, y, lighten(base, 0.45))
            self.px(x + i, y + 1, lighten(base, 0.22))
            self.px(x + i, y + h - 1, darken(base, 0.55))
            self.px(x + i, y + h - 2, darken(base, 0.30))
        for j in range(int(h)):
            self.px(x, y + j, lighten(base, 0.25))
            self.px(x + w - 1, y + j, darken(base, 0.40))

    def rivets(self, x, y, w, h, col=None, step=4):
        col = col or (235, 235, 235, 255)
        for i in range(step - 1, int(w) - 1, step):
            for j in range(step - 1, int(h) - 1, step):
                self.px(x + i, y + j, col)
                self.px(x + i, y + j + 1, darken(col, 0.5))

    def panel_lines(self, x, y, w, h, col, gap=5):
        for i in range(gap, int(w) - 1, gap):
            for j in range(1, int(h) - 1):
                self.px(x + i, y + j, col)

    def glow_blob(self, cx, cy, r, col, seed=3):
        rnd = random.Random(seed)
        for y in range(int(cy - r - 1), int(cy + r + 2)):
            for x in range(int(cx - r - 1), int(cx + r + 2)):
                d = math.hypot(x + 0.5 - cx, y + 0.5 - cy)
                if d <= r:
                    t = 1.0 - (d / max(r, 0.001))
                    c = mix(col, (255, 255, 255, 255), t * 0.75)
                    jitter = rnd.randint(-8, 8)
                    self.px(x, y, (
                        max(0, min(255, c[0] + jitter)),
                        max(0, min(255, c[1] + jitter)),
                        max(0, min(255, c[2] + jitter)),
                        255,
                    ))

    # -- io ---------------------------------------------------------------- #
    def save(self, path):
        os.makedirs(os.path.dirname(path), exist_ok=True)
        self.img.save(path, "PNG")
        return path


# --------------------------------------------------------------------------- #
# minecraft cube UV helpers
# --------------------------------------------------------------------------- #


def cube_faces(u, v, w, h, d):
    """Return the six UV rectangles of a minecraft box (texOffs layout)."""
    return {
        "up": (u + d, v, w, d),
        "down": (u + d + w, v, w, d),
        "west": (u, v + d, d, h),
        "north": (u + d, v + d, w, h),
        "east": (u + d + w, v + d, d, h),
        "south": (u + 2 * d + w, v + d, w, h),
    }


def paint_cube(canvas, u, v, w, h, d, base, accent=None, glow=None, seed=1,
               face_detail=True, rivet=False):
    """Paint a shaded cube into the canvas using minecraft UV layout."""
    accent = accent or lighten(base, 0.35)
    faces = cube_faces(u, v, w, h, d)
    shades = {
        "up": lighten(base, 0.40),
        "down": darken(base, 0.55),
        "north": lighten(base, 0.10),
        "south": darken(base, 0.18),
        "west": darken(base, 0.05),
        "east": darken(base, 0.30),
    }
    for name, (fx, fy, fw, fh) in faces.items():
        if fw <= 0 or fh <= 0:
            continue
        canvas.metal(fx, fy, fw, fh, shades[name], seed=seed + hash(name) % 97, amp=11)
        canvas.frame(fx, fy, fw, fh, darken(base, 0.62))
        if face_detail and name in ("north", "south") and fw >= 5 and fh >= 5:
            canvas.rect(fx + 1, fy + 1, fw - 2, 1, lighten(base, 0.30))
            if fw >= 7 and fh >= 7:
                canvas.frame(fx + 2, fy + 2, fw - 4, fh - 4, darken(base, 0.35))
        if rivet and fw >= 4 and fh >= 4:
            canvas.px(fx + 1, fy + 1, lighten(base, 0.75))
            canvas.px(fx + fw - 2, fy + 1, lighten(base, 0.6))
            canvas.px(fx + 1, fy + fh - 2, darken(base, 0.7))
            canvas.px(fx + fw - 2, fy + fh - 2, darken(base, 0.7))
        if glow is not None and name in ("north",) and fw >= 3 and fh >= 3:
            canvas.rect(fx + max(1, fw // 2 - 1), fy + max(1, fh // 2 - 1),
                        min(2, fw - 2), min(2, fh - 2), glow)
