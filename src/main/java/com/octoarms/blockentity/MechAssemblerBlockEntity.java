package com.octoarms.blockentity;

import com.octoarms.OctoConfig;
import com.octoarms.recipe.AssemblyRecipe;
import com.octoarms.recipe.RecipeMatcher;
import com.octoarms.registry.OctoBlockEntities;
import com.octoarms.registry.OctoRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Mech assembler: 5x5 component grid, fuel-driven PU buffer and an energy cost
 * per recipe. This is where whole exosuits come together.
 */
public class MechAssemblerBlockEntity extends MachineBlockEntity {

    public static final int GRID = 25;
    public static final int FUEL = GRID;
    public static final int OUTPUT = GRID + 1;
    public static final int SLOTS = GRID + 2;

    public MechAssemblerBlockEntity(BlockPos pos, BlockState state) {
        super(OctoBlockEntities.MECH_ASSEMBLER.get(), pos, state, SLOTS, OctoConfig.ASSEMBLER_BUFFER.get());
    }

    @Override
    public int fuelSlot() {
        return FUEL;
    }

    @Override
    public int outputSlot() {
        return OUTPUT;
    }

    @Override
    public int firstInputSlot() {
        return 0;
    }

    @Override
    public int lastInputSlot() {
        return GRID - 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == OUTPUT) {
            return false;
        }
        if (slot == FUEL) {
            return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null) > 0;
        }
        return true;
    }

    public Optional<AssemblyRecipe> currentRecipe() {
        if (level == null) {
            return Optional.empty();
        }
        SimpleContainer grid = new SimpleContainer(GRID);
        for (int i = 0; i < GRID; i++) {
            grid.setItem(i, inventory.getStackInSlot(i));
        }
        return level.getRecipeManager().getRecipeFor(OctoRecipeTypes.ASSEMBLY.get(), grid, level);
    }

    private boolean canOutput(AssemblyRecipe recipe) {
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack current = inventory.getStackInSlot(OUTPUT);
        if (current.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameTags(current, result)) {
            return false;
        }
        return current.getCount() + result.getCount() <= current.getMaxStackSize();
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        boolean working = false;
        Optional<AssemblyRecipe> recipe = currentRecipe();

        if (recipe.isPresent() && canOutput(recipe.get())) {
            progressTotal = recipe.get().energy();
            // fuel -> PU buffer
            if (energy < energyCapacity && burnFuel()) {
                energy = Math.min(energyCapacity, energy + OctoConfig.PU_PER_BURN_TICK.get() * 4);
            }
            int drain = Math.min(OctoConfig.ASSEMBLER_DRAIN_RATE.get(), energy);
            if (drain > 0) {
                energy -= drain;
                progress += drain;
                working = true;
                if (progress >= progressTotal) {
                    craft(recipe.get());
                    progress = 0;
                }
            }
        } else {
            progress = 0;
        }

        setLit(working);
        if (working) {
            setChanged();
        }
    }

    private void craft(AssemblyRecipe recipe) {
        SimpleContainer grid = new SimpleContainer(GRID);
        for (int i = 0; i < GRID; i++) {
            grid.setItem(i, inventory.getStackInSlot(i));
        }
        RecipeMatcher.consume(grid, recipe.inputs());
        for (int i = 0; i < GRID; i++) {
            inventory.setStackInSlot(i, grid.getItem(i));
        }
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack current = inventory.getStackInSlot(OUTPUT);
        if (current.isEmpty()) {
            inventory.setStackInSlot(OUTPUT, result);
        } else {
            current.grow(result.getCount());
            inventory.setStackInSlot(OUTPUT, current);
        }
        level.playSound(null, worldPosition, net.minecraft.sounds.SoundEvents.ANVIL_USE,
                net.minecraft.sounds.SoundSource.BLOCKS, 0.5F, 1.2F);
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.octoarms.mech_assembler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new com.octoarms.menu.AssemblerMenu(windowId, playerInv, worldPosition);
    }
}
