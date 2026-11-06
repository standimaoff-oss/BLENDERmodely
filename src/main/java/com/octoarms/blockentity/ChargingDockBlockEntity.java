package com.octoarms.blockentity;

import com.octoarms.OctoConfig;
import com.octoarms.mech.MechCapabilities;
import com.octoarms.mech.MechCharge;
import com.octoarms.registry.OctoBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Charging dock: burns solid fuel into Power Units and pumps them into the
 * mech piece / cell / weapon placed in the target slot.
 */
public class ChargingDockBlockEntity extends MachineBlockEntity {

    public static final int SLOTS = 2;
    private static final int FUEL = 0;
    private static final int TARGET = 1;
    public static final int BUFFER = 100_000;

    public ChargingDockBlockEntity(BlockPos pos, BlockState state) {
        super(OctoBlockEntities.CHARGING_DOCK.get(), pos, state, SLOTS, BUFFER);
    }

    @Override
    public int fuelSlot() {
        return FUEL;
    }

    @Override
    public int outputSlot() {
        return TARGET;
    }

    @Override
    public int firstInputSlot() {
        return TARGET;
    }

    @Override
    public int lastInputSlot() {
        return TARGET;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == FUEL) {
            return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null) > 0;
        }
        return stack.getCapability(MechCapabilities.CHARGE).isPresent();
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        boolean working = false;

        // fuel -> internal buffer
        if (energy < energyCapacity && burnFuel()) {
            energy = Math.min(energyCapacity, energy + OctoConfig.PU_PER_BURN_TICK.get());
            working = true;
        }

        // buffer -> target item
        ItemStack target = inventory.getStackInSlot(TARGET);
        var charge = target.getCapability(MechCapabilities.CHARGE).resolve();
        if (charge.isPresent() && energy > 0) {
            MechCharge cell = charge.get();
            int room = cell.getCapacity() - cell.getStored();
            int moved = Math.min(Math.min(room, energy), OctoConfig.CHARGER_TRANSFER_RATE.get());
            if (moved > 0) {
                energy -= moved;
                cell.receive(moved, false);
                working = true;
            }
        }

        progressTotal = 100;
        progress = charge.map(c -> Math.round(c.getFraction() * 100.0F)).orElse(0);

        setLit(working);
        if (working) {
            setChanged();
            if (level.getGameTime() % 20 == 0) {
                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BEACON_AMBIENT,
                        net.minecraft.sounds.SoundSource.BLOCKS, 0.15F, 1.6F);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.octoarms.charging_dock");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new com.octoarms.menu.ChargerMenu(windowId, playerInv, worldPosition);
    }
}
