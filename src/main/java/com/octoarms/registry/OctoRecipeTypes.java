package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.recipe.AssemblyRecipe;
import com.octoarms.recipe.PressingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OctoRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, OctoArms.MODID);

    public static final RegistryObject<RecipeType<PressingRecipe>> PRESSING =
            RECIPE_TYPES.register("pressing", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return OctoArms.MODID + ":pressing";
                }
            });

    public static final RegistryObject<RecipeType<AssemblyRecipe>> ASSEMBLY =
            RECIPE_TYPES.register("assembly", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return OctoArms.MODID + ":assembly";
                }
            });

    private OctoRecipeTypes() {
    }
}
