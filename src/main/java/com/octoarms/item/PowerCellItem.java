package com.octoarms.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Swappable battery packs. Cells in the inventory slowly feed equipped suits.
 */
public class PowerCellItem extends Item implements IChargeableItem {

    private final int capacity;

    public PowerCellItem(int capacity) {
        super(new Item.Properties().stacksTo(1));
        this.capacity = capacity;
    }

    public PowerCellItem() {
        this(20_000);
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return capacity;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ChargeUtils.stored(stack) > 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity,
                              int slot, boolean selected) {
        if (!level.isClientSide) {
            ChargeUtils.cool(stack, 1.0F);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ChargeUtils.appendChargeTooltip(stack, tooltip);
        tooltip.add(Component.translatable("tooltip.octoarms.cell"));
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
        return Math.round(13.0F * ChargeUtils.stored(stack) / (float) capacity);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x5FE3C8;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack);
    }

    public static boolean isFull(ItemStack stack) {
        return ChargeUtils.charge(stack).map(c -> c.isFull()).orElse(false);
    }

    public static void giveCharge(Player player, ItemStack stack, int amount) {
        ChargeUtils.charge(stack).ifPresent(c -> c.receive(amount, false));
    }
}
