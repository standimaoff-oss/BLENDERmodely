# Octo Arms — Mecha Exosuits for Minecraft 1.20.1 (Forge)

**Octo Arms** adds a full line of powered mech exosuits («меха-броня»), industrial
machines, a fuel/charge economy, energy weapons and a wall-crawling Spider Mech
with eight servo limbs — the «Octo Arms» rig this mod is named after.

> Русский: мод добавляет линейку мехо-экзокостюмов от медного до незеритового,
> паучий мех «Окто-Армс» с хождением по стенам, машины (гидравлический пресс,
> сборочный верстак, зарядный док), систему зарядки от топлива и батарей,
> энергетическое оружие и физику цепких лап, реагирующую на поверхность.

---

## Getting the jar

* **Releases**: the `latest-build` GitHub release always carries the freshest
  compiled jar (`octoarms-<version>-mc1.20.1-forge47.2.0.jar`).
* **Actions**: every push builds the mod; download `octoarms-jar` from the run.
* **Build yourself**: JDK 17 + `./gradlew build` → `build/libs/octoarms-1.0.0.jar`.

Drop the jar into your `mods/` folder together with Forge 47.x for 1.20.1.

## The suits (6 tiers, 24 pieces)

| Suit | Charge | Perks |
|---|---|---|
| Copper Mech «Scout» | 20k PU | cheap, light servo assist, Haste set bonus |
| Iron Mech «Legionary» | 50k PU | balanced plating, Resistance set bonus |
| Gold Mech «Praetorian» | 35k PU | fastest sprint, huge jump boost, fragile |
| Diamond Mech «Vanguard» | 120k PU | hover thrusters (V), visor night vision (G) |
| Netherite Mech «Titan» | 240k PU | flight, fire immunity, integrated arm cannon (R) |
| Spider Mech «Octo Arms» | 180k PU | **wall crawling (C)**, 8 animated servo legs, twin cannons (R), plasma claws |

Every piece stores **Power Units (PU)** in a Forge capability; the suit drains
charge for sprinting, jumping, hovering, climbing and weapon fire. Empty cells
mean a dead suit — visit a **Charging Dock**.

### Surface-reactive climbing (Spider Mech)

The octo limbs physically react to whatever they grip:

* **metal blocks** — fast crawl, sparks, anvil-like clang
* **stone/wood** — normal grip, falling-dust particles, grind/wood scrape
* **ice** — low grip: the mech slowly slides down with glassy sounds
* **honey/slime/mud** — extra sticky but slow
* **sand/gravel/soil** — crumbling grip, dust pours off the claws
* **water/lava/cobweb/magma** — no grip at all

Climb with `W`, hold still with `SHIFT`, wall-jump by pressing `SPACE` while
gripping. Grip % and surface are shown in the HUD.

## Machines (механизмы)

| Block | Purpose |
|---|---|
| **Hydraulic Press** | presses ingots into plates/gears/modules (fuel-fired) |
| **Mech Assembler** | 5×5 grid + PU buffer: builds whole exosuits from vanilla armor + components (recipes are deliberately heavy: a Diamond chest mech needs a diamond chestplate, 12 plates, gears, actuators, cores…) |
| **Charging Dock** | burns coal/blaze/enriched coal into PU and pumps it into suits, cells and weapons |

Power cells (`power_cell` → `nether_power_cell`) are hot-swappable batteries;
cells in your inventory trickle-charge the worn suit.

## Weapons

* **Plasma Blade** — sword that melts armor while charged (heat builds up)
* **Arm Cannon** — rapid plasma bolts
* **Railgun** — charge-up piercing slug with recoil
* **Integrated cannons** — on Netherite «Titan» and Spider «Octo Arms» chests, fired with `R`

Overheated weapons lock until cooled.

## Controls

| Key | Action |
|---|---|
| `R` | fire integrated octo/arm cannon |
| `V` | toggle hover thrusters |
| `G` | toggle visor night vision |
| `C` | toggle wall cling |

## Repository layout

```
src/main/java/com/octoarms/   mod source (common + client)
src/main/resources/           generated + hand-written assets/data
tools/                        Python generators (textures, models, recipes, lang)
.github/workflows/build.yml   CI: builds the jar and publishes the release
```

All textures (≈90 PNGs), the bulky armor models (`ArmorSpecs.java`), machine
GUIs, recipes and both languages (RU/EN) are produced by `tools/generate.py`
(Pillow). Re-run it after editing `tools/content.py`:

```bash
pip install pillow
python3 tools/generate.py
```

## License

MIT — see [LICENSE](LICENSE).
