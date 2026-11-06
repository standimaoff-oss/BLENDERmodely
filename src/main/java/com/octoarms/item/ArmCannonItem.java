package com.octoarms.item;

import com.octoarms.OctoConfig;
import com.octoarms.entity.PlasmaBoltEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Hand-held plasma projector. Fires the same bolts as integrated mech cannons.
 */
public class ArmCannonItem extends Item implements IChargeableItem {

    public static final int CAPACITY = 24_000;
    public static final int COST_PER_SHOT = 400;

    public ArmCannonItem() {
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (player.getCooldowns().isCoolingDown(this)) {
            return InteractionResultHolder.fail(stack);
        }
        if (ChargeUtils.overheated(stack)) {
            player.displayClientMessage(Component.translatable("octoarms.msg.overheat"), true);
            return InteractionResultHolder.fail(stack);
        }
        if (!ChargeUtils.extract(stack, COST_PER_SHOT)) {
            player.displayClientMessage(Component.translatable("octoarms.msg.no_charge"), true);
            return InteractionResultHolder.fail(stack);
        }
        ChargeUtils.addHeat(stack, 14.0F);
        PlasmaBoltEntity bolt = new PlasmaBoltEntity(level, player,
                OctoConfig.PLASMA_DAMAGE.get().floatValue());
        bolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.7F, 1.0F);
        level.addFreshEntity(bolt);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.7F, 1.3F);
        player.getCooldowns().addCooldown(this, 12);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide) {
            ChargeUtils.cool(stack, 0.6F);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ChargeUtils.stored(stack) > CAPACITY * 3 / 4;
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
        return 0x8CF0FF;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}
