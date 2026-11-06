package com.octoarms.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.octoarms.registry.OctoRecipeSerializers;
import com.octoarms.registry.OctoRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Mech assembler recipe: weighted ingredient multiset + PU energy cost.
 */
public class AssemblyRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final List<PressingRecipe.Entry> inputs;
    private final ItemStack result;
    private final int energy;

    public AssemblyRecipe(ResourceLocation id, List<PressingRecipe.Entry> inputs, ItemStack result, int energy) {
        this.id = id;
        this.inputs = inputs;
        this.result = result;
        this.energy = energy;
    }

    public List<PressingRecipe.Entry> inputs() {
        return inputs;
    }

    public int energy() {
        return energy;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return RecipeMatcher.matches(inv, inputs);
    }

    @Override
    public ItemStack assemble(Container inv, net.minecraft.core.RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        inputs.forEach(entry -> list.add(entry.ingredient()));
        return list;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return OctoRecipeSerializers.ASSEMBLY.get();
    }

    @Override
    public RecipeType<?> getType() {
        return OctoRecipeTypes.ASSEMBLY.get();
    }

    public static class Serializer implements RecipeSerializer<AssemblyRecipe> {

        @Override
        public AssemblyRecipe fromJson(ResourceLocation id, JsonObject json) {
            List<PressingRecipe.Entry> inputs = new ArrayList<>();
            JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(obj);
                int count = GsonHelper.getAsInt(obj, "count", 1);
                inputs.add(new PressingRecipe.Entry(ingredient, count));
            }
            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = net.minecraftforge.common.crafting.CraftingHelper.getItemStack(resultObj, true);
            int energy = GsonHelper.getAsInt(json, "energy", 10000);
            return new AssemblyRecipe(id, inputs, result, energy);
        }

        @Override
        public AssemblyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            List<PressingRecipe.Entry> inputs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                inputs.add(new PressingRecipe.Entry(Ingredient.fromNetwork(buf), buf.readVarInt()));
            }
            ItemStack result = buf.readItem();
            int energy = buf.readVarInt();
            return new AssemblyRecipe(id, inputs, result, energy);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AssemblyRecipe recipe) {
            buf.writeVarInt(recipe.inputs.size());
            for (PressingRecipe.Entry entry : recipe.inputs) {
                entry.ingredient().toNetwork(buf);
                buf.writeVarInt(entry.count());
            }
            buf.writeItem(recipe.result);
            buf.writeVarInt(recipe.energy);
        }
    }
}
