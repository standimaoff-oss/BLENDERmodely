package com.octoarms.item;

import com.octoarms.OctoConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A mono-blade wrapped in a plasma field. While charged it burns through
 * armour; every swing vents cell power.
 */
public class PlasmaBladeItem extends SwordItem implements IChargeableItem {

    public static final int CAPACITY = 12_000;
    public static final int COST_PER_HIT = 150;

    public PlasmaBladeItem() {
        super(Tiers.DIAMOND, 5, -2.2F, new Properties().stacksTo(1).durability(0));
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return CAPACITY;
    }

    @Override
    public int getActionCost(ItemStack stack) {
        return COST_PER_HIT;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ChargeUtils.stored(stack) > CAPACITY / 2;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide && ChargeUtils.extract(stack, COST_PER_HIT)) {
            ChargeUtils.addHeat(stack, 6.0F);
            target.setRemainingFireTicks(40);
            target.hurt(target.level().damageSources().magic(), OctoConfig.PLASMA_BLADE_BONUS.get());
            for (int i = 0; i < 8; i++) {
                target.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                        target.getX(), target.getY(0.6D), target.getZ(),
                        (target.level().getRandom().nextDouble() - 0.5D) * 0.3D,
                        target.level().getRandom().nextDouble() * 0.25D,
                        (target.level().getRandom().nextDouble() - 0.5D) * 0.3D);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide) {
            ChargeUtils.cool(stack, 0.5F);
        }
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ChargeUtils.appendChargeTooltip(stack, tooltip);
        tooltip.add(Component.translatable("tooltip.octoarms.weapon.cost", ChargeUtils.format(COST_PER_HIT)));
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        return ChargeUtils.writeShare(stack, super.getShareTag(stack));
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
        super.readShareTag(stack, tag);
        ChargeUtils.readShare(stack, tag);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * ChargeUtils.stored(stack) / (float) CAPACITY);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x8CF0FF;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    public static boolean canSwingCharged(Player player, ItemStack stack) {
        return ChargeUtils.stored(stack) >= COST_PER_HIT;
    }
}
