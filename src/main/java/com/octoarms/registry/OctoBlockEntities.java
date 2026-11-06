package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.blockentity.ChargingDockBlockEntity;
import com.octoarms.blockentity.HydraulicPressBlockEntity;
import com.octoarms.blockentity.MechAssemblerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OctoBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OctoArms.MODID);

    public static final RegistryObject<BlockEntityType<HydraulicPressBlockEntity>> HYDRAULIC_PRESS =
            BLOCK_ENTITIES.register("hydraulic_press", () -> BlockEntityType.Builder
                    .of(HydraulicPressBlockEntity::new, OctoBlocks.HYDRAULIC_PRESS.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChargingDockBlockEntity>> CHARGING_DOCK =
            BLOCK_ENTITIES.register("charging_dock", () -> BlockEntityType.Builder
                    .of(ChargingDockBlockEntity::new, OctoBlocks.CHARGING_DOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<MechAssemblerBlockEntity>> MECH_ASSEMBLER =
            BLOCK_ENTITIES.register("mech_assembler", () -> BlockEntityType.Builder
                    .of(MechAssemblerBlockEntity::new, OctoBlocks.MECH_ASSEMBLER.get()).build(null));

    private OctoBlockEntities() {
    }
}
