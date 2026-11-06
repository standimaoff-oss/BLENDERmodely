package com.octoarms.mech;

import com.octoarms.registry.OctoTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * How a surface behaves under mech claws: grip, speed, cost, feedback.
 * This is what makes the Spider Mech physically react to what it crawls on.
 */
public final class SurfaceProfile {

    /** 0 = no grip at all, 1 = normal, >1 = extra sticky */
    public final float grip;
    /** climb speed multiplier */
    public final float speed;
    /** charge cost multiplier */
    public final float cost;
    public final SoundEvent scrapeSound;
    public final float pitch;
    public final ParticleOptions particle;
    public final boolean fallingDust;

    private SurfaceProfile(float grip, float speed, float cost, SoundEvent sound, float pitch,
                           ParticleOptions particle, boolean fallingDust) {
        this.grip = grip;
        this.speed = speed;
        this.cost = cost;
        this.scrapeSound = sound;
        this.pitch = pitch;
        this.particle = particle;
        this.fallingDust = fallingDust;
    }

    public static SurfaceProfile of(Level level, BlockPos pos, BlockState state) {
        if (state.is(OctoTags.NO_GRIP)) {
            return new SurfaceProfile(0.0F, 0.0F, 0.0F, SoundEvents.FIRE_EXTINGUISH, 0.7F, null, false);
        }
        if (state.is(OctoTags.SLIPPERY)) {
            return new SurfaceProfile(0.30F, 0.65F, 1.35F, SoundEvents.GLASS_BREAK, 1.6F,
                    ParticleTypes.SNOWFLAKE, false);
        }
        if (state.is(OctoTags.STICKY)) {
            return new SurfaceProfile(1.45F, 0.45F, 1.5F, SoundEvents.HONEY_BLOCK_STEP, 0.8F,
                    ParticleTypes.DRIPPING_HONEY, false);
        }
        if (state.is(OctoTags.LOOSE)) {
            return new SurfaceProfile(0.60F, 0.80F, 1.15F, SoundEvents.SAND_STEP, 0.9F,
                    null, true);
        }
        if (state.is(OctoTags.METALLIC)) {
            return new SurfaceProfile(1.20F, 1.25F, 0.85F, SoundEvents.ANVIL_USE, 1.7F,
                    ParticleTypes.CRIT, false);
        }
        // generic: derive from the block's own sound type
        String soundName = state.getSoundType(level, pos, null).getPlaceSound().getLocation().getPath();
        SoundEvent scrape = SoundEvents.GRINDSTONE_USE;
        float pitch = 1.0F;
        if (soundName.contains("wood") || soundName.contains("bamboo")) {
            scrape = SoundEvents.WOOD_STEP;
            pitch = 0.7F;
        } else if (soundName.contains("stone") || soundName.contains("deepslate") || soundName.contains("basalt")) {
            scrape = SoundEvents.GRINDSTONE_USE;
            pitch = 1.1F;
        } else if (soundName.contains("metal") || soundName.contains("chain")) {
            scrape = SoundEvents.ANVIL_USE;
            pitch = 1.5F;
        } else if (soundName.contains("sand")) {
            scrape = SoundEvents.SAND_STEP;
            pitch = 0.9F;
        } else if (soundName.contains("glass")) {
            return new SurfaceProfile(0.45F, 0.9F, 1.1F, SoundEvents.GLASS_STEP, 1.4F, null, false);
        }
        return new SurfaceProfile(1.0F, 1.0F, 1.0F, scrape, pitch, null, true);
    }

    public ParticleOptions particleFor(BlockState state) {
        if (fallingDust) {
            return new BlockParticleOption(ParticleTypes.FALLING_DUST, state);
        }
        return particle;
    }
}
