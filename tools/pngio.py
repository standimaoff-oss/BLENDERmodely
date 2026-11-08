"""Minimal dependency-free PNG reader/writer (8-bit RGB/RGBA, non-interlaced)."""
import struct, zlib
import numpy as np


def write_png(path, img):
    """img: HxWx3 or HxWx4 uint8 array."""
    img = np.asarray(img, dtype=np.uint8)
    if img.ndim != 3 or img.shape[2] not in (3, 4):
        raise ValueError("expected HxWx3 or HxWx4")
    h, w, c = img.shape
    ctype = {3: 2, 4: 6}[c]
    raw = bytearray()
    for row in img:
        raw.append(0)                     # filter type 0
        raw += row.tobytes()

    def chunk(tag, data):
        return (struct.pack(">I", len(data)) + tag + data +
                struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF))

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, ctype, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(bytes(raw), 9))
    png += chunk(b"IEND", b"")
    with open(path, "wb") as f:
        f.write(png)
    return path


def read_png(path):
    """Returns HxWx3 uint8 (alpha dropped over white)."""
    with open(path, "rb") as f:
        data = f.read()
    assert data[:8] == b"\x89PNG\r\n\x1a\n", "not a png"
    pos, idat, w, h, bitd, ctype, interlace = 8, b"", 0, 0, 8, 6, 0
    while pos < len(data):
        ln = struct.unpack(">I", data[pos:pos + 4])[0]
        tag = data[pos + 4:pos + 8]
        body = data[pos + 8:pos + 8 + ln]
        pos += 12 + ln
        if tag == b"IHDR":
            w, h, bitd, ctype, _, _, interlace = struct.unpack(">IIBBBBB", body)
        elif tag == b"IDAT":
            idat += body
        elif tag == b"IEND":
            break
    assert bitd == 8 and interlace == 0, "unsupported png"
    nch = {0: 1, 2: 3, 4: 2, 6: 4}[ctype]
    raw = np.frombuffer(zlib.decompress(idat), dtype=np.uint8)
    stride = w * nch
    out = np.zeros((h, stride), dtype=np.uint8)
    prev = np.zeros(stride, dtype=np.int32)
    p = 0
    for y in range(h):
        ft = raw[p]; p += 1
        line = raw[p:p + stride].astype(np.int32); p += stride
        if ft == 0:
            cur = line
        elif ft == 1:
            cur = line.copy()
            for i in range(nch, stride):
                cur[i] = (cur[i] + cur[i - nch]) & 0xFF
        elif ft == 2:
            cur = (line + prev) & 0xFF
        elif ft == 3:
            cur = line.copy()
            for i in range(stride):
                a = cur[i - nch] if i >= nch else 0
                cur[i] = (cur[i] + ((a + prev[i]) >> 1)) & 0xFF
        elif ft == 4:
            cur = line.copy()
            for i in range(stride):
                a = cur[i - nch] if i >= nch else 0
                b = prev[i]
                cc = prev[i - nch] if i >= nch else 0
                pp = a + b - cc
                pa, pb, pc = abs(pp - a), abs(pp - b), abs(pp - cc)
                pr = a if (pa <= pb and pa <= pc) else (b if pb <= pc else cc)
                cur[i] = (cur[i] + pr) & 0xFF
        else:
            raise ValueError("bad filter %d" % ft)
        out[y] = cur.astype(np.uint8)
        prev = cur
    arr = out.reshape(h, w, nch)
    if nch == 4:
        rgb = arr[:, :, :3].astype(np.float32)
        a = arr[:, :, 3:4].astype(np.float32) / 255.0
        return (rgb * a + 255.0 * (1 - a)).astype(np.uint8)
    if nch == 3:
        return arr
    return np.repeat(arr, 3, axis=2)
