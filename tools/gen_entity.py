"""Texture for the animated Octo Arms rig (8 servo legs, cannons, thrusters).

The UV rectangles painted here are the exact same numbers hard-coded in
`OctoArmsModel.java`, so the model and the skin always line up.
"""

import os

import content as C
from pixel import Canvas, hexcol, lighten, darken, paint_cube

OUT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "src", "main", "resources",
                                   "assets", C.MODID, "textures", "entity"))

TEX = 64

# name: (u, v, w, h, d)
PARTS = {
    "leg_coxa": (0, 0, 3, 3, 5),
    "leg_femur": (0, 12, 2, 2, 8),
    "leg_tibia": (0, 24, 2, 2, 9),
    "leg_claw": (0, 38, 2, 3, 2),
    "arm_mount": (20, 0, 5, 5, 4),
    "cannon_body": (20, 12, 4, 4, 6),
    "cannon_barrel": (20, 24, 2, 2, 7),
    "thruster": (40, 0, 4, 4, 4),
    "flame": (40, 10, 3, 5, 3),
    "eye_cluster": (40, 20, 4, 2, 1),
    "back_spine": (0, 46, 3, 3, 8),
}


def generate():
    os.makedirs(OUT, exist_ok=True)
    cv = Canvas(TEX, TEX)

    steel = hexcol("#4A525E")
    dark = hexcol("#252A32")
    accent = hexcol("#93A0B2")
    glow = hexcol(C.TIERS["spider_mech"]["glow"])
    hot = hexcol("#FF9B3C")

    for name, (u, v, w, h, d) in PARTS.items():
        if name in ("leg_claw", "eye_cluster"):
            base, acc, gl = dark, accent, glow
        elif name == "flame":
            base, acc, gl = hot, hexcol("#FFE98A"), None
        elif name.startswith("cannon"):
            base, acc, gl = dark, steel, glow
        elif name == "arm_mount" or name == "back_spine":
            base, acc, gl = steel, accent, glow
        else:
            base, acc, gl = steel, accent, None
        paint_cube(cv, u, v, w, h, d, base, accent=acc, glow=gl, seed=abs(hash(name)) % 500,
                   face_detail=True, rivet=False)

    # claws get a bright tip
    u, v, w, h, d = PARTS["leg_claw"]
    cv.rect(u + d, v + d, w, 1, glow)

    # eye cluster glows red
    u, v, w, h, d = PARTS["eye_cluster"]
    cv.rect(u, v, 2 * (w + d), d + h, hexcol("#FF3B30"))
    for x in range(0, 2 * (w + d), 2):
        cv.px(u + x, v, hexcol("#FFD0CC"))

    cv.save(os.path.join(OUT, "octo_arms.png"))
    print(f"[entity] octo_arms.png -> {OUT}")
    return PARTS


if __name__ == "__main__":
    generate()
