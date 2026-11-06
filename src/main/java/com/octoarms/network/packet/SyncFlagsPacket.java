package com.octoarms.network.packet;

import com.octoarms.client.MechClientFlags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server -> client: mirror of the player's mech mode flags for HUD/rendering.
 */
public class SyncFlagsPacket {

    private final boolean cling;
    private final boolean hover;
    private final boolean visor;

    public SyncFlagsPacket(boolean cling, boolean hover, boolean visor) {
        this.cling = cling;
        this.hover = hover;
        this.visor = visor;
    }

    public static SyncFlagsPacket decode(FriendlyByteBuf buf) {
        return new SyncFlagsPacket(buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(cling);
        buf.writeBoolean(hover);
        buf.writeBoolean(visor);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> MechClientFlags.set(cling, hover, visor));
        ctx.get().setPacketHandled(true);
    }
}
