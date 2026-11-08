"""Verification renderer: loads a .bbmodel and rasterises it the same way
Blockbench displays it (orthographic-ish perspective, per-face lighting).

usage: python3 render.py model.bbmodel out.png [--az 38] [--el 22] [--zoom 1.0]
"""
import base64
import json
import math
import sys

import numpy as np

from pngio import read_png, write_png

FACE_AXES = {
    "east":  ((1, 1, 1), (0, 0, -1), (0, -1, 0)),
    "west":  ((0, 1, 0), (0, 0, 1), (0, -1, 0)),
    "north": ((1, 1, 0), (-1, 0, 0), (0, -1, 0)),
    "south": ((0, 1, 1), (1, 0, 0), (0, -1, 0)),
    "up":    ((0, 1, 0), (1, 0, 0), (0, 0, 1)),
    "down":  ((0, 0, 1), (1, 0, 0), (0, 0, -1)),
}
FACE_NORMAL = {"east": (1, 0, 0), "west": (-1, 0, 0), "up": (0, 1, 0),
               "down": (0, -1, 0), "south": (0, 0, 1), "north": (0, 0, -1)}


def rotmat(deg):
    """Blockbench euler order ZYX (applied X then Y then Z)."""
    rx, ry, rz = [math.radians(v) for v in deg]
    Rx = np.array([[1, 0, 0], [0, math.cos(rx), -math.sin(rx)], [0, math.sin(rx), math.cos(rx)]])
    Ry = np.array([[math.cos(ry), 0, math.sin(ry)], [0, 1, 0], [-math.sin(ry), 0, math.cos(ry)]])
    Rz = np.array([[math.cos(rz), -math.sin(rz), 0], [math.sin(rz), math.cos(rz), 0], [0, 0, 1]])
    return Rz @ Ry @ Rx


def around(origin, R):
    o = np.array(origin, float)
    M = np.eye(4)
    M[:3, :3] = R
    M[:3, 3] = o - R @ o
    return M


def load_texture(doc):
    src = doc["textures"][0]["source"]
    data = base64.b64decode(src.split(",", 1)[1])
    tmp = "/tmp/_tex.png"
    with open(tmp, "wb") as f:
        f.write(data)
    return read_png(tmp)


def collect(doc):
    """returns list of (cube_element, world_matrix4)"""
    els = {e["uuid"]: e for e in doc["elements"]}
    out = []

    def walk(node, M):
        if isinstance(node, str):
            e = els.get(node)
            if e is None:
                return
            Me = around(e.get("origin", [0, 0, 0]), rotmat(e.get("rotation", [0, 0, 0])))
            out.append((e, M @ Me))
        else:
            M2 = M
            rot = node.get("rotation", [0, 0, 0])
            if any(abs(v) > 1e-9 for v in rot):
                M2 = M @ around(node.get("origin", [0, 0, 0]), rotmat(rot))
            for ch in node.get("children", []):
                walk(ch, M2)

    for n in doc["outliner"]:
        walk(n, np.eye(4))
    return out


def build_tris(doc):
    tw, th = doc["resolution"]["width"], doc["resolution"]["height"]
    tris = []
    cubes = collect(doc)
    for e, M in cubes:
        frm = np.array(e["from"], float)
        to = np.array(e["to"], float)
        inf = e.get("inflate", 0) or 0
        frm = frm - inf
        to = to + inf
        for fname, fa in FACE_AXES.items():
            face = e["faces"].get(fname)
            if not face or face.get("texture") is None:
                continue
            uv = face["uv"]
            corner, uax, vax = fa
            c3 = np.array([to[i] if corner[i] else frm[i] for i in range(3)])
            uax = np.array(uax, float)
            vax = np.array(vax, float)
            lx, ly, lz = to - frm
            ulen = (abs(uax) * np.array([lx, ly, lz])).sum()
            vlen = (abs(vax) * np.array([lx, ly, lz])).sum()
            u1, v1, u2, v2 = uv
            p = [c3, c3 + uax * ulen, c3 + uax * ulen + vax * vlen, c3 + vax * vlen]
            uv_pts = [(u1, v1), (u2, v1), (u2, v2), (u1, v2)]
            p = [(M @ np.array([*q, 1.0]))[:3] for q in p]
            n = (M[:3, :3] @ np.array(FACE_NORMAL[fname], float))
            nl = np.linalg.norm(n)
            n = n / nl if nl else n
            for a, b, c in ((0, 1, 2), (0, 2, 3)):
                tris.append((p[a], p[b], p[c], uv_pts[a], uv_pts[b], uv_pts[c], n, (tw, th)))
    return tris


def camera(eye, target, up=(0, 1, 0)):
    eye = np.array(eye, float)
    target = np.array(target, float)
    f = target - eye
    f /= np.linalg.norm(f)
    r = np.cross(f, np.array(up, float))
    r /= np.linalg.norm(r)
    u = np.cross(r, f)
    return eye, np.stack([r, u, f])  # rows: right, up, forward


