package com.octoarms.menu;

import com.octoarms.blockentity.MachineBlockEntity;
import com.octoarms.blockentity.MechAssemblerBlockEntity;
import com.octoarms.registry.OctoMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class AssemblerMenu extends AbstractContainerMenu implements MachineMenu {

    public static final int IMAGE_WIDTH = 176;
    public static final int IMAGE_HEIGHT = 222;

    private final MachineBlockEntity blockEntity;

    public static AssemblerMenu fromNetwork(int windowId, Inventory inventory, FriendlyByteBuf buf) {
        return new AssemblerMenu(windowId, inventory, buf.readBlockPos());
    }

    public AssemblerMenu(int windowId, Inventory inventory, BlockPos pos) {
        super(OctoMenuTypes.ASSEMBLER.get(), windowId);
        this.blockEntity = (MachineBlockEntity) inventory.player.level().getBlockEntity(pos);
        checkContainerSize(blockEntity, MechAssemblerBlockEntity.SLOTS);

        var handler = blockEntity.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("machine has no item handler"));
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                addSlot(new SlotItemHandler(handler, row * 5 + col, 8 + col * 18, 17 + row * 18));
            }
        }
        addSlot(new SlotItemHandler(handler, MechAssemblerBlockEntity.FUEL, 116, 80));
        addSlot(new PressMenu.OutputSlot(handler, MechAssemblerBlockEntity.OUTPUT, 116, 44));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 198));
        }
        addDataSlots(blockEntity.containerData());
    }

    public MachineBlockEntity blockEntity() {
        return blockEntity;
    }

    @Override
    public ContainerData machineData() {
        return blockEntity.containerData();
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
        int machineSlots = MechAssemblerBlockEntity.SLOTS;
        if (index < machineSlots) {
            if (!this.moveItemStackTo(original, machineSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(original, 0, MechAssemblerBlockEntity.GRID, false)) {
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
