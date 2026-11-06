package com.octoarms.blockentity;

import com.octoarms.block.MachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base of all three machines: fuel burner, item handler, ContainerData sync
 * and item-handler capability.
 */
public abstract class MachineBlockEntity extends BlockEntity implements Container, MenuProvider {

    protected final ItemStackHandler inventory;
    protected final LazyOptional<IItemHandler> handler;

    protected int litTime;
    protected int litTotal;
    protected int progress;
    protected int progressTotal;
    protected int energy;
    protected int energyCapacity;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> litTime;
                case 1 -> litTotal;
                case 2 -> progress;
                case 3 -> progressTotal;
                case 4 -> energy;
                case 5 -> energyCapacity;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> litTime = value;
                case 1 -> litTotal = value;
                case 2 -> progress = value;
                case 3 -> progressTotal = value;
                case 4 -> energy = value;
                case 5 -> energyCapacity = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    protected MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slots, int energyCapacity) {
        super(type, pos, state);
        this.energyCapacity = energyCapacity;
        this.inventory = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return MachineBlockEntity.this.canPlaceItem(slot, stack);
            }
        };
        this.handler = LazyOptional.of(() -> this.inventory);
    }

    public ContainerData containerData() {
        return data;
    }

    public abstract int fuelSlot();

    public abstract int outputSlot();

    public abstract int firstInputSlot();

    public abstract int lastInputSlot();

    /** Server-side processing step. */
    public abstract void serverTick(Level level, BlockPos pos, BlockState state);

    // -- fuel --------------------------------------------------------------- //

    protected boolean burnFuel() {
        if (litTime > 0) {
            litTime--;
            return true;
        }
        ItemStack fuel = inventory.getStackInSlot(fuelSlot());
        int burn = ForgeHooks.getBurnTime(fuel, null);
        if (burn > 0) {
            litTime = burn;
            litTotal = burn;
            fuel.shrink(1);
            inventory.setStackInSlot(fuelSlot(), fuel);
            setChanged();
            return true;
        }
        return false;
    }

    protected boolean hasFuel() {
        return litTime > 0 || ForgeHooks.getBurnTime(inventory.getStackInSlot(fuelSlot()), null) > 0;
    }

    protected void setLit(boolean lit) {
        if (level != null && getBlockState().hasProperty(MachineBlock.LIT)
                && getBlockState().getValue(MachineBlock.LIT) != lit) {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(MachineBlock.LIT, lit));
        }
    }

    public int getRedstoneSignal() {
        if (progressTotal <= 0) {
            return litTime > 0 ? 1 : 0;
        }
        return 1 + (int) (14.0F * progress / progressTotal);
    }

    // -- nbt / caps ---------------------------------------------------------- //

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inv", inventory.serializeNBT());
        tag.putInt("litTime", litTime);
        tag.putInt("litTotal", litTotal);
        tag.putInt("progress", progress);
        tag.putInt("progressTotal", progressTotal);
        tag.putInt("energy", energy);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inv"));
        litTime = tag.getInt("litTime");
        litTotal = tag.getInt("litTotal");
        progress = tag.getInt("progress");
        progressTotal = tag.getInt("progressTotal");
        energy = tag.getInt("energy");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    // -- Container view over the handler ------------------------------------- //

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack extracted = inventory.extractItem(slot, amount, false);
        setChanged();
        return extracted;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.getStackInSlot(slot);
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    protected LazyOptional<IItemHandler> wrapped() {
        return LazyOptional.of(() -> new InvWrapper(this));
    }
}
