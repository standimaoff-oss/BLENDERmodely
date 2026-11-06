package com.octoarms.menu;

import com.octoarms.blockentity.MachineBlockEntity;
import com.octoarms.registry.OctoMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class PressMenu extends AbstractContainerMenu {

    public static final int IMAGE_WIDTH = 176;
    public static final int IMAGE_HEIGHT = 184;

    private final MachineBlockEntity blockEntity;

    public static PressMenu fromNetwork(int windowId, Inventory inventory, FriendlyByteBuf buf) {
        return new PressMenu(windowId, inventory, buf.readBlockPos());
    }

    public PressMenu(int windowId, Inventory inventory, BlockPos pos) {
        super(OctoMenuTypes.PRESS.get(), windowId);
        this.blockEntity = (MachineBlockEntity) inventory.player.level().getBlockEntity(pos);
        checkContainerSize(blockEntity, 5);

        for (int i = 0; i < 3; i++) {
            addSlot(new SlotItemHandler(blockEntityInventory(), i, 44, 17 + i * 18));
        }
        addSlot(new SlotItemHandler(blockEntityInventory(), 3, 44, 71));
        addSlot(new OutputSlot(blockEntityInventory(), 4, 116, 44));

        addPlayerInventory(inventory, 102);
        addDataSlots(blockEntity.containerData());
    }

    protected net.minecraftforge.items.IItemHandler blockEntityInventory() {
        return blockEntity.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("machine has no item handler"));
    }

    protected void addPlayerInventory(Inventory inventory, int top) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, top + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, top + 58));
        }
    }

    public MachineBlockEntity blockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();
        int machineSlots = 5;
        if (index < machineSlots) {
            if (!this.moveItemStackTo(original, machineSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(original, 0, machineSlots - 1, false)) {
            return ItemStack.EMPTY;
        }
        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    /** Output-only slot. */
    public static class OutputSlot extends SlotItemHandler {
        public OutputSlot(net.minecraftforge.items.IItemHandler handler, int index, int x, int y) {
            super(handler, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
