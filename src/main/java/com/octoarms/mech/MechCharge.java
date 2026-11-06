package com.octoarms.mech;

import net.minecraft.nbt.CompoundTag;

/**
 * The charge + heat buffer inside every mech piece, power cell and weapon.
 * Stored in Power Units (PU).
 */
public class MechCharge {

    public static final float MAX_HEAT = 100.0F;

    private final int capacity;
    private int stored;
    private float heat;

    public MechCharge(int capacity) {
        this.capacity = Math.max(1, capacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStored() {
        return stored;
    }

    public void setStored(int value) {
        this.stored = clamp(value);
    }

    public float getFraction() {
        return stored / (float) capacity;
    }

    public boolean has(int amount) {
        return stored >= amount;
    }

    public boolean isEmpty() {
        return stored <= 0;
    }

    public boolean isFull() {
        return stored >= capacity;
    }

    /** @return how much was actually inserted */
    public int receive(int amount, boolean simulate) {
        int room = capacity - stored;
        int moved = Math.min(Math.max(amount, 0), room);
        if (!simulate) {
            stored += moved;
        }
        return moved;
    }

    /** @return how much was actually extracted */
    public int extract(int amount, boolean simulate) {
        int moved = Math.min(Math.max(amount, 0), stored);
        if (!simulate) {
            stored -= moved;
        }
        return moved;
    }

    // -- heat -------------------------------------------------------------- //

    public float getHeat() {
        return heat;
    }

    public void addHeat(float amount) {
        this.heat = Math.min(MAX_HEAT, this.heat + Math.max(0, amount));
    }

    public void cool(float amount) {
        this.heat = Math.max(0, this.heat - Math.max(0, amount));
    }

    public boolean isOverheated() {
        return heat >= MAX_HEAT - 0.01F;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(capacity, value));
    }

    // -- nbt --------------------------------------------------------------- //

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("octoStored", stored);
        tag.putFloat("octoHeat", heat);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        if (tag == null) {
            return;
        }
        this.stored = clamp(tag.getInt("octoStored"));
        this.heat = Math.max(0, Math.min(MAX_HEAT, tag.getFloat("octoHeat")));
    }
}
