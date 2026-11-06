package com.octoarms.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiset matching for machine recipes: every weighted ingredient must be
 * present, and no unrelated item may sit in the input slots.
 */
public final class RecipeMatcher {

    private RecipeMatcher() {
    }

    public static boolean matches(Container inv, List<PressingRecipe.Entry> entries) {
        List<Integer> remaining = new ArrayList<>(entries.size());
        for (PressingRecipe.Entry entry : entries) {
            remaining.add(entry.count());
        }

        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack stack = inv.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            int left = stack.getCount();
            for (int i = 0; i < entries.size() && left > 0; i++) {
                PressingRecipe.Entry entry = entries.get(i);
                if (remaining.get(i) > 0 && entry.ingredient().test(stack)) {
                    int take = Math.min(left, remaining.get(i));
                    remaining.set(i, remaining.get(i) - take);
                    left -= take;
                }
            }
            if (left > 0) {
                return false; // foreign item in the machine
            }
        }
        for (int left : remaining) {
            if (left > 0) {
                return false;
            }
        }
        return true;
    }

    /** Consumes the recipe inputs from the container. */
    public static void consume(Container inv, List<PressingRecipe.Entry> entries) {
        List<Integer> remaining = new ArrayList<>(entries.size());
        for (PressingRecipe.Entry entry : entries) {
            remaining.add(entry.count());
        }
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack stack = inv.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            int left = stack.getCount();
            for (int i = 0; i < entries.size() && left > 0; i++) {
                PressingRecipe.Entry entry = entries.get(i);
                if (remaining.get(i) > 0 && entry.ingredient().test(stack)) {
                    int take = Math.min(left, remaining.get(i));
                    remaining.set(i, remaining.get(i) - take);
                    left -= take;
                }
            }
            int removed = stack.getCount() - left;
            if (removed > 0) {
                stack.shrink(removed);
                inv.setItem(slot, stack);
            }
        }
    }
}
