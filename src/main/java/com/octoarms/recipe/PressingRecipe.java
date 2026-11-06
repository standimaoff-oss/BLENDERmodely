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
 * Hydraulic press recipe: up to three weighted inputs, one output, time.
 */
public class PressingRecipe implements Recipe<Container> {

    public record Entry(Ingredient ingredient, int count) {
    }

    private final ResourceLocation id;
    private final List<Entry> inputs;
    private final ItemStack result;
    private final int processTime;

    public PressingRecipe(ResourceLocation id, List<Entry> inputs, ItemStack result, int processTime) {
        this.id = id;
        this.inputs = inputs;
        this.result = result;
        this.processTime = processTime;
    }

    public List<Entry> inputs() {
        return inputs;
    }

    public int getProcessTime() {
        return processTime;
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
        return OctoRecipeSerializers.PRESSING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return OctoRecipeTypes.PRESSING.get();
    }

    public static class Serializer implements RecipeSerializer<PressingRecipe> {

        @Override
        public PressingRecipe fromJson(ResourceLocation id, JsonObject json) {
            List<Entry> inputs = new ArrayList<>();
            JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(obj);
                int count = GsonHelper.getAsInt(obj, "count", 1);
                inputs.add(new Entry(ingredient, count));
            }
            JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = net.minecraftforge.common.crafting.CraftingHelper.getItemStack(resultObj, true);
            int time = GsonHelper.getAsInt(json, "process_time", 120);
            return new PressingRecipe(id, inputs, result, time);
        }

        @Override
        public PressingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            List<Entry> inputs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                inputs.add(new Entry(Ingredient.fromNetwork(buf), buf.readVarInt()));
            }
            ItemStack result = buf.readItem();
            int time = buf.readVarInt();
            return new PressingRecipe(id, inputs, result, time);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PressingRecipe recipe) {
            buf.writeVarInt(recipe.inputs.size());
            for (Entry entry : recipe.inputs) {
                entry.ingredient().toNetwork(buf);
                buf.writeVarInt(entry.count());
            }
            buf.writeItem(recipe.result);
            buf.writeVarInt(recipe.processTime);
        }
    }
}
