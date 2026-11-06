package com.octoarms.mech;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Capability handle for {@link MechCharge}.
 */
public final class MechCapabilities {

    public static final Capability<MechCharge> CHARGE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private MechCapabilities() {
    }
}
