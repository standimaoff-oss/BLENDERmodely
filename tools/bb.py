"""Tiny Blockbench .bbmodel authoring kit.

Writes native Blockbench 4.x project files (model_format "free") with a single
embedded texture atlas, plus a side-car PNG.

UV convention is taken 1:1 from Blockbench's own source
(js/util/three_custom.js -> setShape, js/outliner/types/cube.js -> updateUV):
every cube uses the classic box-unwrap layout, stored as explicit per-face UV
rectangles so the atlas can carry hand-painted decals.
"""
import base64
import json
import random
import struct
import uuid as _uuid
import zlib

import numpy as np

from pngio import write_png

FACES = ("east", "west", "up", "down", "south", "north")

# face -> (corner_xyz_name, u_axis, v_axis)  (Blockbench / Minecraft convention)
FACE_AXES = {
    "east":  ((1, 1, 1), (0, 0, -1), (0, -1, 0)),
    "west":  ((0, 1, 0), (0, 0, 1), (0, -1, 0)),
    "north": ((1, 1, 0), (-1, 0, 0), (0, -1, 0)),
    "south": ((0, 1, 1), (1, 0, 0), (0, -1, 0)),
    "up":    ((0, 1, 0), (1, 0, 0), (0, 0, 1)),
    "down":  ((0, 0, 1), (1, 0, 0), (0, 0, -1)),
}


def hx(c):
    c = c.lstrip("#")
    if len(c) == 3:
        c = "".join(ch * 2 for ch in c)
    return (int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16))


def mix(a, b, t):
    a, b = hx(a) if isinstance(a, str) else a, hx(b) if isinstance(b, str) else b
    return tuple(int(round(a[i] + (b[i] - a[i]) * t)) for i in range(3))


def shade(c, k):
    """multiply brightness"""
    c = hx(c) if isinstance(c, str) else c
    return tuple(max(0, min(255, int(round(v * k)))) for v in c)


class Atlas:
    def __init__(self, w=256, h=256, bg="#ff00ff"):
        self.w, self.h = w, h
        self.img = np.zeros((h, w, 3), np.uint8)
        self.img[:] = hx(bg)
        self.rects = []
        self._x = 1
        self._y = 1
        self._row = 0
        self.pad = 1

    def alloc(self, w, h):
        p = self.pad
        if self._x + w + p + 1 > self.w:
            self._x = 1
            self._y += self._row + 2 * p + 1
            self._row = 0
        if self._y + h + p + 1 > self.h:
            raise RuntimeError("atlas full (%d x %d)" % (self.w, self.h))
        r = (self._x + p, self._y + p, w, h)
        self._x += w + 2 * p + 1
        self._row = max(self._row, h)
        self.rects.append(r)
        return r

    def bleed(self):
        """extend each rect edge 1px into its gutter (kills seams everywhere)."""
        img = self.img
        for (x, y, w, h) in self.rects:
            img[y - 1, x:x + w] = img[y, x:x + w]
            img[y + h, x:x + w] = img[y + h - 1, x:x + w]
            img[y:y + h, x - 1] = img[y:y + h, x]
            img[y:y + h, x + w] = img[y:y + h, x + w - 1]
        return img

    def png_bytes(self):
        import io
        buf = io.BytesIO()
        # in-memory png via write_png to a temp path is avoided: encode directly
        img = self.bleed()
        h, w, c = img.shape
        raw = bytearray()
        for row in img:
            raw.append(0)
            raw += row.tobytes()

        def chunk(tag, data):
            return (struct.pack(">I", len(data)) + tag + data +
                    struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF))
        out = b"\x89PNG\r\n\x1a\n"
        out += chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 2, 0, 0, 0))
        out += chunk(b"IDAT", zlib.compress(bytes(raw), 9))
        out += chunk(b"IEND", b"")
        return out


