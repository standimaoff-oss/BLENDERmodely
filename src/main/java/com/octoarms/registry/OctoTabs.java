package com.octoarms.registry;

import com.octoarms.OctoArms;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class OctoTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OctoArms.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.octoarms"))
                    .icon(() -> new ItemStack(OctoItems.ARMOR.get("spider_mech_chestplate").get()))
                    .displayItems((parameters, output) -> {
                        OctoBlocks.BLOCKS.getEntries().forEach(entry ->
                                output.accept(new ItemStack(entry.get())));
                        OctoItems.COMPONENTS.values().forEach(entry ->
                                output.accept(new ItemStack(entry.get())));
                        output.accept(new ItemStack(OctoItems.POWER_CELL.get()));
                        output.accept(new ItemStack(OctoItems.ADVANCED_POWER_CELL.get()));
                        output.accept(new ItemStack(OctoItems.NETHER_POWER_CELL.get()));
                        output.accept(new ItemStack(OctoItems.PLASMA_BLADE.get()));
                        output.accept(new ItemStack(OctoItems.ARM_CANNON.get()));
                        output.accept(new ItemStack(OctoItems.RAILGUN.get()));
                        for (var entry : OctoItems.ARMOR.values()) {
                            output.accept(new ItemStack(entry.get()));
                        }
                    })
                    .build());

    private OctoTabs() {
    }
}
