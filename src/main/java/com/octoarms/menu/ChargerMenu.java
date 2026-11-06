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

public class ChargerMenu extends AbstractContainerMenu {

    public static final int IMAGE_WIDTH = 176;
    public static final int IMAGE_HEIGHT = 184;

    private final MachineBlockEntity blockEntity;

    public static ChargerMenu fromNetwork(int windowId, Inventory inventory, FriendlyByteBuf buf) {
        return new ChargerMenu(windowId, inventory, buf.readBlockPos());
    }

    public ChargerMenu(int windowId, Inventory inventory, BlockPos pos) {
        super(OctoMenuTypes.CHARGER.get(), windowId);
        this.blockEntity = (MachineBlockEntity) inventory.player.level().getBlockEntity(pos);
        checkContainerSize(blockEntity, 2);

        var handler = blockEntity.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("machine has no item handler"));
        addSlot(new SlotItemHandler(handler, 0, 44, 53));
        addSlot(new SlotItemHandler(handler, 1, 80, 34));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 102 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 160));
        }
        addDataSlots(blockEntity.containerData());
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
        if (index < 2) {
            if (!this.moveItemStackTo(original, 2, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(original, 0, 2, false)) {
            return ItemStack.EMPTY;
        }
        if (original.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }
}
