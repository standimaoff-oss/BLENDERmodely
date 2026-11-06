"""Generates block textures (16x16) for machines and decorative blocks."""

import os

import content as C
from pixel import Canvas, hexcol, lighten, darken, mix

OUT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources",
                                   "assets", C.MODID, "textures", "block"))


def _panel(cv, base, seed=3, rivets=True, vents=False):
    b = hexcol(base) if isinstance(base, str) else base
    cv.metal(0, 0, 16, 16, b, seed=seed, amp=10)
    cv.frame(0, 0, 16, 16, darken(b, 0.55))
    cv.line(1, 1, 14, 1, lighten(b, 0.4))
    cv.line(1, 1, 1, 14, lighten(b, 0.22))
    cv.line(1, 14, 14, 14, darken(b, 0.5))
    cv.line(14, 1, 14, 14, darken(b, 0.45))
    if rivets:
        for (x, y) in ((2, 2), (13, 2), (2, 13), (13, 13)):
            cv.px(x, y, lighten(b, 0.65))
            cv.px(x, y + 1, darken(b, 0.5))
    if vents:
        for y in (10, 11, 12):
            cv.rect(4, y, 8, 1, darken(b, 0.55))
        cv.line(4, 9, 11, 9, darken(b, 0.3))


def steel_casing(cv):
    _panel(cv, "#9EA7B2", seed=5, rivets=True, vents=True)
    b = hexcol("#9EA7B2")
    cv.frame(3, 3, 10, 10, darken(b, 0.35))
    cv.line(3, 3, 12, 12, darken(b, 0.2))
    cv.line(12, 3, 3, 12, darken(b, 0.2))
    cv.px(7, 7, hexcol("#FFC24A"))
    cv.px(8, 8, hexcol("#FFC24A"))


def coil_block(cv):
    b = hexcol("#B1653A")
    cv.metal(0, 0, 16, 16, hexcol("#7A5230"), seed=9, amp=8)
    for i in range(4):
        y = 2 + i * 4
        cv.rect(1, y, 14, 3, lighten(b, 0.05 * i))
        cv.frame(1, y, 14, 3, darken(b, 0.5))
        cv.line(2, y + 1, 13, y + 1, lighten(b, 0.35))
    cv.px(8, 8, hexcol("#5FE3C8"))


def _machine_common(cv, base, glow, active, kind):
    b = hexcol(base)
    g = hexcol(glow)
    dim = mix(g, hexcol("#1A1D22"), 0.65) if not active else g


def machine_side(cv, base, glow, active):
    _panel(cv, base, seed=13, rivets=True, vents=True)
    b = hexcol(base)
    g = hexcol(glow) if active else mix(hexcol(glow), hexcol("#15181C"), 0.7)
    cv.rect(5, 3, 6, 5, darken(b, 0.4))
    cv.frame(5, 3, 6, 5, darken(b, 0.6))
    cv.px(6, 4, g)
    cv.px(8, 4, g)
    cv.px(10, 4, g)
    cv.rect(6, 5, 4, 2, darken(b, 0.55))
    cv.line(6, 6, 9, 6, g)


def machine_top(cv, base, glow, active):
    _panel(cv, base, seed=17, rivets=True)
    b = hexcol(base)
    g = hexcol(glow) if active else mix(hexcol(glow), hexcol("#15181C"), 0.75)
    cv.circle(8, 8, 5.2, darken(b, 0.35))
    cv.circle(8, 8, 5.2, darken(b, 0.6), filled=False)
    cv.circle(8, 8, 3.4, darken(b, 0.15))
    cv.circle(8, 8, 1.6, g)
    for i in range(4):
        cv.px(8 + (4 if i == 0 else -4 if i == 1 else 0), 8 + (0 if i < 2 else (4 if i == 2 else -4)), g)


def machine_bottom(cv, base, glow, active):
    b = hexcol(base)
    cv.metal(0, 0, 16, 16, darken(b, 0.45), seed=19, amp=7)
    cv.frame(0, 0, 16, 16, darken(b, 0.7))
    for x in (3, 6, 9, 12):
        cv.rect(x, 3, 2, 10, darken(b, 0.3))


def press_front(cv, base, glow, active):
    b = hexcol(base)
    g = hexcol(glow) if active else mix(hexcol(glow), hexcol("#15181C"), 0.7)
    cv.metal(0, 0, 16, 16, b, seed=23, amp=9)
    cv.frame(0, 0, 16, 16, darken(b, 0.6))
    # frame uprights
    cv.rect(1, 1, 3, 14, darken(b, 0.25))
    cv.rect(12, 1, 3, 14, darken(b, 0.25))
    cv.frame(1, 1, 3, 14, darken(b, 0.55))
    cv.frame(12, 1, 3, 14, darken(b, 0.55))
    # press head (piston)
    head_y = 3 if active else 2
    cv.metal(4, head_y, 8, 4, lighten(b, 0.15), seed=29)
    cv.frame(4, head_y, 8, 4, darken(b, 0.6))
    cv.rect(5, head_y + 4, 6, 1, g)
    # anvil bed
    cv.metal(4, 11, 8, 4, darken(b, 0.2), seed=31)
    cv.frame(4, 11, 8, 4, darken(b, 0.6))
    cv.rect(6, 12, 4, 2, darken(b, 0.45))
    if active:
        cv.px(6, 10, g)
        cv.px(9, 10, g)
        cv.px(7, 9, lighten(g, 0.4))
        cv.px(8, 9, lighten(g, 0.4))
    # indicator lamp
    cv.px(2, 2, g)
    cv.px(13, 2, g)


