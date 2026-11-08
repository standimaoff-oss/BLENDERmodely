"""Render the hero preview + thumbnail for every model and build gallery.html."""
import glob
import json
import os
import sys

from PIL import Image

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import render as R                                    # noqa: E402

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")

# model -> (azimuth, elevation, zoom)
CAM = {
    "01_porcelain_crane": (74, 16, 0.97),
    "02_basalt_beetle": (-36, 12, 0.96),
    "03_ash_moth": (-14, 15, 0.93),
}
REF = {
    "01_porcelain_crane": "01_porcelain_crane.png",
    "02_basalt_beetle": "02_basalt_beetle.png",
    "03_ash_moth": "03_ash_moth.png",
}
TITLES = {
    "01_porcelain_crane": ("01 — Фарфоровый журавль", "Porcelain Crane"),
    "02_basalt_beetle": ("02 — Базальтовый жук-углеед", "Basalt Ember Beetle"),
    "03_ash_moth": ("03 — Пепельная моль", "Ash Moth"),
}


def main():
    os.makedirs(os.path.join(ROOT, "previews"), exist_ok=True)
    cards = []
    for path in sorted(glob.glob(os.path.join(ROOT, "models", "*.bbmodel"))):
        name = os.path.splitext(os.path.basename(path))[0]
        az, el, zoom = CAM.get(name, (40, 18, 1.0))
        out = os.path.join(ROOT, "previews", name + ".png")
        R.render(path, out, az=az, el=el, zoom=zoom, res=(1408, 768), ss=2,
                 shadow=0.12, amb=0.60, fill=0.26,
                 bg=("#f2f2f0", "#e2e2de"))
        im = Image.open(out)
        im.resize((704, 384), Image.LANCZOS).save(
            os.path.join(ROOT, "previews", name + "_small.png"))
        doc = json.load(open(path, encoding="utf-8"))
        cards.append((name, len(doc["elements"]), TITLES.get(name, (name, ""))))
        print("preview ->", out)

    # ---------------- gallery ----------------
    rows = []
    for name, ncubes, (ru, en) in cards:
        ref_html = ""
        refpath = os.path.join(ROOT, "references", REF.get(name, ""))
        if os.path.exists(refpath):
            ref_html = ('<figure class="ref"><img src="references/%s" alt="reference">'
                        '<figcaption>референс</figcaption></figure>' % REF[name])
        rows.append("""
    <section class="card">
      <header><h2>%s</h2><p class="en">%s &middot; %d кубов &middot; 256&times;256 атлас</p></header>
      <div class="pair">
        %s
        <figure class="shot"><img src="previews/%s.png" alt="render">
          <figcaption>рендер модели</figcaption></figure>
      </div>
      <p class="links"><a href="models/%s.bbmodel">скачать .bbmodel</a>
      &middot; <a href="textures/%s.png">текстура</a>
      &middot; <a href="models/build_%s.py">build-скрипт</a></p>
    </section>""" % (ru, en, ncubes, ref_html, name, name, name, name))

    html = """<!doctype html>
<html lang="ru"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>BLENDERmodely — блоки-модели мобов</title>
<style>
  :root { color-scheme: light; }
  * { box-sizing: border-box; }
  body { margin:0; font:16px/1.5 -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,Arial,sans-serif;
         background:#f6f5f2; color:#1e1c1a; }
  header.top { padding:56px 24px 24px; text-align:center; }
  header.top h1 { margin:0 0 8px; font-size:34px; letter-spacing:-.02em; }
  header.top p { margin:0; color:#6b6660; max-width:720px; margin-inline:auto; }
  main { max-width:1180px; margin:0 auto; padding:24px 24px 80px; display:grid; gap:28px; }
  .card { background:#fff; border:1px solid #e4e1db; border-radius:16px; overflow:hidden;
          box-shadow:0 1px 2px rgba(0,0,0,.04), 0 12px 30px -22px rgba(0,0,0,.35); }
  .card header { padding:22px 26px 0; }
  .card h2 { margin:0; font-size:22px; letter-spacing:-.01em; }
  .card .en { margin:2px 0 0; color:#8a847c; font-size:14px; }
  .pair { display:grid; grid-template-columns:1fr 1fr; gap:2px; background:#eeebe5; margin-top:16px; }
  @media (max-width:820px) { .pair { grid-template-columns:1fr; } }
  figure { margin:0; position:relative; background:#eceae5; }
  figure img { display:block; width:100%; height:auto; }
  figcaption { position:absolute; left:10px; bottom:10px; font-size:12px; letter-spacing:.06em;
               text-transform:uppercase; color:#7b756d; background:rgba(255,255,255,.82);
               padding:3px 8px; border-radius:999px; }
  .links { margin:0; padding:16px 26px 22px; font-size:14px; }
  .links a { color:#8a5a1e; text-decoration:none; border-bottom:1px solid #e0d3c0; }
  .links a:hover { border-color:#8a5a1e; }
</style></head>
<body>
  <header class="top">
    <h1>BLENDERmodely</h1>
    <p>Блочные модели мобов, собранные под Blockbench: нативные <code>.bbmodel</code>,
       встроенная текстура-атлас 256&times;256 и рендер для сверки с референсом.</p>
  </header>
  <main>{{CARDS}}</main>
</body></html>
""".replace("{{CARDS}}", "".join(rows))
    with open(os.path.join(ROOT, "gallery.html"), "w", encoding="utf-8") as f:
        f.write(html)
    print("gallery.html written")


if __name__ == "__main__":
    main()
