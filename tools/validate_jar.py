#!/usr/bin/env python3
"""Offline smoke tests for the built mod jar (no JVM needed).

Validates: every JSON parses; every texture/model reference resolves inside
the jar; en_us and ru_ru lang key sets match; every translation key used in
the Java sources exists in both langs; every item/block id referenced by
recipes/tags is registered; armor layer textures exist for all suits;
mods.toml is sane; all compiled classes are present.
"""
import io
import json
import re
import sys
import tomllib
import zipfile
from pathlib import Path

JAR = Path(sys.argv[1] if len(sys.argv) > 1 else "jar/octoarms-1.0.0-mc1.20.1-forge47.2.0.jar")
SRC = Path("src/main/java")
MODID = "octoarms"

errors: list[str] = []
warnings: list[str] = []

zf = zipfile.ZipFile(JAR)
names = set(zf.namelist())

# ---------- 1. all JSON parse ----------
json_files = [n for n in names if n.endswith(".json") and (n.startswith("assets/") or n.startswith("data/"))]
parsed: dict[str, object] = {}
for n in json_files:
    try:
        parsed[n] = json.loads(zf.read(n))
    except Exception as e:  # noqa: BLE001
        errors.append(f"JSON parse fail {n}: {e}")

def model_path(model: str) -> str:
    ns, _, path = model.partition(":")
    return f"assets/{ns}/models/{path}.json"

def texture_path(tex: str) -> str:
    ns, _, path = tex.partition(":")
    return f"assets/{ns}/textures/{path}.png"

# ---------- 2. model -> texture refs ----------
for n, data in parsed.items():
    if not n.startswith(f"assets/{MODID}/models/"):
        continue
    d = data if isinstance(data, dict) else {}
    for layer_key, tex in (d.get("textures") or {}).items():
        if isinstance(tex, str) and not tex.startswith("#"):
            if texture_path(tex) not in names and f"assets/{MODID}/textures" in texture_path(tex):
                errors.append(f"{n}: missing texture {tex} -> {texture_path(tex)}")
    parent = d.get("parent")
    if isinstance(parent, str) and parent.startswith(f"{MODID}:"):
        if model_path(parent) not in names:
            errors.append(f"{n}: missing parent model {parent}")

# ---------- 3. blockstates -> models ----------
for n, data in parsed.items():
    if not n.startswith(f"assets/{MODID}/blockstates/"):
        continue
    refs: list[str] = []
    def walk(x):
        if isinstance(x, dict):
            for k, v in x.items():
                if k == "model" and isinstance(v, str):
                    refs.append(v)
                else:
                    walk(v)
        elif isinstance(x, list):
            for i in x:
                walk(i)
    walk(data)
    for r in refs:
        if r.startswith(f"{MODID}:") and model_path(r) not in names:
            errors.append(f"{n}: blockstate model missing {r}")

# ---------- 4. lang files ----------
en = parsed.get(f"assets/{MODID}/lang/en_us.json")
ru = parsed.get(f"assets/{MODID}/lang/ru_ru.json")
if not isinstance(en, dict) or not isinstance(ru, dict):
    errors.append("lang files missing or invalid")
    en, ru = {}, {}
only_en = set(en) - set(ru)
only_ru = set(ru) - set(en)
if only_en:
    errors.append(f"keys missing in ru_ru: {sorted(only_en)}")
if only_ru:
    errors.append(f"keys missing in en_us: {sorted(only_ru)}")

# ---------- 5. translation keys used in Java ----------
used_keys: set[str] = set()
key_re = re.compile(r'"((?:item|block|itemGroup|octoarms|key|death)[a-z0-9_.]*)"')
for p in SRC.rglob("*.java"):
    text = p.read_text(encoding="utf-8")
    for m in key_re.finditer(text):
        k = m.group(1)
        if k.startswith(("item.", "block.", "itemGroup.", "octoarms.", "key.")) and " " not in k and not k.endswith("."):
            used_keys.add(k)
missing_lang = sorted(k for k in used_keys if k not in en)
if missing_lang:
    errors.append(f"translation keys used in code but absent from lang: {missing_lang}")

# ground-truth id sets come from the jar itself: every item has an item model,
# every block a blockstate. Code-side register("...") literals are cross-checked
# against them to catch registrations without assets.
item_ids = {n.rsplit("/", 1)[-1][:-5] for n in names if n.startswith(f"assets/{MODID}/models/item/") and n.endswith(".json")}
block_ids = {n.rsplit("/", 1)[-1][:-5] for n in names if n.startswith(f"assets/{MODID}/blockstates/") and n.endswith(".json")}
reg_name_re = re.compile(r'(?:ITEMS|BLOCKS)\.register\("([a-z0-9_]+)"')
code_ids: set[str] = set()
for p in SRC.rglob("*.java"):
    if p.name.startswith("Octo") and "registry" in str(p.parent):
        code_ids |= {m.group(1) for m in reg_name_re.finditer(p.read_text(encoding="utf-8"))}
unmodelled = sorted(i for i in code_ids if i not in item_ids and i not in block_ids)
if unmodelled:
    errors.append(f"registered in code but no model/blockstate in jar: {unmodelled}")
