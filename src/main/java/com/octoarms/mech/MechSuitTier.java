package com.octoarms.mech;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;

/**
 * The six exosuit families of Octo Arms, from cheap copper scout frames up to
 * the wall-crawling Spider Mech.
 */
public enum MechSuitTier {

    COPPER("copper_mech", 20_000,
            new int[]{2, 5, 4, 2}, 0.0F, 0.0F, 16, 12,
            0.04F, 1.0F, 0.10F,
            false, false, false, false, false,
            4, 0, 2, 40,
            SoundEvents.ARMOR_EQUIP_IRON),
    IRON("iron_mech", 50_000,
            new int[]{3, 7, 5, 2}, 0.5F, 0.0F, 22, 14,
            0.06F, 2.0F, 0.15F,
            false, false, false, false, false,
            5, 0, 2, 50,
            SoundEvents.ARMOR_EQUIP_IRON),
    GOLD("gold_mech", 35_000,
            new int[]{2, 6, 4, 2}, 0.0F, 0.0F, 12, 30,
            0.16F, 1.0F, 0.45F,
            false, false, false, false, false,
            4, 0, 3, 40,
            SoundEvents.ARMOR_EQUIP_GOLD),
    DIAMOND("diamond_mech", 120_000,
            new int[]{4, 9, 6, 3}, 2.5F, 0.05F, 32, 18,
            0.09F, 4.0F, 0.25F,
            false, true, false, false, true,
            6, 22, 2, 60,
            SoundEvents.ARMOR_EQUIP_DIAMOND),
    NETHERITE("netherite_mech", 240_000,
            new int[]{5, 11, 8, 4}, 4.0F, 0.15F, 42, 20,
            0.11F, 6.0F, 0.30F,
            false, true, true, false, true,
            7, 26, 3, 70,
            SoundEvents.ARMOR_EQUIP_NETHERITE),
    SPIDER("spider_mech", 180_000,
            new int[]{4, 10, 7, 3}, 3.0F, 0.10F, 36, 22,
            0.17F, 5.0F, 0.60F,
            true, false, true, true, true,
            8, 0, 2, 50,
            SoundEvents.ARMOR_EQUIP_NETHERITE);

    /** defense values in HELMET, CHEST, LEGGINGS, FEET order */
    private final int[] defense;
    public final String id;
    public final int capacity;
    public final float toughness;
    public final float knockbackResistance;
    public final int durabilityMultiplier;
    public final int enchantability;
    public final float speedBonus;
    public final float attackBonus;
    public final float jumpBonus;
    public final boolean canClimb;
    public final boolean canFly;
    public final boolean integratedCannon;
    public final boolean integratedBlade;
    public final boolean visorNightVision;
    public final int costClimb;
    public final int costFly;
    public final int costSprint;
    public final int costJump;
    public final SoundEvent equipSound;

    MechSuitTier(String id, int capacity, int[] defense, float toughness, float knockbackResistance,
                 int durabilityMultiplier, int enchantability,
                 float speedBonus, float attackBonus, float jumpBonus,
                 boolean canClimb, boolean canFly, boolean integratedCannon, boolean integratedBlade,
                 boolean visorNightVision,
                 int costClimb, int costFly, int costSprint, int costJump,
                 SoundEvent equipSound) {
        this.id = id;
        this.capacity = capacity;
        this.defense = defense;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.durabilityMultiplier = durabilityMultiplier;
        this.enchantability = enchantability;
        this.speedBonus = speedBonus;
        this.attackBonus = attackBonus;
        this.jumpBonus = jumpBonus;
        this.canClimb = canClimb;
        this.canFly = canFly;
        this.integratedCannon = integratedCannon;
        this.integratedBlade = integratedBlade;
        this.visorNightVision = visorNightVision;
        this.costClimb = costClimb;
        this.costFly = costFly;
        this.costSprint = costSprint;
        this.costJump = costJump;
        this.equipSound = equipSound;
    }

    public static MechSuitTier byId(String id) {
        for (MechSuitTier tier : values()) {
            if (tier.id.equals(id)) {
                return tier;
            }
        }
        return COPPER;
    }

    public int defenseFor(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> defense[0];
            case CHEST -> defense[1];
            case LEGGINGS -> defense[2];
            default -> defense[3];
        };
    }

    public int durabilityFor(ArmorItem.Type type) {
        int base = switch (type) {
            case HELMET -> 13;
            case CHEST -> 16;
            case LEGGINGS -> 15;
            default -> 11;
        };
        return base * durabilityMultiplier;
    }

    public String translationKey() {
        return "item.octoarms." + id + "_chestplate";
    }

    public String repairItem() {
        return switch (this) {
            case COPPER -> Items.COPPER_INGOT.toString();
            case GOLD -> Items.GOLD_INGOT.toString();
            case DIAMOND -> Items.DIAMOND.toString();
            default -> Items.IRON_INGOT.toString();
        };
    }
}
