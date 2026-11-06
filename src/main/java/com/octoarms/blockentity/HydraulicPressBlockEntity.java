package com.octoarms.blockentity;

import com.octoarms.recipe.PressingRecipe;
import com.octoarms.registry.OctoBlockEntities;
import com.octoarms.registry.OctoMenuTypes;
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
 * Hydraulic press: weighted inputs -> plates, gears and modules.
 */
public class HydraulicPressBlockEntity extends MachineBlockEntity {

    public static final int SLOTS = 5;
    private static final int INPUT_FIRST = 0;
    private static final int INPUT_LAST = 2;
    private static final int FUEL = 3;
    private static final int OUTPUT = 4;

    public HydraulicPressBlockEntity(BlockPos pos, BlockState state) {
        super(OctoBlockEntities.HYDRAULIC_PRESS.get(), pos, state, SLOTS, 0);
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
        return INPUT_FIRST;
    }

    @Override
    public int lastInputSlot() {
        return INPUT_LAST;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != OUTPUT && slot != FUEL
                || (slot == FUEL && net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null) > 0);
    }

    public Optional<PressingRecipe> currentRecipe() {
        if (level == null) {
            return Optional.empty();
        }
        SimpleContainer inputs = new SimpleContainer(INPUT_LAST - INPUT_FIRST + 1);
        for (int i = INPUT_FIRST; i <= INPUT_LAST; i++) {
            inputs.setItem(i - INPUT_FIRST, inventory.getStackInSlot(i));
        }
        return level.getRecipeManager().getRecipeFor(OctoRecipeTypes.PRESSING.get(), inputs, level);
    }

    private boolean canOutput(PressingRecipe recipe) {
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

    public static void serverTickStatic(Level level, BlockPos pos, BlockState state, HydraulicPressBlockEntity be) {
        be.serverTick(level, pos, state);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        boolean working = false;
        Optional<PressingRecipe> recipe = currentRecipe();
        if (recipe.isPresent() && canOutput(recipe.get())) {
            progressTotal = recipe.get().getProcessTime();
            if (burnFuel()) {
                progress++;
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
        if (working || litTime > 0) {
            setChanged();
        }
    }

    private void craft(PressingRecipe recipe) {
        SimpleContainer inputs = new SimpleContainer(INPUT_LAST - INPUT_FIRST + 1);
        for (int i = INPUT_FIRST; i <= INPUT_LAST; i++) {
            inputs.setItem(i - INPUT_FIRST, inventory.getStackInSlot(i));
        }
        com.octoarms.recipe.RecipeMatcher.consume(inputs, recipe.inputs());
        for (int i = INPUT_FIRST; i <= INPUT_LAST; i++) {
            inventory.setStackInSlot(i, inputs.getItem(i - INPUT_FIRST));
        }
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack current = inventory.getStackInSlot(OUTPUT);
        if (current.isEmpty()) {
            inventory.setStackInSlot(OUTPUT, result);
        } else {
            current.grow(result.getCount());
            inventory.setStackInSlot(OUTPUT, current);
        }
        level.playSound(null, worldPosition, net.minecraft.sounds.SoundEvents.PISTON_CONTRACT,
                net.minecraft.sounds.SoundSource.BLOCKS, 0.4F, 0.7F);
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.octoarms.hydraulic_press");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new com.octoarms.menu.PressMenu(windowId, playerInv, worldPosition);
    }
}
