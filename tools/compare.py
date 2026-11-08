"""Render a model and overlay its silhouette on a reference photo."""
import subprocess
import sys
import os

import numpy as np
from PIL import Image

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import render as R


def mask_of(img, bg=None):
    a = np.asarray(img.convert("RGB")).astype(float)
    h, w, _ = a.shape
    if bg is None:
        bg = np.median(np.concatenate([a[:, :30], a[:, -30:]], axis=1), axis=1)
    d = np.abs(a - bg[:, None, :]).max(2)
    return d > 10


def bbox(m):
    ys, xs = np.where(m)
    return xs.min(), xs.max(), ys.min(), ys.max()


def norm(m, size=(700, 460)):
    x0, x1, y0, y1 = bbox(m)
    sub = Image.fromarray((m[y0:y1 + 1, x0:x1 + 1] * 255).astype(np.uint8))
    w, h = size
    s = min(w / sub.width, h / sub.height)
    sub = sub.resize((max(1, int(sub.width * s)), max(1, int(sub.height * s))), Image.BILINEAR)
    out = np.zeros((h, w), bool)
    oy, ox = (h - sub.height) // 2, (w - sub.width) // 2
    out[oy:oy + sub.height, ox:ox + sub.width] = np.asarray(sub) > 110
    return out


def compare(model, ref, out, az=78, el=20, ss=1, res=(1408, 768), shadow=0.0):
    R.render(model, "/tmp/_cmp.png", az=az, el=el, res=res, ss=ss, shadow=shadow,
             bg_flat="#e8e8e8")
    mine = Image.open("/tmp/_cmp.png")
    refi = Image.open(ref)
    m1 = norm(mask_of(refi))
    m2 = norm(mask_of(mine))
    h, w = m1.shape
    rgb = np.zeros((h, w, 3), np.uint8)
    rgb[:] = 245
    rgb[m1 & m2] = (120, 120, 120)
    rgb[m1 & ~m2] = (220, 60, 60)      # reference only
    rgb[~m1 & m2] = (60, 90, 220)      # model only
    side = np.concatenate([np.asarray(refi.convert("RGB").resize((w, int(refi.height * w / refi.width)))),
                           np.asarray(mine.convert("RGB").resize((w, int(mine.height * w / mine.width))))], axis=0)
    Image.fromarray(rgb).save(out)
    Image.fromarray(side).save(out.replace(".png", "_side.png"))
    iou = (m1 & m2).sum() / max(1, (m1 | m2).sum())
    print("IoU %.3f" % iou)
    return iou


if __name__ == "__main__":
    compare(sys.argv[1], sys.argv[2], sys.argv[3],
            az=float(sys.argv[4]) if len(sys.argv) > 4 else 78,
            el=float(sys.argv[5]) if len(sys.argv) > 5 else 20)
