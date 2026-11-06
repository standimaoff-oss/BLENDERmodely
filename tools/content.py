"""Master content table for the Octo Arms mod.

Single source of truth shared by every generator script (textures, models,
recipes, lang files).  The Java side mirrors these registry names.
"""

MODID = "octoarms"

# --------------------------------------------------------------------------- #
# palettes
# --------------------------------------------------------------------------- #

TIERS = {
    "copper_mech": {
        "en": "Copper Mech",
        "ru": "Медный мех",
        "suffix_en": "Scout",
        "suffix_ru": "Разведчик",
        "base": "#B1653A",
        "dark": "#6E3A1E",
        "accent": "#E8A169",
        "glow": "#5FE3C8",
        "visor": "#8CFFE8",
        "material": "copper",
        "repair": "minecraft:copper_ingot",
        "sound": "minecraft:item.armor.equip_iron",
    },
    "iron_mech": {
        "en": "Iron Mech",
        "ru": "Железный мех",
        "suffix_en": "Legionary",
        "suffix_ru": "Легионер",
        "base": "#C6C6C6",
        "dark": "#7E7E7E",
        "accent": "#F2F2F2",
        "glow": "#7FB2FF",
        "visor": "#BBD9FF",
        "material": "iron",
        "repair": "minecraft:iron_ingot",
        "sound": "minecraft:item.armor.equip_iron",
    },
    "gold_mech": {
        "en": "Gold Mech",
        "ru": "Золотой мех",
        "suffix_en": "Praetorian",
        "suffix_ru": "Преторий",
        "base": "#F2C230",
        "dark": "#A67C11",
        "accent": "#FFE98A",
        "glow": "#FFF6B0",
        "visor": "#FFF3A0",
        "material": "gold",
        "repair": "minecraft:gold_ingot",
        "sound": "minecraft:item.armor.equip_gold",
    },
    "diamond_mech": {
        "en": "Diamond Mech",
        "ru": "Алмазный мех",
        "suffix_en": "Vanguard",
        "suffix_ru": "Авангард",
        "base": "#5FD9CE",
        "dark": "#2C8B8B",
        "accent": "#B6FFF6",
        "glow": "#DFFFFF",
        "visor": "#CFFFFF",
        "material": "diamond",
        "repair": "minecraft:diamond",
        "sound": "minecraft:item.armor.equip_diamond",
    },
    "netherite_mech": {
        "en": "Netherite Mech",
        "ru": "Незеритовый мех",
        "suffix_en": "Titan",
        "suffix_ru": "Титан",
        "base": "#5B4A4F",
        "dark": "#33272B",
        "accent": "#8C747A",
        "glow": "#FF6A3C",
        "visor": "#FF9B57",
        "material": "netherite",
        "repair": "minecraft:netherite_ingot",
        "sound": "minecraft:item.armor.equip_netherite",
    },
    "spider_mech": {
        "en": "Spider Mech",
        "ru": "Паучий мех",
        "suffix_en": "Octo Arms",
        "suffix_ru": "Окто-Армс",
        "base": "#57606E",
        "dark": "#2B303A",
        "accent": "#93A0B2",
        "glow": "#B4FF3C",
        "visor": "#FF3B30",
        "material": "spider",
        "repair": "minecraft:netherite_ingot",
        "sound": "minecraft:item.armor.equip_netherite",
    },
}

TIER_ORDER = ["copper_mech", "iron_mech", "gold_mech", "diamond_mech", "netherite_mech", "spider_mech"]

PIECES = [
    ("helmet", "Helmet", "Шлем"),
    ("chestplate", "Chestplate", "Нагрудник"),
    ("leggings", "Leggings", "Поножи"),
    ("boots", "Boots", "Ботинки"),
]

# --------------------------------------------------------------------------- #
# items:  name -> (en, ru, painter, palette-key or None)
# --------------------------------------------------------------------------- #