def dock_front(cv, base, glow, active):
    b = hexcol(base)
    g = hexcol(glow) if active else mix(hexcol(glow), hexcol("#15181C"), 0.7)
    cv.metal(0, 0, 16, 16, b, seed=37, amp=9)
    cv.frame(0, 0, 16, 16, darken(b, 0.6))
    # recessed chamber
    cv.rect(3, 3, 10, 8, darken(b, 0.5))
    cv.frame(3, 3, 10, 8, darken(b, 0.7))
    # charging pads
    for i in range(3):
        x = 4 + i * 3
        cv.rect(x, 5, 2, 4, darken(b, 0.35))
        cv.frame(x, 5, 2, 4, darken(b, 0.65))
        cv.px(x, 6, g)
        cv.px(x + 1, 8, g)
    # coil above
    cv.line(4, 4, 11, 4, g)
    # burner hatch below
    cv.rect(4, 12, 8, 3, darken(b, 0.4))
    cv.frame(4, 12, 8, 3, darken(b, 0.65))
    if active:
        cv.rect(6, 13, 4, 1, hexcol("#FF9B3C"))
        cv.px(7, 13, hexcol("#FFE98A"))
    cv.px(2, 2, g)
    cv.px(13, 2, g)


def assembler_front(cv, base, glow, active):
    b = hexcol(base)
    g = hexcol(glow) if active else mix(hexcol(glow), hexcol("#15181C"), 0.7)
    cv.metal(0, 0, 16, 16, b, seed=41, amp=9)
    cv.frame(0, 0, 16, 16, darken(b, 0.6))
    # assembly bay glass
    cv.rect(2, 2, 12, 8, hexcol("#1D2530"))
    cv.frame(2, 2, 12, 8, darken(b, 0.5))
    for i in range(4):
        cv.px(3 + i * 3, 3, mix(hexcol("#1D2530"), hexcol("#7FB2FF"), 0.25))
    # robot arms inside
    cv.line(4, 9, 6, 5, g)
    cv.line(6, 5, 9, 6, g)
    cv.line(12, 9, 10, 5, g)
    cv.line(10, 5, 8, 7, g)
    cv.px(8, 7, lighten(g, 0.6))
    cv.circle(8, 6, 1.4, g)
    # control panel
    cv.rect(2, 11, 12, 4, darken(b, 0.35))
    cv.frame(2, 11, 12, 4, darken(b, 0.6))
    for i, x in enumerate((3, 5, 7, 9, 11)):
        col = g if (active or i % 2 == 0) else mix(g, hexcol("#15181C"), 0.6)
        cv.rect(x, 12, 1, 2, col)
    cv.px(13, 13, hexcol("#FF3B30") if active else hexcol("#5A2020"))


MACHINE_ART = {
    "hydraulic_press": press_front,
    "charging_dock": dock_front,
    "mech_assembler": assembler_front,
}


def generate():
    os.makedirs(OUT, exist_ok=True)
    made = []

    cv = Canvas(16, 16)
    steel_casing(cv)
    made.append(cv.save(os.path.join(OUT, "steel_casing.png")))

    cv = Canvas(16, 16)
    coil_block(cv)
    made.append(cv.save(os.path.join(OUT, "coil_block.png")))

    for name, (_en, _ru, base, glow) in C.MACHINES.items():
        art = MACHINE_ART[name]
        for active in (False, True):
            suffix = "_front_active" if active else "_front"
            cv = Canvas(16, 16)
            art(cv, base, glow, active)
            made.append(cv.save(os.path.join(OUT, f"{name}{suffix}.png")))

            cv = Canvas(16, 16)
            machine_side(cv, base, glow, active)
            made.append(cv.save(os.path.join(OUT, f"{name}_side.png")))

            cv = Canvas(16, 16)
            machine_top(cv, base, glow, active)
            made.append(cv.save(os.path.join(OUT, f"{name}_top.png")))

        cv = Canvas(16, 16)
        machine_bottom(cv, base, glow, False)
        made.append(cv.save(os.path.join(OUT, f"{name}_bottom.png")))

    print(f"[blocks] wrote {len(made)} textures -> {OUT}")
    return made


if __name__ == "__main__":
    generate()
