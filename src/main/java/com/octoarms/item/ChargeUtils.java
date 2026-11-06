package com.octoarms.item;

import com.octoarms.mech.MechCapabilities;
import com.octoarms.mech.MechCharge;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Shared helpers for charge storage, share-tag sync and tooltips.
 */
public final class ChargeUtils {

    private ChargeUtils() {
    }

    public static Optional<MechCharge> charge(ItemStack stack) {
        return stack.getCapability(MechCapabilities.CHARGE).resolve();
    }

    public static int stored(ItemStack stack) {
        return charge(stack).map(MechCharge::getStored).orElse(0);
    }

    public static boolean extract(ItemStack stack, int amount) {
        Optional<MechCharge> cap = charge(stack);
        if (cap.isPresent() && cap.get().has(amount)) {
            cap.get().extract(amount, false);
            return true;
        }
        return false;
    }

    public static void addHeat(ItemStack stack, float amount) {
        charge(stack).ifPresent(c -> c.addHeat(amount));
    }

    public static void cool(ItemStack stack, float amount) {
        charge(stack).ifPresent(c -> c.cool(amount));
    }

    public static boolean overheated(ItemStack stack) {
        return charge(stack).map(MechCharge::isOverheated).orElse(false);
    }

    // -- share tag sync (server -> client) --------------------------------- //

    @Nullable
    public static CompoundTag writeShare(ItemStack stack, @Nullable CompoundTag base) {
        CompoundTag tag = base == null ? new CompoundTag() : base;
        charge(stack).ifPresent(c -> tag.put("octoCharge", c.serialize()));
        return tag.isEmpty() ? null : tag;
    }

    public static void readShare(ItemStack stack, @Nullable CompoundTag tag) {
        if (tag != null && tag.contains("octoCharge")) {
            charge(stack).ifPresent(c -> c.deserialize(tag.getCompound("octoCharge")));
        }
    }

    // -- tooltips ---------------------------------------------------------- //

    public static void appendChargeTooltip(ItemStack stack, List<Component> tooltip) {
        Optional<MechCharge> cap = charge(stack);
        if (cap.isEmpty()) {
            return;
        }
        MechCharge charge = cap.get();
        if (charge.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.octoarms.charge.empty"));
        } else {
            tooltip.add(Component.translatable("tooltip.octoarms.charge",
                    format(charge.getStored()), format(charge.getCapacity())));
        }
        if (charge.getHeat() > 1.0F) {
            tooltip.add(Component.translatable("tooltip.octoarms.heat",
                    String.format("%.0f", charge.getHeat())));
        }
    }

    public static String format(int value) {
        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0D);
        }
        if (value >= 1000) {
            return String.format("%.1fk", value / 1000.0D);
        }
        return Integer.toString(value);
    }
}
