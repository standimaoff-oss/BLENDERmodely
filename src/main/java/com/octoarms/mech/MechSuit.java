package com.octoarms.mech;

import com.octoarms.item.IChargeableItem;
import com.octoarms.item.MechArmorItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

/**
 * Static helpers for querying and draining the mecha suit a player wears.
 */
public final class MechSuit {

    public static final String DATA_TAG = "octoData";
    public static final String KEY_CLING = "octoCling";
    public static final String KEY_HOVER = "octoHover";
    public static final String KEY_VISOR = "octoVisor";

    private MechSuit() {
    }

    public static Optional<MechCharge> chargeOf(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        LazyOptional<MechCharge> cap = stack.getCapability(MechCapabilities.CHARGE);
        return cap.resolve();
    }

    public static boolean isMechPiece(ItemStack stack) {
        return stack.getItem() instanceof IChargeableItem;
    }

    /** Tier of the chest piece (the "engine" of the suit), if any. */
    public static Optional<MechSuitTier> wornTier(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof MechArmorItem armor) {
            return Optional.of(armor.tier());
        }
        return Optional.empty();
    }

    /** True when all four slots hold mech pieces of the same tier. */
    /** Tier of the mech piece worn in one specific slot (empty for vanilla gear). */
    public static Optional<MechSuitTier> tierInSlot(Player player, EquipmentSlot slot) {
        ItemStack stack = player.getItemBySlot(slot);
        if (stack.getItem() instanceof MechArmorItem armor) {
            return Optional.of(armor.tier());
        }
        return Optional.empty();
    }

    public static boolean isFullSet(Player player, MechSuitTier tier) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof MechArmorItem armor) || armor.tier() != tier) {
                return false;
            }
        }
        return true;
    }

    public static Optional<MechSuitTier> fullSetTier(Player player) {
        Optional<MechSuitTier> tier = wornTier(player);
        return tier.filter(t -> isFullSet(player, t));
    }

    /** Average charge fraction across all worn pieces of the suit. */
    public static float suitFraction(Player player) {
        int count = 0;
        float sum = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            Optional<MechCharge> charge = chargeOf(player.getItemBySlot(slot));
            if (charge.isPresent()) {
                sum += charge.get().getFraction();
                count++;
            }
        }
        return count == 0 ? 0 : sum / count;
    }

    /**
     * Drains {@code amount} PU spread over every worn mech piece.
     *
     * @return true when the full amount could be paid
     */
    public static boolean drain(Player player, int amount) {
        if (amount <= 0) {
            return true;
        }
        int pieces = 0;
        for (EquipmentSlot slot : EQUIPPABLE) {
            if (isMechPiece(player.getItemBySlot(slot))) {
                pieces++;
            }
        }
        if (pieces == 0) {
            return false;
        }
        int per = (int) Math.ceil(amount / (float) pieces);
        int paid = 0;
        for (EquipmentSlot slot : EQUIPPABLE) {
            ItemStack stack = player.getItemBySlot(slot);
            Optional<MechCharge> charge = chargeOf(stack);
            if (charge.isPresent()) {
                paid += charge.get().extract(per, false);
            }
        }
        return paid >= amount;
    }

    public static boolean canPay(Player player, int amount) {
        int pieces = 0;
        for (EquipmentSlot slot : EQUIPPABLE) {
            if (isMechPiece(player.getItemBySlot(slot))) {
                pieces++;
            }
        }
        if (pieces == 0) {
            return false;
        }
        int per = (int) Math.ceil(amount / (float) pieces);
        for (EquipmentSlot slot : EQUIPPABLE) {
            Optional<MechCharge> charge = chargeOf(player.getItemBySlot(slot));
            if (charge.isPresent() && !charge.get().has(per)) {
                return false;
            }
        }
        return true;
    }

    /** Feeds the worn suit from charged power cells in the inventory. */
    public static void feedFromCells(Player player, int rate) {
        if (suitFraction(player) >= 0.999F) {
            return;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack cell = player.getInventory().getItem(i);
            if (cell.getItem() instanceof com.octoarms.item.PowerCellItem) {
                Optional<MechCharge> cellCharge = chargeOf(cell);
                if (cellCharge.isEmpty() || cellCharge.get().isEmpty()) {
                    continue;
                }
                for (EquipmentSlot slot : EQUIPPABLE) {
                    ItemStack piece = player.getItemBySlot(slot);
                    Optional<MechCharge> pieceCharge = chargeOf(piece);
                    if (pieceCharge.isPresent() && !pieceCharge.get().isFull()) {
                        int moved = cellCharge.get().extract(rate, false);
                        pieceCharge.get().receive(moved, false);
                        if (moved > 0) {
                            return;
                        }
                    }
                }
            }
        }
    }

    // -- player flags (cling / hover / visor) ------------------------------ //

    public static CompoundTag mechData(Player player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(DATA_TAG)) {
            root.put(DATA_TAG, new CompoundTag());
        }
        return root.getCompound(DATA_TAG);
    }

    public static boolean flag(Player player, String key, boolean def) {
        CompoundTag tag = mechData(player);
        return tag.contains(key) ? tag.getBoolean(key) : def;
    }

    public static void setFlag(Player player, String key, boolean value) {
        mechData(player).putBoolean(key, value);
    }

    private static final EquipmentSlot[] EQUIPPABLE = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };
}
