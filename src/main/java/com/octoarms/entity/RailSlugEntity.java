package com.octoarms.entity;

import com.octoarms.registry.OctoEntityTypes;
import com.octoarms.registry.OctoItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Hypersonic rail slug: no drop, pierces every entity on its line.
 */
public class RailSlugEntity extends ThrowableItemProjectile {

    private final Set<UUID> pierced = new HashSet<>();
    private float damage;

    public RailSlugEntity(EntityType<? extends RailSlugEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.damage = 22.0F;
    }

    public RailSlugEntity(Level level, LivingEntity shooter, float damage) {
        super(OctoEntityTypes.RAIL_SLUG.get(), shooter, level);
        this.setNoGravity(true);
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return OctoItems.component("heavy_slug");
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                level().addParticle(ParticleTypes.CRIT,
                        getX() - getDeltaMovement().x * i * 0.3D,
                        getY() - getDeltaMovement().y * i * 0.3D,
                        getZ() - getDeltaMovement().z * i * 0.3D, 0, 0, 0);
            }
        }
        if (tickCount > 100) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity target = hit.getEntity();
        if (!pierced.add(target.getUUID())) {
            return;
        }
        Entity owner = getOwner();
        target.hurt(level().damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null), damage);
        Vec3 knock = getDeltaMovement().normalize().scale(0.9D);
        target.push(knock.x, knock.y, knock.z);
        target.hurtMarked = true;
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        if (!level().isClientSide) {
            level().explode(this, getX(), getY(), getZ(), 0.6F, Level.ExplosionInteraction.NONE);
            discard();
        }
    }
}
