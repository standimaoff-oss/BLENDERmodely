package com.octoarms.mech;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * ArmorMaterial wrapper around a {@link MechSuitTier}. The material name is the
 * tier id, which makes Forge resolve the armor texture to
 * {@code octoarms:textures/models/armor/<tier>_layer_N.png}.
 */
public class MechArmorMaterial implements ArmorMaterial {

    private final MechSuitTier tier;
    private Ingredient repair;

    public MechArmorMaterial(MechSuitTier tier) {
        this.tier = tier;
    }

    public MechSuitTier tier() {
        return tier;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return tier.durabilityFor(type);
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return tier.defenseFor(type);
    }

    @Override
    public int getEnchantmentValue() {
        return tier.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return tier.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        if (repair == null) {
            repair = Ingredient.of(switch (tier) {
                case COPPER -> net.minecraft.world.item.Items.COPPER_INGOT;
                case GOLD -> net.minecraft.world.item.Items.GOLD_INGOT;
                case DIAMOND -> net.minecraft.world.item.Items.DIAMOND;
                case NETHERITE, SPIDER -> net.minecraft.world.item.Items.NETHERITE_INGOT;
                default -> net.minecraft.world.item.Items.IRON_INGOT;
            });
        }
        return repair;
    }

    @Override
    public String getName() {
        return "octoarms:" + tier.id;
    }

    @Override
    public float getToughness() {
        return tier.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return tier.knockbackResistance;
    }
}
