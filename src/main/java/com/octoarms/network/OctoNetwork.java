package com.octoarms.network;

import com.octoarms.OctoArms;
import com.octoarms.network.packet.FireIntegratedPacket;
import com.octoarms.network.packet.SyncFlagsPacket;
import com.octoarms.network.packet.ToggleModePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class OctoNetwork {

    private static final String PROTOCOL = "octoarms-1";
    public static SimpleChannel CHANNEL;

    private OctoNetwork() {
    }

    public static void init() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(OctoArms.MODID, "main"),
                () -> PROTOCOL,
                PROTOCOL::equals,
                PROTOCOL::equals);

        CHANNEL.messageBuilder(FireIntegratedPacket.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .encoder(FireIntegratedPacket::encode)
                .decoder(FireIntegratedPacket::decode)
                .consumerMainThread(FireIntegratedPacket::handle)
                .add();

        CHANNEL.messageBuilder(ToggleModePacket.class, 2, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ToggleModePacket::encode)
                .decoder(ToggleModePacket::decode)
                .consumerMainThread(ToggleModePacket::handle)
                .add();

        CHANNEL.messageBuilder(SyncFlagsPacket.class, 3, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncFlagsPacket::encode)
                .decoder(SyncFlagsPacket::decode)
                .consumerMainThread(SyncFlagsPacket::handle)
                .add();
    }

    public static void sendToServer(Object message) {
        if (CHANNEL != null) {
            CHANNEL.sendToServer(message);
        }
    }

    public static void sendToPlayer(ServerPlayer player, Object message) {
        if (CHANNEL != null) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
        }
    }
}
