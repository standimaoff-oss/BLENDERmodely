package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.recipe.AssemblyRecipe;
import com.octoarms.recipe.PressingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OctoRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OctoArms.MODID);

    public static final RegistryObject<PressingRecipe.Serializer> PRESSING =
            RECIPE_SERIALIZERS.register("pressing", PressingRecipe.Serializer::new);

    public static final RegistryObject<AssemblyRecipe.Serializer> ASSEMBLY =
            RECIPE_SERIALIZERS.register("assembly", AssemblyRecipe.Serializer::new);

    private OctoRecipeSerializers() {
    }
}
