package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.entity.PlasmaBoltEntity;
import com.octoarms.entity.RailSlugEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OctoEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OctoArms.MODID);

    public static final RegistryObject<EntityType<PlasmaBoltEntity>> PLASMA_BOLT =
            ENTITIES.register("plasma_bolt", () -> EntityType.Builder.<PlasmaBoltEntity>of(PlasmaBoltEntity::new, MobCategory.MISC)
                    .sized(0.45F, 0.45F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(OctoArms.MODID + ":plasma_bolt"));

    public static final RegistryObject<EntityType<RailSlugEntity>> RAIL_SLUG =
            ENTITIES.register("rail_slug", () -> EntityType.Builder.<RailSlugEntity>of(RailSlugEntity::new, MobCategory.MISC)
                    .sized(0.35F, 0.35F)
                    .clientTrackingRange(6)
                    .updateInterval(5)
                    .build(OctoArms.MODID + ":rail_slug"));

    private OctoEntityTypes() {
    }
}