def render(bbpath, outpath, az=38.0, el=20.0, zoom=1.0, res=(1408, 768), ss=2,
           fov=26.0, bg=("#f0f0ee", "#dcdcda"), shadow=0.20, key=(-0.45, 0.78, 0.45),
           fill=0.30, amb=0.60, target=None, dist=None, spin=0.0, bg_flat=None):
    doc = json.load(open(bbpath, encoding="utf-8"))
    tex = load_texture(doc)
    th, tw = tex.shape[:2]
    tris = build_tris(doc)
    if not tris:
        raise RuntimeError("no geometry")

    # model bounds
    pts = np.array([p for t in tris for p in t[:3]])
    lo, hi = pts.min(0), pts.max(0)
    ctr = (lo + hi) / 2 if target is None else np.array(target, float)

    a = math.radians(az)
    e = math.radians(el)
    d = np.array([math.cos(e) * math.cos(a), math.sin(e), math.cos(e) * math.sin(a)])
    if spin:
        cs, sn = math.cos(math.radians(spin)), math.sin(math.radians(spin))
        d = np.array([d[0] * cs - d[2] * sn, d[1], d[0] * sn + d[2] * cs])

    W, H = res[0] * ss, res[1] * ss
    aspect = W / H
    tv = math.tan(math.radians(fov) / 2)

    radius = np.linalg.norm(hi - lo) / 2
    d0 = radius / tv * 1.55 / zoom
    if dist:
        d0 = dist
    for _ in range(6):
        eye, R = camera(ctr + d * d0, ctr)
        cam = (pts - eye) @ R.T
        z = cam[:, 2]
        z = np.maximum(z, 1e-3)
        sx = cam[:, 0] / (z * tv * aspect)
        sy = cam[:, 1] / (z * tv)
        m = max(np.abs(sx).max(), np.abs(sy).max())
        d0 *= m * 1.06
    d0 /= max(zoom, 1e-6)
    eye, R = camera(ctr + d * d0, ctr)
    cam2d = (pts - eye) @ R.T

    # lights
    L = np.array(key, float); L /= np.linalg.norm(L)
    F = np.array([-L[0], 0.25, -L[2]]); F /= np.linalg.norm(F)

    if bg_flat:
        img = np.zeros((H, W, 3), np.float32)
        img[:] = np.array(bg_flat if not isinstance(bg_flat, str) else
                          [int(bg_flat[i:i + 2], 16) for i in (1, 3, 5)], np.float32)
    else:
        c0 = np.array([int(bg[0][i:i + 2], 16) for i in (1, 3, 5)], np.float32)
        c1 = np.array([int(bg[1][i:i + 2], 16) for i in (1, 3, 5)], np.float32)
        yy = np.linspace(0, 1, H)[:, None, None]
        xx = np.linspace(0, 1, W)[None, :, None]
        t = np.clip(0.5 * yy + 0.5 * (1 - np.abs(xx - 0.5) * 2) ** 0.0 * 0 + 0.5 * yy * 0, 0, 1)
        t = 0.55 * yy + 0.45 * (1 - np.abs(xx * 2 - 1))
        img = c0 * (1 - t[..., :1]) + c1 * t[..., :1]

    zbuf = np.full((H, W), np.inf, np.float32)

    # ---- ground shadow (project to y = lo[1]) via silhouette from light
    if shadow:
        y0 = lo[1]
        S = np.array([0.22, -1.0, 0.10], float); S /= np.linalg.norm(S)
        sh = np.zeros((H, W), np.float32)
        for t in tris:
            p3 = []
            for q in (t[0], t[1], t[2]):
                q = np.array(q, float)
                k = (q[1] - y0) / abs(S[1])
                p3.append(q + S * k)
            sp = []
            ok = True
            for q in p3:
                c = (np.array([*q, 1.0]) - np.append(eye, 1.0))
                qc = R @ c[:3]
                if qc[2] <= 0.01:
                    ok = False
                    break
                sp.append(((qc[0] / (qc[2] * tv * aspect) * 0.5 + 0.5) * W,
                           (0.5 - qc[1] / (qc[2] * tv) * 0.5) * H))
            if not ok:
                continue
            (x0, y0s), (x1, y1s), (x2, y2s) = sp
            mnx = max(0, int(math.floor(min(x0, x1, x2))))
            mxx = min(W - 1, int(math.ceil(max(x0, x1, x2))))
            mny = max(0, int(math.floor(min(y0s, y1s, y2s))))
            mxy = min(H - 1, int(math.ceil(max(y0s, y1s, y2s))))
            if mxx < mnx or mxy < mny:
                continue
            yy, xx = np.mgrid[mny:mxy + 1, mnx:mxx + 1].astype(np.float32)
            xx += 0.5; yy += 0.5
            den = (y1s - y2s) * (x0 - x2) + (x2 - x1) * (y0s - y2s)
            if abs(den) < 1e-9:
                continue
            l0 = ((y1s - y2s) * (xx - x2) + (x2 - x1) * (yy - y2s)) / den
            l1 = ((y2s - y0s) * (xx - x2) + (x0 - x2) * (yy - y2s)) / den
            l2 = 1 - l0 - l1
            inside = (l0 >= -0.002) & (l1 >= -0.002) & (l2 >= -0.002)
            sh[mny:mxy + 1, mnx:mxx + 1][inside] = 1.0
        # blur
        k = max(3, int(11 * ss))
        ker = np.ones(k, np.float32) / k
        pad = k // 2
        tmp = np.apply_along_axis(lambda r: np.convolve(r, ker, mode="same"), 1, sh)
        tmp = np.apply_along_axis(lambda c: np.convolve(c, ker, mode="same"), 0, tmp)
        sh = np.clip(tmp * 1.35, 0, 1)
        img *= (1 - shadow * sh)[..., None]

    # ---- main rasterisation
    for t in tris:
        p0, p1, p2, uv0, uv1, uv2, n, (tw_, th_) = t
        sp = []
        ok = True
        for q in (p0, p1, p2):
            qc = R @ (q - eye)
            if qc[2] <= 0.02:
                ok = False
                break
            sp.append((((qc[0] / (qc[2] * tv * aspect)) * 0.5 + 0.5) * W,
                       (0.5 - (qc[1] / (qc[2] * tv)) * 0.5) * H, qc[2]))
        if not ok:
            continue
        (x0, y0, w0), (x1, y1, w1), (x2, y2, w2) = sp
        mnx = max(0, int(math.floor(min(x0, x1, x2))))
        mxx = min(W - 1, int(math.ceil(max(x0, x1, x2))))
        mny = max(0, int(math.floor(min(y0, y1, y2))))
        mxy = min(H - 1, int(math.ceil(max(y0, y1, y2))))
        if mxx < mnx or mxy < mny:
            continue
        den = (y1 - y2) * (x0 - x2) + (x2 - x1) * (y0 - y2)
        if abs(den) < 1e-9:
            continue
        yy, xx = np.mgrid[mny:mxy + 1, mnx:mxx + 1].astype(np.float32)
        xx += 0.5; yy += 0.5
        l0 = ((y1 - y2) * (xx - x2) + (x2 - x1) * (yy - y2)) / den
        l1 = ((y2 - y0) * (xx - x2) + (x0 - x2) * (yy - y2)) / den
        l2 = 1 - l0 - l1
        inside = (l0 >= 0) & (l1 >= 0) & (l2 >= 0)
        if not inside.any():
            continue
        iw = l0 / w0 + l1 / w1 + l2 / w2
        zz = 1.0 / iw
        sub = zbuf[mny:mxy + 1, mnx:mxx + 1]
        vis = inside & (zz < sub)
        if not vis.any():
            continue
        # perspective correct uv
        a0 = (l0 / w0) / iw
        a1 = (l1 / w1) / iw
        a2 = (l2 / w2) / iw
        u = a0 * uv0[0] + a1 * uv1[0] + a2 * uv2[0]
        v = a0 * uv0[1] + a1 * uv1[1] + a2 * uv2[1]
        ti = np.clip((v).astype(np.int32), 0, th_ - 1)
        tj = np.clip((u).astype(np.int32), 0, tw_ - 1)
        col = tex[ti, tj].astype(np.float32)
        lam = max(0.0, float(n @ L))
        lam2 = max(0.0, float(n @ F))
        inten = amb + 0.5 * lam + fill * lam2
        col = col * inten
        sub[vis] = zz[vis]
        block = img[mny:mxy + 1, mnx:mxx + 1]
        block[vis] = col[vis]
    img = np.clip(img, 0, 255)
    # downsample
    if ss > 1:
        img = img.reshape(res[1], ss, res[0], ss, 3).mean(axis=(1, 3))
    write_png(outpath, np.clip(img, 0, 255).astype(np.uint8))
    return outpath


