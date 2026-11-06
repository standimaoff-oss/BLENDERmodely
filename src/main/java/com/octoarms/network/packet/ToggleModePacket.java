package com.octoarms.network.packet;

import com.octoarms.mech.MechSuit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client -> server: toggle hover thrusters (0), visor night vision (1)
 * or wall cling (2). Server flips the flag and answers with SyncFlagsPacket.
 */
public class ToggleModePacket {

    public static final int MODE_HOVER = 0;
    public static final int MODE_VISOR = 1;
    public static final int MODE_CLING = 2;

    private final int mode;

    public ToggleModePacket(int mode) {
        this.mode = mode;
    }

    public static ToggleModePacket decode(FriendlyByteBuf buf) {
        return new ToggleModePacket(buf.readByte());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(mode);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            String key = switch (mode) {
                case MODE_HOVER -> MechSuit.KEY_HOVER;
                case MODE_VISOR -> MechSuit.KEY_VISOR;
                case MODE_CLING -> MechSuit.KEY_CLING;
                default -> null;
            };
            if (key == null) {
                return;
            }
            boolean current = MechSuit.flag(player, key, key.equals(MechSuit.KEY_CLING));
            MechSuit.setFlag(player, key, !current);
            String message = switch (mode) {
                case MODE_HOVER -> !current ? "octoarms.msg.thrusters_on" : "octoarms.msg.thrusters_off";
                case MODE_VISOR -> !current ? "octoarms.msg.visor_on" : "octoarms.msg.visor_off";
                default -> !current ? "octoarms.msg.cling_on" : "octoarms.msg.cling_off";
            };
            player.displayClientMessage(Component.translatable(message), true);
            com.octoarms.network.OctoNetwork.sendToPlayer(player, new SyncFlagsPacket(
                    MechSuit.flag(player, MechSuit.KEY_CLING, true),
                    MechSuit.flag(player, MechSuit.KEY_HOVER, false),
                    MechSuit.flag(player, MechSuit.KEY_VISOR, false)));
        });
        ctx.get().setPacketHandled(true);
    }
}
