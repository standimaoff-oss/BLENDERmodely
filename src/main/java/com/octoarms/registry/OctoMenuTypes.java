package com.octoarms.registry;

import com.octoarms.OctoArms;
import com.octoarms.menu.AssemblerMenu;
import com.octoarms.menu.ChargerMenu;
import com.octoarms.menu.PressMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OctoMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, OctoArms.MODID);

    public static final RegistryObject<MenuType<PressMenu>> PRESS =
            MENUS.register("hydraulic_press", () -> IForgeMenuType.create(PressMenu::fromNetwork));

    public static final RegistryObject<MenuType<ChargerMenu>> CHARGER =
            MENUS.register("charging_dock", () -> IForgeMenuType.create(ChargerMenu::fromNetwork));

    public static final RegistryObject<MenuType<AssemblerMenu>> ASSEMBLER =
            MENUS.register("mech_assembler", () -> IForgeMenuType.create(AssemblerMenu::fromNetwork));

    private OctoMenuTypes() {
    }
}