if __name__ == "__main__":
    import argparse
    ap = argparse.ArgumentParser()
    ap.add_argument("model")
    ap.add_argument("out")
    ap.add_argument("--az", type=float, default=38.0)
    ap.add_argument("--el", type=float, default=20.0)
    ap.add_argument("--zoom", type=float, default=1.0)
    ap.add_argument("--fov", type=float, default=26.0)
    ap.add_argument("--ss", type=int, default=2)
    ap.add_argument("--spin", type=float, default=0.0)
    ap.add_argument("--res", default="1408x768")
    ap.add_argument("--shadow", type=float, default=0.11)
    ap.add_argument("--amb", type=float, default=0.60)
    ap.add_argument("--fill", type=float, default=0.30)
    ap.add_argument("--key", default="-0.45,0.78,0.45")
    ap.add_argument("--bg", default="#f0f0ee,#dcdcda")
    ap.add_argument("--bg-flat", dest="bg_flat", default=None)
    a = ap.parse_args()
    W, H = [int(v) for v in a.res.lower().split("x")]
    render(a.model, a.out, az=a.az, el=a.el, zoom=a.zoom, fov=a.fov, ss=a.ss,
           spin=a.spin, res=(W, H), shadow=a.shadow, amb=a.amb, fill=a.fill,
           key=tuple(float(v) for v in a.key.split(",")),
           bg=tuple(a.bg.split(",")), bg_flat=a.bg_flat)
    print("wrote", a.out)
