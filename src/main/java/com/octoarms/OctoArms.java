package com.octoarms;

import com.octoarms.mech.MechCapabilities;
import com.octoarms.mech.MechCharge;
import com.octoarms.mech.MechEvents;
import com.octoarms.network.OctoNetwork;
import com.octoarms.registry.OctoBlockEntities;
import com.octoarms.registry.OctoBlocks;
import com.octoarms.registry.OctoEntityTypes;
import com.octoarms.registry.OctoItems;
import com.octoarms.registry.OctoMenuTypes;
import com.octoarms.registry.OctoRecipeSerializers;
import com.octoarms.registry.OctoRecipeTypes;
import com.octoarms.registry.OctoTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Octo Arms - mecha exosuits, machines and surface-reactive climbing
 * for Minecraft 1.20.1 (Forge).
 */
@Mod(OctoArms.MODID)
public class OctoArms {

    public static final String MODID = "octoarms";
    public static final Logger LOGGER = LogManager.getLogger();

    public OctoArms() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::registerCapabilities);

        OctoBlocks.BLOCKS.register(modBus);
        OctoItems.ITEMS.register(modBus);
        OctoBlockEntities.BLOCK_ENTITIES.register(modBus);
        OctoMenuTypes.MENUS.register(modBus);
        OctoEntityTypes.ENTITIES.register(modBus);
        OctoRecipeTypes.RECIPE_TYPES.register(modBus);
        OctoRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
        OctoTabs.TABS.register(modBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OctoConfig.SPEC, MODID + "-common.toml");

        MinecraftForge.EVENT_BUS.register(new MechEvents());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(OctoNetwork::init);
        LOGGER.info("Octo Arms initialised: suits, machines and octo-limbs standing by.");
    }

    @SubscribeEvent
    public void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(MechCharge.class);
        LOGGER.debug("Octo Arms capability registered: {}", MechCapabilities.CHARGE.getName());
    }
}