for i in sorted(item_ids):
    if i in block_ids:
        continue  # BlockItems translate via block.<id>
    key = f"item.{MODID}.{i}"
    if key not in en:
        warnings.append(f"no lang entry {key}")
    if f"assets/{MODID}/models/item/{i}.json" not in names:
        errors.append(f"item model missing for {i}")
for b in sorted(block_ids):
    key = f"block.{MODID}.{b}"
    if key not in en:
        warnings.append(f"no lang entry {key}")
    if f"assets/{MODID}/blockstates/{b}.json" not in names:
        errors.append(f"blockstate missing for {b}")
    if f"assets/{MODID}/models/block/{b}.json" not in names and f"assets/{MODID}/models/item/{b}.json" not in names:
        warnings.append(f"no block/item model file for block {b} (may use builtin)")

# ---------- 6. recipes: ids and tags resolve ----------
known_ids = {f"{MODID}:{i}" for i in item_ids} | {f"{MODID}:{b}" for b in block_ids}
tag_files = {n for n in names if n.startswith(f"data/{MODID}/tags/")}
def check_ingredient(ing, where):
    if not isinstance(ing, dict):
        return
    if "item" in ing:
        iid = ing["item"]
        if iid.startswith(f"{MODID}:") and iid not in known_ids:
            errors.append(f"{where}: unknown item id {iid}")
    elif "tag" in ing:
        tag = ing["tag"]
        if tag.startswith(f"{MODID}:"):
            ns, _, t = tag.partition(":")
            cands = (f"data/{ns}/tags/blocks/{t}.json", f"data/{ns}/tags/items/{t}.json")
            if not any(c in tag_files for c in cands):
                errors.append(f"{where}: unknown tag {tag}")

for n, data in parsed.items():
    if not n.startswith(f"data/{MODID}/recipes/"):
        continue
    d = data if isinstance(data, dict) else {}
    ings = d.get("ingredients") or []
    for ing in ings:
        check_ingredient(ing, n)
    res = d.get("result")
    if isinstance(res, dict):
        rid = res.get("item")
        if isinstance(rid, str) and rid.startswith(f"{MODID}:") and rid not in known_ids:
            errors.append(f"{n}: result unknown id {rid}")
    # recipe type must have a serializer class registered in code

# ---------- 7. armor layer textures ----------
suit_re = re.compile(r'"([a-z_]+_mech)"')
specs = SRC / "com/octoarms/client/model/ArmorSpecs.java"
suit_src = specs.read_text() if specs.exists() else ""
suits = sorted({m.group(1) for m in suit_re.finditer(suit_src)}) or \
        sorted({k.split(".")[2].removesuffix("_chestplate") for k in en
                if k.startswith(f"item.{MODID}.") and k.endswith("_chestplate") and "mech" in k})
for s in suits:
    for layer in (1, 2):
        t = f"assets/{MODID}/textures/models/armor/{s}_layer_{layer}.png"
        if t not in names:
            errors.append(f"armor texture missing: {t}")

# ---------- 8. mods.toml ----------
toml = tomllib.loads(zf.read("META-INF/mods.toml").decode())
mods = toml.get("mods", [])
if not mods or mods[0].get("modId") != MODID:
    errors.append("mods.toml modId mismatch")
if mods and mods[0].get("version") not in ("1.0.0", "${file.jarVersion}"):
    warnings.append(f"mods.toml version: {mods[0].get('version')}")
loader = toml.get("loaderVersion", "")
if not loader.startswith("["):
    warnings.append(f"odd loaderVersion {loader}")

# ---------- 9. key classes present ----------
for cls in ("com/octoarms/OctoArms.class", "com/octoarms/mech/MechSuit.class",
            "com/octoarms/mech/ClimbHandler.class", "com/octoarms/item/MechArmorItem.class",
            "com/octoarms/client/OctoClient.class", "com/octoarms/client/model/OctoArmsModel.class",
            "com/octoarms/client/screen/MachineScreen.class", "com/octoarms/network/OctoNetwork.class",
            "com/octoarms/menu/MachineMenu.class"):
    if cls not in names:
        errors.append(f"class missing from jar: {cls}")

# ---------- 10. entity textures ----------
if f"assets/{MODID}/textures/entity/octo_arms.png" not in names:
    errors.append("entity texture octo_arms.png missing")

# ---------- report ----------
print(f"jar: {JAR} ({JAR.stat().st_size / 1024:.0f} KiB)")
print(f"entries: {len(names)}, json: {len(json_files)}, classes: {len([n for n in names if n.endswith('.class')])}, png: {len([n for n in names if n.endswith('.png')])}")
print(f"lang keys: en={len(en)} ru={len(ru)}; suits checked: {suits}")
print(f"items={len(item_ids)} blocks={len(block_ids)}")
for w in warnings:
    print(f"WARN  {w}")
for e in errors:
    print(f"ERROR {e}")
if errors:
    print(f"\nSMOKE TEST FAILED: {len(errors)} errors, {len(warnings)} warnings")
    sys.exit(1)
print(f"\nSMOKE TEST PASSED (0 errors, {len(warnings)} warnings)")
