package com.octoarms.registry;

import com.octoarms.OctoArms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Block tags drive the surface physics of the Spider Mech; item tags group
 * suit pieces and batteries for recipes/datapacks.
 */
public final class OctoTags {

    public static final TagKey<Block> SLIPPERY = blockTag("slippery");
    public static final TagKey<Block> STICKY = blockTag("sticky");
    public static final TagKey<Block> LOOSE = blockTag("loose");
    public static final TagKey<Block> METALLIC = blockTag("metallic");
    public static final TagKey<Block> NO_GRIP = blockTag("no_grip");

    public static final TagKey<Item> MECH_ARMOR = itemTag("mech_armor");
    public static final TagKey<Item> POWER_CELLS = itemTag("power_cells");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(OctoArms.MODID, name));
    }

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(OctoArms.MODID, name));
    }

    private OctoTags() {
    }
}