class Face:
    """Painter for one face of one cube. Local coords: x right, y DOWN."""

    def __init__(self, atlas, rect, name, rng, size):
        self.a = atlas
        self.x0, self.y0, self.w, self.h = rect
        self.name = name
        self.rng = rng
        self.size = size  # (dx,dy,dz)

    # -- low level -------------------------------------------------------
    def _set(self, x, y, c):
        if 0 <= x < self.w and 0 <= y < self.h:
            self.a.img[self.y0 + y, self.x0 + x] = c if not isinstance(c, str) else hx(c)

    def get(self, x, y):
        return tuple(int(v) for v in self.a.img[self.y0 + y, self.x0 + x])

    def fill(self, c):
        if callable(c):
            for y in range(self.h):
                for x in range(self.w):
                    self._set(x, y, c(x, y))
        else:
            self.a.img[self.y0:self.y0 + self.h, self.x0:self.x0 + self.w] = hx(c) if isinstance(c, str) else c
        return self

    def px(self, x, y, c, w=1, h=1):
        for j in range(h):
            for i in range(w):
                self._set(x + i, y + j, c)
        return self

    def rect(self, x, y, w, h, c):
        for j in range(h):
            for i in range(w):
                self._set(x + i, y + j, c)
        return self

    def frame(self, c, t=1):
        for i in range(self.w):
            for k in range(t):
                self._set(i, k, c); self._set(i, self.h - 1 - k, c)
        for j in range(self.h):
            for k in range(t):
                self._set(k, j, c); self._set(self.w - 1 - k, j, c)
        return self

    # -- texture helpers -------------------------------------------------
    def noise(self, cols, weights=None, seed=None):
        r = random.Random(seed)
        cols = [hx(c) if isinstance(c, str) else c for c in cols]
        for y in range(self.h):
            for x in range(self.w):
                self._set(x, y, r.choices(cols, weights)[0])
        return self

    def grain(self, base, cols, chance=0.35, seed=None):
        """speckle cols over a solid base"""
        r = random.Random(seed)
        base = hx(base) if isinstance(base, str) else base
        cols = [hx(c) if isinstance(c, str) else c for c in cols]
        self.fill(base)
        for y in range(self.h):
            for x in range(self.w):
                if r.random() < chance:
                    self._set(x, y, r.choice(cols))
        return self

    def vgrad(self, c0, c1, y0=0, y1=None):
        y1 = self.h if y1 is None else y1
        c0 = hx(c0) if isinstance(c0, str) else c0
        c1 = hx(c1) if isinstance(c1, str) else c1
        for y in range(y0, y1):
            t = 0 if y1 - y0 <= 1 else (y - y0) / (y1 - y0 - 1)
            c = tuple(int(round(c0[i] + (c1[i] - c0[i]) * t)) for i in range(3))
            for x in range(self.w):
                self._set(x, y, c)
        return self

    def band(self, fy0, fy1, c, offset=0):
        """horizontal band given in pixels (float ok)"""
        for y in range(int(fy0), int(fy1) + (1 if fy1 % 1 else 0)):
            for x in range(self.w):
                self._set(x, y, c)
        return self

    def art(self, rows, pal, ox=0, oy=0, sx=1, sy=1):
        """ascii-art decal: pal maps char -> color, '.'/' ' = transparent"""
        for j, row in enumerate(rows):
            for i, ch in enumerate(row):
                if ch in (".", " "):
                    continue
                c = pal.get(ch)
                if c is None:
                    continue
                self.rect(ox + i * sx, oy + j * sy, sx, sy, c)
        return self


class Cube:
    def __init__(self, el, group, face_ctx):
        self.el = el
        self.group = group
        self.ctx = face_ctx


