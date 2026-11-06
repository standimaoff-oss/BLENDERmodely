package com.octoarms.menu;

import net.minecraft.world.inventory.ContainerData;

/**
 * Implemented by every machine menu so the generic MachineScreen can read
 * the synced container data without knowing the concrete menu type.
 */
public interface MachineMenu {
    ContainerData machineData();
}
