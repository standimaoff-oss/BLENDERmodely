package com.octoarms.mech;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Attaches a {@link MechCharge} to mech items, cells and weapons.
 */
public class MechChargeProvider implements ICapabilitySerializable<CompoundTag> {

    private final MechCharge charge;
    private final LazyOptional<MechCharge> holder;

    public MechChargeProvider(int capacity) {
        this.charge = new MechCharge(capacity);
        this.holder = LazyOptional.of(() -> this.charge);
    }

    public MechCharge charge() {
        return charge;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return MechCapabilities.CHARGE.orEmpty(cap, holder);
    }

    @Override
    public CompoundTag serializeNBT() {
        return charge.serialize();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        charge.deserialize(nbt);
    }

    public void invalidate() {
        holder.invalidate();
    }
}