COMPONENTS = {
    "copper_plate": ("Copper Plate", "Медная пластина", "plate", "#B1653A"),
    "iron_plate": ("Iron Plate", "Железная пластина", "plate", "#C6C6C6"),
    "gold_plate": ("Gold Plate", "Золотая пластина", "plate", "#F2C230"),
    "diamond_plate": ("Diamond Plate", "Алмазная пластина", "plate", "#5FD9CE"),
    "netherite_plate": ("Netherite Plate", "Незеритовая пластина", "plate", "#5B4A4F"),
    "obsidian_plate": ("Obsidian Plate", "Обсидиановая пластина", "plate", "#3A2C52"),

    "copper_gear": ("Copper Gear", "Медная шестерня", "gear", "#B1653A"),
    "iron_gear": ("Iron Gear", "Железная шестерня", "gear", "#C6C6C6"),
    "diamond_gear": ("Diamond Gear", "Алмазная шестерня", "gear", "#5FD9CE"),
    "netherite_gear": ("Netherite Gear", "Незеритовая шестерня", "gear", "#5B4A4F"),

    "actuator": ("Servo Actuator", "Сервопривод", "actuator", "#9AA3AD"),
    "heavy_actuator": ("Heavy Servo Actuator", "Тяжёлый сервопривод", "actuator", "#6E7885"),
    "hydraulic_piston": ("Hydraulic Piston", "Гидропоршень", "piston", "#B7C0CB"),
    "circuit_board": ("Circuit Board", "Плата управления", "circuit", "#2E8B57"),
    "advanced_circuit": ("Advanced Circuit", "Улучшенная плата", "circuit", "#1F6FA8"),
    "power_core": ("Power Core", "Энергоядро", "core", "#5FE3C8"),
    "nether_power_core": ("Nether Power Core", "Незер-энергоядро", "core", "#FF6A3C"),
    "reinforced_frame": ("Reinforced Frame", "Усиленный каркас", "frame", "#8C8C8C"),
    "silk_mesh": ("Grip Silk Mesh", "Цепкая шёлковая сеть", "mesh", "#C8C8D8"),
    "octo_arm": ("Octo Arm Module", "Модуль «Окто-рука»", "octoarm", "#93A0B2"),
    "thruster_module": ("Thruster Module", "Модуль тяги", "thruster", "#C0C8D2"),

    "power_cell": ("Power Cell", "Энергоячейка", "cell", "#5FE3C8"),
    "advanced_power_cell": ("Advanced Power Cell", "Улучшенная энергоячейка", "cell", "#7FB2FF"),
    "nether_power_cell": ("Nether Power Cell", "Незер-энергоячейка", "cell", "#FF6A3C"),

    "enriched_coal": ("Enriched Coal", "Обогащённый уголь", "coal", "#3B3B44"),
    "energy_orb": ("Energy Orb", "Энергосфера", "orb", "#8CF0FF"),
    "heavy_slug": ("Heavy Slug", "Тяжёлая болванка", "slug", "#A9B2BC"),
}

WEAPONS = {
    "plasma_blade": ("Plasma Blade", "Плазменный клинок", "blade", "#8CF0FF"),
    "arm_cannon": ("Arm Cannon", "Ручная пушка", "cannon", "#9AA3AD"),
    "railgun": ("Railgun", "Рельсотрон", "railgun", "#6E7885"),
}

BLOCKS_SIMPLE = {
    "steel_casing": ("Steel Casing", "Стальной корпус", "casing", "#9EA7B2"),
    "coil_block": ("Copper Coil Block", "Блок медной обмотки", "coil", "#B1653A"),
}

MACHINES = {
    "hydraulic_press": ("Hydraulic Press", "Гидравлический пресс", "#8E97A2", "#FFC24A"),
    "charging_dock": ("Charging Dock", "Зарядный док", "#7E8894", "#5FE3C8"),
    "mech_assembler": ("Mech Assembler", "Сборочный верстак", "#96A0AC", "#B4FF3C"),
}

TAB = ("Octo Arms", "Окто-Армс")

MACHINE_GUI = {
    "hydraulic_press": ("Pressing", "Прессование"),
    "charging_dock": ("Charging", "Зарядка"),
    "mech_assembler": ("Assembly", "Сборка"),
}


def all_items():
    """Every plain (non-block, non-armor) item name."""
    out = list(COMPONENTS) + list(WEAPONS)
    return out


def armor_items():
    out = []
    for tier in TIER_ORDER:
        for piece, _, _ in PIECES:
            out.append(f"{tier}_{piece}")
    return out


