package com.octoarms.item;

import net.minecraft.world.item.ItemStack;

/**
 * Anything that stores Power Units: mech pieces, power cells, energy weapons.
 */
public interface IChargeableItem {

    int getCapacity(ItemStack stack);

    /** Power Units consumed per "action" (shot, hit, tick) - for tooltips. */
    default int getActionCost(ItemStack stack) {
        return 0;
    }
}
