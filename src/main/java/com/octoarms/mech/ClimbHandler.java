package com.octoarms.mech;

import com.octoarms.OctoConfig;
import com.octoarms.item.MechArmorItem;
import com.octoarms.registry.OctoTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Wall crawling physics. The mech claws bite into whatever surface the player
 * is pressed against; grip, speed and charge cost all depend on the block
 * (see {@link SurfaceProfile}).
 *
 * <p>Runs on both logical sides with identical maths so the client feels
 * responsive while the server stays authoritative.</p>
 */
public final class ClimbHandler {

    private static final Map<UUID, ClimbState> STATES = new HashMap<>();

    public static final class ClimbState {
        public boolean climbing;
        public Direction wall = Direction.NORTH;
        public SurfaceProfile profile;
        public int soundTimer;
    }

    private ClimbHandler() {
    }

    public static ClimbState state(Player player) {
        return STATES.computeIfAbsent(player.getUUID(), id -> new ClimbState());
    }

    public static boolean isClimbing(Player player) {
        ClimbState s = STATES.get(player.getUUID());
        return s != null && s.climbing;
    }

    public static void clear(Player player) {
        STATES.remove(player.getUUID());
    }

    /**
     * @return the direction of the wall the player is touching, or null
     */
    public static Direction findWall(Level level, Player player) {
        AABB box = player.getBoundingBox().inflate(0.12D, 0.0D, 0.12D);
        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
            int dx = dir.getStepX();
            int dz = dir.getStepZ();
            for (double y = box.minY; y <= box.maxY + 0.5D; y += 0.9D) {
                BlockPos probe = BlockPos.containing(
                        box.minX + (dx > 0 ? box.getXsize() : 0) + dx * 0.06D,
                        y,
                        box.minZ + (dz > 0 ? box.getZsize() : 0) + dz * 0.06D);
                BlockState state = level.getBlockState(probe);
                if (state.isAir() || state.is(OctoTags.NO_GRIP)) {
                    continue;
                }
                if (!state.getCollisionShape(level, probe).isEmpty() && state.isSolidRender(level, probe)) {
                    return dir;
                }
            }
        }
        return null;
    }

    /**
     * Called at the END of every player tick (both sides).
     */
    public static void tick(Player player) {
        Level level = player.level();
        ClimbState st = state(player);
        MechSuitTier tier = MechSuit.wornTier(player).orElse(null);
        boolean enabled = tier != null && tier.canClimb
                && OctoConfig.ALLOW_CLIMBING.get()
                && MechSuit.flag(player, MechSuit.KEY_CLING, true)
                && MechSuit.canPay(player, 1);

        Direction wall = enabled ? findWall(level, player) : null;
        boolean pressed = player.horizontalCollision || player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-5D;

        if (wall == null || !enabled || !(pressed || st.climbing)) {
            if (st.climbing) {
                leave(player, st);
            }
            return;
        }

        BlockPos wallPos = player.blockPosition().relative(wall);
        BlockState wallState = level.getBlockState(wallPos);
        SurfaceProfile profile = SurfaceProfile.of(level, wallPos, wallState);
        if (profile.grip <= 0.01F) {
            if (st.climbing) {
                leave(player, st);
            }
            return;
        }

        boolean entered = !st.climbing;
        st.climbing = true;
        st.wall = wall;
        st.profile = profile;

        player.setNoGravity(true);
        player.fallDistance = 0.0F;

        Vec3 motion = player.getDeltaMovement();
        double climb = OctoConfig.CLIMB_SPEED.get() * profile.speed;
        double up;
        if (player.isShiftKeyDown()) {
            up = 0.0D;                                  // cling in place
        } else if (player.zza > 0.01F) {
            up = climb;                                // crawl up
        } else if (player.zza < -0.01F) {
            up = -climb * 0.8D;                        // descend
        } else {
            up = -0.035D * (1.5F - profile.grip);      // slow slide on weak grip
        }
        // stick to the wall and damp sideways drift
        double nx = wall.getStepX();
        double nz = wall.getStepZ();
        double stick = 0.02D;
        player.setDeltaMovement(
                motion.x * 0.55D - nx * stick,
                up,
                motion.z * 0.55D - nz * stick);

        int cost = (int) Math.max(1, tier.costClimb * profile.cost * OctoConfig.CLIMB_COST_MULTIPLIER.get());
        if (up != 0.0D || entered) {
            MechSuit.drain(player, cost);
        }

        // physical feedback: sparks/dust + scrape sound scaled by the surface
        st.soundTimer++;
        if (level.isClientSide) {
            ParticleOptions particle = profile.particleFor(wallState);
            if (particle != null && up != 0.0D) {
                for (int i = 0; i < 2; i++) {
                    double px = player.getX() + (level.getRandom().nextDouble() - 0.5D) * 0.8D - nx * 0.4D;
                    double py = player.getY() + level.getRandom().nextDouble() * player.getBbHeight();
                    double pz = player.getZ() + (level.getRandom().nextDouble() - 0.5D) * 0.8D - nz * 0.4D;
                    level.addParticle(particle, px, py, pz, -nx * 0.05D, -0.02D, -nz * 0.05D);
                }
            }
        } else if (st.soundTimer % 6 == 0) {
            level.playSound(null, player.blockPosition(), profile.scrapeSound, SoundSource.PLAYERS,
                    0.35F, profile.pitch * (0.9F + level.getRandom().nextFloat() * 0.2F));
        }
    }

    private static void leave(Player player, ClimbState st) {
        st.climbing = false;
        st.profile = null;
        player.setNoGravity(false);
    }

    /** Wall-jump impulse: push away from the wall and upwards. */
    public static Vec3 wallJumpImpulse(Player player) {
        ClimbState st = state(player);
        if (!st.climbing || st.wall == null) {
            return null;
        }
        Direction wall = st.wall;
        return new Vec3(-wall.getStepX() * 0.55D, 0.52D, -wall.getStepZ() * 0.55D);
    }

    public static void releaseAfterJump(Player player) {
        ClimbState st = state(player);
        leave(player, st);
    }

    /** Armor piece used to gate climbing effects (the chest servo core). */
    static boolean chestIsClimber(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof MechArmorItem armor
                && armor.tier().canClimb;
    }
}