class Model:
    def __init__(self, name, atlas_w=256, atlas_h=256, tex="texture"):
        self.name = name
        self.atlas = Atlas(atlas_w, atlas_h)
        self.elements = []
        self.outliner = []
        self.tex_name = tex
        self._seed = 0

    # -- structure -------------------------------------------------------
    def group(self, name, parent=None, origin=(0, 0, 0), rot=None, color=0, open=True):
        g = {
            "name": name, "origin": list(origin), "rotation": list(rot or (0, 0, 0)),
            "color": color, "uuid": str(_uuid.uuid4()), "export": True,
            "mirror_uv": False, "isOpen": open, "locked": False,
            "visibility": True, "autouv": 0, "children": [],
        }
        (self.outliner if parent is None else parent["children"]).append(g)
        return g

    def cube(self, name, frm, to, parent=None, origin=None, rot=None,
             inflate=None, paint=None, base="#ffffff", mirror=False,
             seed=None, uv_scale=None):
        """paint: dict{face: fn(Face)} or fn(Face) for every face, or None"""
        frm = [float(v) for v in frm]
        to = [float(v) for v in to]
        dx, dy, dz = (absto := [to[i] - frm[i] for i in range(3)])
        if dx < 0 or dy < 0 or dz < 0:
            raise ValueError("cube %s has negative size" % name)
        size = (dx, dy, dz)
        if uv_scale is None:
            uv_scale = (1, 1)
        # per-face pixel size (1 model unit = uv_scale px)
        sdx = max(1, int(round(dx * uv_scale[0])))
        sdy = max(1, int(round(dy * uv_scale[1])))
        sdz = max(1, int(round(dz * uv_scale[0])))
        g = 1                                   # gutter so faces never touch
        W = 4 * g + 2 * sdz + 2 * sdx
        H = 2 * g + sdz + sdy
        col0, col1, col2, col3 = 0, g + sdz, 2 * g + sdz + sdx, 3 * g + 2 * sdz + sdx
        rowA, rowB = 0, g + sdz
        rx, ry, _, _ = self.atlas.alloc(W, H)

        if rot is not None:
            nz = [v for v in rot if abs(v) > 1e-9]
            if len(nz) > 1:
                raise ValueError("cube %s: multi-axis rotation forbidden" % name)

        rng = random.Random(seed if seed is not None else abs(hash(name)) % 99999)
        faces = {}
        lay = {
            "east":  (col0, rowB, sdz, sdy),
            "west":  (col2, rowB, sdz, sdy),
            "up":    (col1, rowA, sdx, sdz),
            "down":  (col2, rowA, sdx, sdz),
            "south": (col3, rowB, sdx, sdy),
            "north": (col1, rowB, sdx, sdy),
        }
        for fname in FACES:
            lx, ly, lw, lh = lay[fname]
            fx, fy = rx + lx, ry + ly
            f = Face(self.atlas, (fx, fy, lw, lh), fname, rng, size)
            f.fill(base)
            fn = None
            if callable(paint):
                fn = paint
            elif isinstance(paint, dict):
                fn = paint.get(fname)
            if fn:
                fn(f)
            faces[fname] = {"uv": [fx, fy, fx + lw, fy + lh], "texture": 0}

        el = {
            "name": name, "box_uv": False, "rescale": False, "locked": False,
            "light_emission": 0, "render_order": "default",
            "allow_mirror_modeling": True,
            "from": frm, "to": to, "autouv": 0, "color": 0,
            "origin": list(origin if origin is not None else [0, 0, 0]),
            "rotation": list(rot or (0, 0, 0)),
            "faces": faces, "type": "cube", "uuid": str(_uuid.uuid4()),
        }
        if inflate:
            el["inflate"] = float(inflate)
        self.elements.append(el)
        (self.outliner if parent is None else parent["children"]).append(el["uuid"])
        return el

    def mirror_cube(self, name, frm, to, parent=None, **kw):
        """mirror a cube across the XZ plane (z -> -z), mirroring rotation."""
        frm = list(frm); to = list(to)
        nf = [frm[0], frm[1], -to[2]]
        nt = [to[0], to[1], -frm[2]]
        rot = kw.get("rot")
        if rot is not None:
            rx, ry, rz = rot
            kw["rot"] = (-rx, -ry, rz) if abs(rz) > 1e-9 else (-rx, -ry, rz)
        origin = kw.get("origin")
        if origin is not None:
            kw["origin"] = [origin[0], origin[1], -origin[2]]
        return self.cube(name, nf, nt, parent=parent, **kw)

    def settle(self, floor=0.0):
        """shift the whole model vertically so its lowest point rests on `floor`."""
        lo = min(min(e["from"][1], e["to"][1]) for e in self.elements)
        dy = float(floor) - lo
        if abs(dy) < 1e-9:
            return self
        for e in self.elements:
            e["from"][1] += dy
            e["to"][1] += dy
            e["origin"][1] += dy

        def walk(nodes):
            for n in nodes:
                if isinstance(n, dict):
                    n["origin"][1] += dy
                    walk(n.get("children", []))
        walk(self.outliner)
        return self

    # -- output ----------------------------------------------------------
    def save(self, path_bbmodel, path_png=None, extra_meta=None):
        png = self.atlas.png_bytes()
        if path_png:
            with open(path_png, "wb") as f:
                f.write(png)
        b64 = base64.b64encode(png).decode()
        tex = {
            "path": "", "name": self.tex_name + ".png", "folder": "blockbench",
            "namespace": "", "id": "0", "group": "", "width": self.atlas.w,
            "height": self.atlas.h, "uv_width": self.atlas.w,
            "uv_height": self.atlas.h, "particle": False, "use_as_default": False,
            "layers_enabled": False, "sync_to_project": "", "render_mode": "default",
            "render_sides": "auto", "frame_time": 1, "frame_order_type": "loop",
            "frame_order": "", "frame_interpolate": False, "visible": True,
            "mode": "bitmap", "saved": False, "uuid": str(_uuid.uuid4()),
            "relative_path": "", "source": "data:image/png;base64," + b64,
        }
        proj = {
            "meta": {"format_version": "4.9", "model_format": "free",
                     "box_uv": False, "anim_time": 0, "anim_fps": 20},
            "name": self.name,
            "model_identifier": "",
            "visible_box": [1, 1, 1],
            "variable_placeholders": "",
            "variable_placeholder_buttons": [],
            "timeline_setups": [],
            "unhandled_root_fields": {},
            "resolution": {"width": self.atlas.w, "height": self.atlas.h},
            "elements": self.elements,
            "outliner": self.outliner,
            "textures": [tex],
        }
        if extra_meta:
            proj["meta"].update(extra_meta)
        with open(path_bbmodel, "w", encoding="utf-8") as f:
            json.dump(proj, f, ensure_ascii=False, indent=2)
        return path_bbmodel
