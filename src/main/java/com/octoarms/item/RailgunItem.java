package com.octoarms.item;

import com.octoarms.OctoConfig;
import com.octoarms.entity.RailSlugEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Charge-up magnetic accelerator. Hold to spool the rails, release to fire a
 * piercing slug. Devours cell power and heat.
 */
public class RailgunItem extends Item implements IChargeableItem {

    public static final int CAPACITY = 40_000;
    public static final int COST_PER_SHOT = 3_000;
    public static final int CHARGE_TICKS = 24;

    public RailgunItem() {
        super(new Properties().stacksTo(1).durability(0));
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return CAPACITY;
    }

    @Override
    public int getActionCost(ItemStack stack) {
        return COST_PER_SHOT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (ChargeUtils.overheated(stack) || !ChargeUtils.charge(stack).map(c -> c.has(COST_PER_SHOT)).orElse(false)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable(
                        ChargeUtils.overheated(stack) ? "octoarms.msg.overheat" : "octoarms.msg.no_charge"), true);
            }
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        int charged = getUseDuration(stack) - timeLeft;
        if (charged < 10 || level.isClientSide) {
            return;
        }
        if (!ChargeUtils.extract(stack, COST_PER_SHOT)) {
            return;
        }
        ChargeUtils.addHeat(stack, 34.0F);
        float power = Math.min(1.0F, charged / (float) CHARGE_TICKS);
        RailSlugEntity slug = new RailSlugEntity(level, user,
                OctoConfig.RAILGUN_DAMAGE.get().floatValue() * (0.5F + 0.5F * power));
        slug.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 2.6F + 1.4F * power, 0.4F);
        level.addFreshEntity(slug);
        // recoil
        user.push(-user.getLookAngle().x * 0.45D, 0.08D, -user.getLookAngle().z * 0.45D);
        user.hurtMarked = true;
        level.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.8F, 1.9F);
        level.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 0.9F, 0.5F);
        if (user instanceof Player player) {
            player.getCooldowns().addCooldown(this, 30);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide) {
            ChargeUtils.cool(stack, 0.45F);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ChargeUtils.appendChargeTooltip(stack, tooltip);
        tooltip.add(Component.translatable("tooltip.octoarms.weapon.cost", ChargeUtils.format(COST_PER_SHOT)));
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
        return 0xB4FF3C;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}
