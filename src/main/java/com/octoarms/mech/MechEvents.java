package com.octoarms.mech;

import com.octoarms.OctoArms;
import com.octoarms.OctoConfig;
import com.octoarms.item.IChargeableItem;
import com.octoarms.network.OctoNetwork;
import com.octoarms.network.packet.SyncFlagsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

/**
 * Forge-bus glue: per-tick suit systems, jump/fall physics, damage absorption,
 * capability attachment and login synchronisation.
 */
public class MechEvents {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        ClimbHandler.tick(player);

        if (player.level().isClientSide) {
            return;
        }
        serverTick(player);
    }

    private void serverTick(Player player) {
        // CHESTPLATE = power core: cells, sprint servos, hover thrusters, cling.
        Optional<MechSuitTier> chest = MechSuit.wornTier(player);

        // power cells in the pockets feed the suit
        if (chest.isPresent() && OctoConfig.CELLS_FEED_INVENTORY.get() && player.tickCount % 4 == 0) {
            MechSuit.feedFromCells(player, 40);
        }

        // servo-sprint drain
        if (chest.isPresent() && player.isSprinting() && player.tickCount % 4 == 0) {
            int cost = (int) Math.max(1, chest.get().costSprint * OctoConfig.SPRINT_COST_MULTIPLIER.get());
            MechSuit.drain(player, cost);
        }

        // hover thrusters
        if (chest.isPresent() && OctoConfig.ALLOW_FLIGHT.get() && chest.get().canFly) {
            MechSuitTier tier = chest.get();
            boolean hover = MechSuit.flag(player, MechSuit.KEY_HOVER, false);
            boolean powered = MechSuit.suitFraction(player) > 0.01F;
            boolean allowed = hover && powered && !player.isCreative() && !player.isSpectator();
            var abilities = player.getAbilities();
            if (abilities.mayfly != allowed) {
                abilities.mayfly = allowed;
                if (!allowed) {
                    abilities.flying = false;
                }
                player.onUpdateAbilities();
            }
            if (abilities.flying) {
                int cost = (int) Math.max(1, tier.costFly * OctoConfig.FLIGHT_COST_MULTIPLIER.get());
                MechSuit.drain(player, cost);
            }
        }

        // full-set passive bonuses
        if (chest.isPresent() && player.tickCount % 60 == 0 && MechSuit.isFullSet(player, chest.get())
                && MechSuit.suitFraction(player) > 0.02F) {
            switch (chest.get()) {
                case COPPER -> player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 140, 0, true, false));
                case IRON -> player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 140, 0, true, false));
                case GOLD -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 140, 0, true, false));
                case DIAMOND -> player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 140, 0, true, false));
                case NETHERITE -> {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 0, true, false));
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 140, 0, true, false));
                }
                case SPIDER -> {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 140, 0, true, false));
                    player.addEffect(new MobEffectInstance(MobEffects.JUMP, 140, 0, true, false));
                }
            }
        }

        // HELMET = visor: night vision while the visor key is on
        if (player.tickCount % 80 == 0) {
            boolean visorKey = MechSuit.flag(player, MechSuit.KEY_VISOR, false);
            Optional<MechSuitTier> head = MechSuit.tierInSlot(player, EquipmentSlot.HEAD);
            boolean visor = visorKey
                    && head.isPresent()
                    && head.get().visorNightVision
                    && MechSuit.suitFraction(player) > 0.01F;
            if (visor) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 320, 0, true, false));
            } else if (visorKey && player.hasEffect(MobEffects.NIGHT_VISION)) {
                player.removeEffect(MobEffects.NIGHT_VISION);
            }
        }

        // LEGGINGS = servo stride: speed scales with the leggings' own tier
        if (player.tickCount % 60 == 0) {
            Optional<MechSuitTier> legs = MechSuit.tierInSlot(player, EquipmentSlot.LEGS);
            if (legs.isPresent() && MechSuit.suitFraction(player) > 0.01F) {
                int amp = Math.round(legs.get().speedBonus * 12.0F);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 140, amp, true, false));
            }
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        // wall-jump belongs to the chestplate (cling system)
        Optional<MechSuitTier> chest = MechSuit.wornTier(player);
        if (ClimbHandler.isClimbing(player)) {
            if (chest.isEmpty() || !MechSuit.canPay(player, chest.get().costJump)) {
                return;
            }
            MechSuitTier tier = chest.get();
            Vec3 impulse = ClimbHandler.wallJumpImpulse(player);
            if (impulse != null) {
                player.setDeltaMovement(impulse);
                player.hurtMarked = true;
                MechSuit.drain(player, tier.costJump * 2);
                ClimbHandler.releaseAfterJump(player);
                player.level().playSound(null, player.blockPosition(), SoundEvents.PISTON_EXTEND,
                        SoundSource.PLAYERS, 0.5F, 1.4F);
            }
            return;
        }

        // BOOTS = jump servos: boost scales with the boots' own tier
        Optional<MechSuitTier> boots = MechSuit.tierInSlot(player, EquipmentSlot.FEET);
        if (boots.isEmpty() || !MechSuit.canPay(player, boots.get().costJump)) {
            return;
        }
        MechSuitTier tier = boots.get();

        Vec3 motion = player.getDeltaMovement();
        if (motion.y > 0.0D) {
            player.setDeltaMovement(motion.x, motion.y * (1.0D + tier.jumpBonus), motion.z);
            player.hurtMarked = true;
            MechSuit.drain(player, tier.costJump);
            player.level().playSound(null, player.blockPosition(), SoundEvents.PISTON_CONTRACT,
                    SoundSource.PLAYERS, 0.35F, 1.2F);
        }
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        // BOOTS = landing dampers
        Optional<MechSuitTier> boots = MechSuit.tierInSlot(player, EquipmentSlot.FEET);
        if (boots.isEmpty()) {
            return;
        }
        float distance = event.getDistance();
        if (distance <= 0.5F) {
            return;
        }
        int cost = (int) (distance * 6);
        if (MechSuit.drain(player, cost)) {
            event.setDistance(0.0F);
            event.setDamageMultiplier(0.0F);
            if (distance > 4.0F) {
                player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_PLACE,
                        SoundSource.PLAYERS, 0.6F, 0.8F);
            }
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        DamageSource source = event.getSource();
        if (source.isCreativePlayer()) {
            return;
        }
        Optional<MechSuitTier> full = MechSuit.fullSetTier(player);
        if (full.isEmpty()) {
            return;
        }
        float amount = event.getAmount();
        int cost = (int) (amount * 12);
        if (MechSuit.drain(player, cost)) {
            event.setAmount(amount * 0.72F);
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof IChargeableItem chargeable) {
            event.addCapability(new ResourceLocation(OctoArms.MODID, "charge"),
                    new MechChargeProvider(chargeable.getCapacity(event.getObject())));
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncFlags((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncFlags(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncFlags(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ClimbHandler.clear(event.getEntity());
    }

    private void syncFlags(ServerPlayer player) {
        OctoNetwork.sendToPlayer(player, new SyncFlagsPacket(
                MechSuit.flag(player, MechSuit.KEY_CLING, true),
                MechSuit.flag(player, MechSuit.KEY_HOVER, false),
                MechSuit.flag(player, MechSuit.KEY_VISOR, false)));
    }
}
