package com.octoarms.client;

import com.octoarms.OctoArms;
import com.octoarms.client.renderer.OctoArmsLayer;
import com.octoarms.client.renderer.OrbRenderer;
import com.octoarms.client.screen.MachineScreen;
import com.octoarms.menu.AssemblerMenu;
import com.octoarms.menu.ChargerMenu;
import com.octoarms.menu.PressMenu;
import com.octoarms.registry.OctoEntityTypes;
import com.octoarms.registry.OctoItems;
import com.octoarms.registry.OctoMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = OctoArms.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class OctoClient {

    private OctoClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.<PressMenu, MachineScreen<PressMenu>>register(
                    OctoMenuTypes.PRESS.get(), MachineScreen::press);
            MenuScreens.<ChargerMenu, MachineScreen<ChargerMenu>>register(
                    OctoMenuTypes.CHARGER.get(), MachineScreen::charger);
            MenuScreens.<AssemblerMenu, MachineScreen<AssemblerMenu>>register(
                    OctoMenuTypes.ASSEMBLER.get(), MachineScreen::assembler);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OctoEntityTypes.PLASMA_BOLT.get(),
                context -> new OrbRenderer<>(context, () -> OctoItems.component("energy_orb"), 1.1F));
        event.registerEntityRenderer(OctoEntityTypes.RAIL_SLUG.get(),
                context -> new OrbRenderer<>(context, () -> OctoItems.component("heavy_slug"), 0.9F));
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new OctoArmsLayer(renderer));
            }
        }
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        OctoKeyBindings.register(event);
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("mech_hud", new MechHudOverlay());
    }
}
