"""Sanity-check .bbmodel files (structure, uuids, uv bounds, texture payload)."""
import base64
import glob
import json
import os
import sys

REQUIRED_FACE = ("uv", "texture")


def check(path):
    errs, warns = [], []
    doc = json.load(open(path, encoding="utf-8"))
    meta = doc.get("meta", {})
    if meta.get("format_version") is None:
        errs.append("missing meta.format_version")
    res = doc.get("resolution") or {}
    tw, th = res.get("width"), res.get("height")
    if not tw or not th:
        errs.append("missing resolution")
    els = doc.get("elements", [])
    if not els:
        errs.append("no elements")
    uuids = set()
    for e in els:
        u = e.get("uuid")
        if not u:
            errs.append("element without uuid: %r" % e.get("name"))
        if u in uuids:
            errs.append("duplicate uuid %s" % u)
        uuids.add(u)
        if e.get("type") != "cube":
            warns.append("%s: type %r" % (e.get("name"), e.get("type")))
        for k in ("from", "to", "origin"):
            if k not in e:
                errs.append("%s: missing %s" % (e.get("name"), k))
            elif any(e[k][i] > e[k + "x"] for i in ()):
                pass
        if any(e["to"][i] < e["from"][i] for i in range(3)):
            errs.append("%s: to < from" % e.get("name"))
        faces = e.get("faces", {})
        if len(faces) != 6:
            warns.append("%s: %d faces" % (e.get("name"), len(faces)))
        for fn, f in faces.items():
            if f.get("texture") is None:
                continue
            uv = f.get("uv")
            if not uv or len(uv) != 4:
                errs.append("%s/%s: bad uv" % (e.get("name"), fn))
                continue
            if min(uv) < 0 or max(uv) > max(tw, th):
                errs.append("%s/%s: uv out of texture %s" % (e.get("name"), fn, uv))
    seen = set()

    def walk(n):
        if isinstance(n, str):
            if n not in uuids:
                errs.append("outliner references unknown uuid %s" % n)
            if n in seen:
                errs.append("uuid listed twice in outliner: %s" % n)
            seen.add(n)
        else:
            if "uuid" not in n:
                errs.append("group without uuid: %r" % n.get("name"))
            if any(abs(v) > 1e-9 for v in n.get("rotation", [0, 0, 0])) and \
                    sum(1 for v in n.get("rotation", [0, 0, 0]) if abs(v) > 1e-9) > 1:
                errs.append("group %s: multi-axis rotation" % n.get("name"))
            for ch in n.get("children", []):
                walk(ch)
    for n in doc.get("outliner", []):
        walk(n)
    missing = uuids - seen
    if missing:
        errs.append("%d elements not in outliner" % len(missing))
    tw_ = doc.get("textures", [])
    if not tw_:
        errs.append("no textures")
    else:
        src = tw_[0].get("source", "")
        if not src.startswith("data:image/png;base64,"):
            errs.append("texture not embedded")
        else:
            raw = base64.b64decode(src.split(",", 1)[1])
            if raw[:8] != b"\x89PNG\r\n\x1a\n":
                errs.append("texture payload is not a png")
    return errs, warns


if __name__ == "__main__":
    pats = sys.argv[1:] or ["models/*.bbmodel"]
    bad = 0
    for pat in pats:
        for path in sorted(glob.glob(pat)):
            errs, warns = check(path)
            n = len(json.load(open(path, encoding="utf-8"))["elements"])
            print("%-42s %3d cubes  %s" % (os.path.basename(path), n,
                                           "OK" if not errs else "FAIL"))
            for e in errs:
                print("   ERROR:", e)
            for w in warns[:5]:
                print("   warn :", w)
            bad += len(errs)
    sys.exit(1 if bad else 0)
