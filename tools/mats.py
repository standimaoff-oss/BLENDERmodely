"""Shared procedural pixel-art materials for the BLENDERmodely creatures."""
import random

from bb import hx, mix, shade


def _spk(f, base, cols, weights, seed=0, chance=0.55):
    base_c = base if not isinstance(base, str) else hx(base)
    f.fill(base_c)
    r = random.Random(seed)
    pal = [(hx(c) if isinstance(c, str) else c) for c in cols]
    for y in range(f.h):
        for x in range(f.w):
            if r.random() < chance:
                f._set(x, y, r.choices(pal, weights)[0])


# --------------------------------------------------------------- porcelain
POR = "#efece4"
POR_L = "#faf8f3"
POR_D = "#dedad1"


def porcelain(f, seed=0, base=POR, glaze=True):
    # glazed ceramic: almost flat, a whisper of value variation only
    _spk(f, base, [mix(POR, POR_L, 0.45), mix(POR, POR_D, 0.28)], [3, 2], seed,
         chance=0.16)
    if glaze:                                  # soft glaze highlight near the top
        for y in range(f.h):
            k = 1.0 + 0.045 * (1 - y / max(1, f.h - 1)) * 0
            for x in range(f.w):
                c = tuple(int(v) for v in f.a.img[f.y0 + y, f.x0 + x])
                f._set(x, y, shade(c, k))


# -------------------------------------------------------------------- gold
GOLD = "#b79a55"
GOLD_L = "#d6bd7c"
GOLD_D = "#8d7233"


def gold(f, seed=0):
    _spk(f, GOLD, [GOLD_L, GOLD, mix(GOLD, GOLD_D, 0.35), GOLD_D], [1, 3, 2, 1], seed)


def gold_band(f, y0, y1, seed=0):
    """solid band across the whole face (used for neck rings / trims)"""
    r = random.Random(seed)
    for y in range(int(y0), int(y1)):
        for x in range(f.w):
            if r.random() < 0.22:
                f._set(x, y, r.choice([hx(GOLD_L), hx(GOLD)]))
            else:
                f._set(x, y, hx(GOLD_D if (y == int(y0)) else GOLD))


def stitch(f, x0, y0, n=6, col=GOLD, dx=-1, dy=-1, dash=1, gap=1):
    """kintsugi-ish dashed staircase (the crane's porcelain repair stitches)."""
    x, y = x0, y0
    for i in range(n):
        for k in range(dash):
            if (i // 1) % 2 == 0:
                f._set(x + k * 0 + (0 if dy else k), y, col)
        x += dx
        y += dy
        if i % 2 == 0:
            continue


def dash_diag(f, x0, y0, n, col=GOLD, dx=-1, dy=-1, on=1, off=1):
    """n steps of a diagonal, drawing `on` px then skipping `off` px"""
    x, y = x0, y0
    i = 0
    while i < n:
        for _ in range(on):
            f._set(x, y, col)
            x += dx
            y += dy
            i += 1
        for _ in range(off):
            x += dx
            y += dy
            i += 1


# --------------------------------------------------------------- organics
def basalt(f, seed=0, base="#2a211d"):
    _spk(f, base, ["#332a25", "#231b17", "#3d332d", "#1a1310"], [3, 3, 2, 1], seed)


def ember(f, seed=0, base="#ff7a12"):
    _spk(f, base, ["#ffb340", "#ff5a05", "#ffd88a", "#e8480a"], [3, 2, 1, 2], seed)


def wing_green(f, seed=0, base="#9ab5a3"):
    _spk(f, base, ["#a2bdac", "#92b0a0"], [3, 2], seed, chance=0.13)


def ash(f, seed=0, base="#b9b1a7"):
    _spk(f, base, ["#c6beb4", "#aea69c", "#d0c8be", "#9d958b"], [3, 3, 1, 1], seed)
