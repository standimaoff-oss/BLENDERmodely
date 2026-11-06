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
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Glowing plasma bolt fired by arm cannons and integrated mech weapons.
 */
public class PlasmaBoltEntity extends ThrowableItemProjectile {

    private float damage;

    public PlasmaBoltEntity(EntityType<? extends PlasmaBoltEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.damage = 9.0F;
    }

    public PlasmaBoltEntity(Level level, LivingEntity shooter, float damage) {
        super(OctoEntityTypes.PLASMA_BOLT.get(), shooter, level);
        this.setNoGravity(true);
        this.damage = damage;
    }

    @Override
    protected Item getDefaultItem() {
        return OctoItems.component("energy_orb");
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(), 0, 0, 0);
        }
        if (tickCount > 120) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        Entity target = hit.getEntity();
        Entity owner = getOwner();
        target.hurt(level().damageSources().mobProjectile(this, owner instanceof LivingEntity living ? living : null), damage);
        target.setRemainingFireTicks(40);
        detonate();
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        detonate();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            discard();
        }
    }

    private void detonate() {
        if (level().isClientSide) {
            return;
        }
        level().explode(this, getX(), getY(), getZ(), 0.9F, ExplosionInteraction.NONE);
        for (int i = 0; i < 12; i++) {
            level().addParticle(ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(),
                    (random.nextDouble() - 0.5D) * 0.4D,
                    random.nextDouble() * 0.3D,
                    (random.nextDouble() - 0.5D) * 0.4D);
        }
    }
}