def lang_entries():
    """Build the full lang dict {key: (en, ru)}."""
    entries = {}

    entries[f"itemGroup.{MODID}"] = TAB
    entries[f"itemGroup.{MODID}.mechs"] = (TAB[0] + " — Mechs", TAB[1] + " — Мехи")

    for tier in TIER_ORDER:
        t = TIERS[tier]
        for piece, en_piece, ru_piece in PIECES:
            key = f"item.{MODID}.{tier}_{piece}"
            entries[key] = (f"{t['en']} {en_piece} «{t['suffix_en']}»",
                            f"{t['ru']} {ru_piece} «{t['suffix_ru']}»")

    for name, (en, ru, _painter, _pal) in COMPONENTS.items():
        entries[f"item.{MODID}.{name}"] = (en, ru)

    for name, (en, ru, _painter, _pal) in WEAPONS.items():
        entries[f"item.{MODID}.{name}"] = (en, ru)

    for name, (en, ru, _p, _pal) in BLOCKS_SIMPLE.items():
        entries[f"block.{MODID}.{name}"] = (en, ru)

    for name, (en, ru, _b, _g) in MACHINES.items():
        entries[f"block.{MODID}.{name}"] = (en, ru)

    # entity / gui / tooltips / keys / subtitles
    extra = {
        f"entity.{MODID}.plasma_bolt": ("Plasma Bolt", "Плазменный заряд"),
        f"entity.{MODID}.rail_slug": ("Rail Slug", "Рельсотронный снаряд"),

        f"container.{MODID}.hydraulic_press": ("Hydraulic Press", "Гидравлический пресс"),
        f"container.{MODID}.charging_dock": ("Charging Dock", "Зарядный док"),
        f"container.{MODID}.mech_assembler": ("Mech Assembler", "Сборочный верстак"),

        f"tooltip.{MODID}.charge": ("Charge: %s / %s PU", "Заряд: %s / %s ЕМ"),
        f"tooltip.{MODID}.charge.empty": ("Empty — recharge at a Charging Dock",
                                           "Пусто — зарядите в зарядном доке"),
        f"tooltip.{MODID}.heat": ("Heat: %s%%", "Нагрев: %s%%"),
        f"tooltip.{MODID}.suit.capacity": ("Cell capacity: %s PU", "Ёмкость ячеек: %s ЕМ"),
        f"tooltip.{MODID}.suit.speed": ("Speed boost: +%s%%", "Прибавка скорости: +%s%%"),
        f"tooltip.{MODID}.suit.jump": ("Jump boost: +%s%%", "Прибавка прыжка: +%s%%"),
        f"tooltip.{MODID}.suit.strength": ("Strength: +%s damage", "Сила: +%s урона"),
        f"tooltip.{MODID}.suit.climb": ("Wall climbing: YES", "Хождение по стенам: ДА"),
        f"tooltip.{MODID}.suit.flight": ("Hover thrusters: YES", "Парящие двигатели: ДА"),
        f"tooltip.{MODID}.suit.arms": ("Octo Arms: 8 servo limbs", "Окто-руки: 8 серво-лап"),
        f"tooltip.{MODID}.suit.setbonus": ("Full set bonus: %s", "Бонус полного комплекта: %s"),
        f"tooltip.{MODID}.needs_fuel": ("Needs fuel in the burner slot", "Нужно топливо в слоте горелки"),
        f"tooltip.{MODID}.weapon.cost": ("Cost per shot: %s PU", "Расход за выстрел: %s ЕМ"),
        f"tooltip.{MODID}.cell": ("Right-click a mech to inject charge", "ПКМ по меху — передать заряд"),
        f"tooltip.{MODID}.hold_shift": ("Hold SHIFT for stats", "Удерживайте SHIFT для характеристик"),

        f"key.{MODID}.category": ("Octo Arms — Mechs", "Окто-Армс — Мехи"),
        f"key.{MODID}.fire_arms": ("Fire Octo Arms cannon", "Выстрел пушками «Окто-Армс»"),
        f"key.{MODID}.toggle_thrusters": ("Toggle hover thrusters", "Вкл/выкл парящие двигатели"),
        f"key.{MODID}.toggle_visor": ("Toggle visor night vision", "Вкл/выкл ночное зрение визора"),
        f"key.{MODID}.toggle_cling": ("Toggle wall cling", "Вкл/выкл прилипание к стенам"),

        f"octoarms.msg.no_charge": ("Mech cells are empty — find a Charging Dock",
                                     "Энергоячейки мехов пусты — найдите зарядный док"),
        f"octoarms.msg.overheat": ("Weapon overheated!", "Оружие перегрето!"),
        f"octoarms.msg.thrusters_on": ("Hover thrusters engaged", "Парящие двигатели включены"),
        f"octoarms.msg.thrusters_off": ("Hover thrusters offline", "Парящие двигатели выключены"),
        f"octoarms.msg.visor_on": ("Visor night vision ON", "Ночное зрение визора ВКЛ"),
        f"octoarms.msg.visor_off": ("Visor night vision OFF", "Ночное зрение визора ВЫКЛ"),
        f"octoarms.msg.cling_on": ("Wall cling enabled", "Прилипание к стенам включено"),
        f"octoarms.msg.cling_off": ("Wall cling disabled", "Прилипание к стенам выключено"),
        f"octoarms.msg.suit_required": ("Full mech suit required", "Нужен полный комплект меха"),

        f"subtitles.{MODID}.mech_step": ("Mech stomps", "Мех топает"),
        f"subtitles.{MODID}.mech_climb": ("Mech claws scrape", "Когти меха скрежещут"),
        f"subtitles.{MODID}.mech_jump": ("Hydraulics hiss", "Гидравлика шипит"),
        f"subtitles.{MODID}.plasma": ("Plasma crackles", "Плазма трещит"),
        f"subtitles.{MODID}.railgun": ("Railgun booms", "Рельсотрон грохочет"),
        f"subtitles.{MODID}.press": ("Press slams", "Пресс грохает"),
        f"subtitles.{MODID}.charge": ("Cells charging", "Ячейки заряжаются"),
        f"subtitles.{MODID}.mech_land": ("Mech lands heavily", "Мех тяжело приземляется"),

        f"death.attack.{MODID}.plasma": ("%1$s was melted by plasma", "%1$s расплавлен плазмой"),
        f"death.attack.{MODID}.rail": ("%1$s was pierced by a rail slug", "%1$s прошит болванкой"),
    }
    entries.update(extra)
    return entries
