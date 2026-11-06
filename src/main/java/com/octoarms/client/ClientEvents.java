package com.octoarms.client;

import com.octoarms.OctoArms;
import com.octoarms.network.OctoNetwork;
import com.octoarms.network.packet.FireIntegratedPacket;
import com.octoarms.network.packet.ToggleModePacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OctoArms.MODID, value = Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getConnection() == null) {
            return;
        }
        if (OctoKeyBindings.FIRE_ARMS.consumeClick()) {
            OctoNetwork.sendToServer(new FireIntegratedPacket());
        }
        if (OctoKeyBindings.TOGGLE_THRUSTERS.consumeClick()) {
            OctoNetwork.sendToServer(new ToggleModePacket(ToggleModePacket.MODE_HOVER));
        }
        if (OctoKeyBindings.TOGGLE_VISOR.consumeClick()) {
            OctoNetwork.sendToServer(new ToggleModePacket(ToggleModePacket.MODE_VISOR));
        }
        if (OctoKeyBindings.TOGGLE_CLING.consumeClick()) {
            OctoNetwork.sendToServer(new ToggleModePacket(ToggleModePacket.MODE_CLING));
        }
    }
}
