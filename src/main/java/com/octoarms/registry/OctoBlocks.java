package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.block.MachineBlock;
import com.octoarms.blockentity.ChargingDockBlockEntity;
import com.octoarms.blockentity.HydraulicPressBlockEntity;
import com.octoarms.blockentity.MechAssemblerBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OctoBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OctoArms.MODID);

    private static BlockBehaviour.Properties machineProps(int light) {
        return BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_BLOCK)
                .strength(4.0F, 8.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
                .lightLevel(state -> state.getValue(MachineBlock.LIT) ? light : 0);
    }

    public static final RegistryObject<MachineBlock> HYDRAULIC_PRESS = BLOCKS.register("hydraulic_press",
            () -> new MachineBlock(machineProps(4), () -> OctoBlockEntities.HYDRAULIC_PRESS.get(),
                    HydraulicPressBlockEntity.SLOTS));

    public static final RegistryObject<MachineBlock> CHARGING_DOCK = BLOCKS.register("charging_dock",
            () -> new MachineBlock(machineProps(9), () -> OctoBlockEntities.CHARGING_DOCK.get(),
                    ChargingDockBlockEntity.SLOTS));

    public static final RegistryObject<MachineBlock> MECH_ASSEMBLER = BLOCKS.register("mech_assembler",
            () -> new MachineBlock(machineProps(6), () -> OctoBlockEntities.MECH_ASSEMBLER.get(),
                    MechAssemblerBlockEntity.SLOTS));

    public static final RegistryObject<Block> STEEL_CASING = BLOCKS.register("steel_casing",
            () -> new Block(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_BLOCK)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    public static final RegistryObject<Block> COIL_BLOCK = BLOCKS.register("coil_block",
            () -> new Block(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.COPPER_BLOCK)
                    .strength(3.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.COPPER)));

    private OctoBlocks() {
    }
}
