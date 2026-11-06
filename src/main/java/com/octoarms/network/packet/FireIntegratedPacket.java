package com.octoarms.network.packet;

import com.octoarms.OctoConfig;
import com.octoarms.entity.PlasmaBoltEntity;
import com.octoarms.item.ChargeUtils;
import com.octoarms.item.MechArmorItem;
import com.octoarms.mech.MechSuitTier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client -> server: "fire the integrated octo/netherite arm cannon".
 */
public class FireIntegratedPacket {

    public FireIntegratedPacket() {
    }

    public static FireIntegratedPacket decode(FriendlyByteBuf buf) {
        return new FireIntegratedPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (!(chest.getItem() instanceof MechArmorItem armor)) {
                return;
            }
            MechSuitTier tier = armor.tier();
            if (!tier.integratedCannon || player.getCooldowns().isOnCooldown(chest.getItem())) {
                return;
            }
            if (ChargeUtils.overheated(chest)) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("octoarms.msg.overheat"), true);
                return;
            }
            int cost = 400;
            if (!ChargeUtils.extract(chest, cost)) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("octoarms.msg.no_charge"), true);
                return;
            }
            ChargeUtils.addHeat(chest, 16.0F);
            PlasmaBoltEntity bolt = new PlasmaBoltEntity(player.level(), player,
                    OctoConfig.PLASMA_DAMAGE.get().floatValue());
            bolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.8F, 1.0F);
            player.level().addFreshEntity(bolt);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.8F, 1.1F);
            player.getCooldowns().addCooldown(chest.getItem(), 14);
        });
        ctx.get().setPacketHandled(true);
    }
}
