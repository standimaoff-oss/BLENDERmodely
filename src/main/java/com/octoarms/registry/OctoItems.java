package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.item.ArmCannonItem;
import com.octoarms.item.MechArmorItem;
import com.octoarms.item.PlasmaBladeItem;
import com.octoarms.item.PowerCellItem;
import com.octoarms.item.RailgunItem;
import com.octoarms.mech.MechSuitTier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class OctoItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OctoArms.MODID);

    public static final Map<String, RegistryObject<Item>> COMPONENTS = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<MechArmorItem>> ARMOR = new LinkedHashMap<>();

    private static final String[] COMPONENT_NAMES = {
            "copper_plate", "iron_plate", "gold_plate", "diamond_plate", "netherite_plate", "obsidian_plate",
            "copper_gear", "iron_gear", "diamond_gear", "netherite_gear",
            "actuator", "heavy_actuator", "hydraulic_piston", "circuit_board", "advanced_circuit",
            "power_core", "nether_power_core", "reinforced_frame", "silk_mesh", "octo_arm", "thruster_module",
            "enriched_coal", "energy_orb", "heavy_slug",
    };

    static {
        for (String name : COMPONENT_NAMES) {
            COMPONENTS.put(name, ITEMS.register(name, () -> new Item(base())));
        }
    }

    // -- power cells ------------------------------------------------------- //
    public static final RegistryObject<PowerCellItem> POWER_CELL = ITEMS.register("power_cell",
            () -> new PowerCellItem(20_000));
    public static final RegistryObject<PowerCellItem> ADVANCED_POWER_CELL = ITEMS.register("advanced_power_cell",
            () -> new PowerCellItem(60_000));
    public static final RegistryObject<PowerCellItem> NETHER_POWER_CELL = ITEMS.register("nether_power_cell",
            () -> new PowerCellItem(200_000));

    // -- weapons ----------------------------------------------------------- //
    public static final RegistryObject<PlasmaBladeItem> PLASMA_BLADE = ITEMS.register("plasma_blade",
            PlasmaBladeItem::new);
    public static final RegistryObject<ArmCannonItem> ARM_CANNON = ITEMS.register("arm_cannon",
            ArmCannonItem::new);
    public static final RegistryObject<RailgunItem> RAILGUN = ITEMS.register("railgun",
            RailgunItem::new);

    // -- mech suits -------------------------------------------------------- //
    static {
        for (MechSuitTier tier : MechSuitTier.values()) {
            ARMOR.put(tier.id + "_helmet", ITEMS.register(tier.id + "_helmet",
                    () -> new MechArmorItem(tier, ArmorItem.Type.HELMET)));
            ARMOR.put(tier.id + "_chestplate", ITEMS.register(tier.id + "_chestplate",
                    () -> new MechArmorItem(tier, ArmorItem.Type.CHESTPLATE)));
            ARMOR.put(tier.id + "_leggings", ITEMS.register(tier.id + "_leggings",
                    () -> new MechArmorItem(tier, ArmorItem.Type.LEGGINGS)));
            ARMOR.put(tier.id + "_boots", ITEMS.register(tier.id + "_boots",
                    () -> new MechArmorItem(tier, ArmorItem.Type.BOOTS)));
        }
    }

    // -- block items ------------------------------------------------------- //
    public static final RegistryObject<BlockItem> HYDRAULIC_PRESS = ITEMS.register("hydraulic_press",
            () -> new BlockItem(OctoBlocks.HYDRAULIC_PRESS.get(), base()));
    public static final RegistryObject<BlockItem> CHARGING_DOCK = ITEMS.register("charging_dock",
            () -> new BlockItem(OctoBlocks.CHARGING_DOCK.get(), base()));
    public static final RegistryObject<BlockItem> MECH_ASSEMBLER = ITEMS.register("mech_assembler",
            () -> new BlockItem(OctoBlocks.MECH_ASSEMBLER.get(), base()));
    public static final RegistryObject<BlockItem> STEEL_CASING = ITEMS.register("steel_casing",
            () -> new BlockItem(OctoBlocks.STEEL_CASING.get(), base()));
    public static final RegistryObject<BlockItem> COIL_BLOCK = ITEMS.register("coil_block",
            () -> new BlockItem(OctoBlocks.COIL_BLOCK.get(), base()));

    public static Item.Properties base() {
        return new Item.Properties();
    }

    public static Item component(String name) {
        RegistryObject<Item> obj = COMPONENTS.get(name);
        return obj == null ? null : obj.get();
    }

    public static MechArmorItem armor(MechSuitTier tier, String piece) {
        RegistryObject<MechArmorItem> obj = ARMOR.get(tier.id + "_" + piece);
        return obj == null ? null : obj.get();
    }

    private OctoItems() {
    }
}
