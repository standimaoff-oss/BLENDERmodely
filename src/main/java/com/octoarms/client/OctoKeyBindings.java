package com.octoarms.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public final class OctoKeyBindings {

    public static final KeyMapping FIRE_ARMS = new KeyMapping(
            "key.octoarms.fire_arms", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.octoarms.category");
    public static final KeyMapping TOGGLE_THRUSTERS = new KeyMapping(
            "key.octoarms.toggle_thrusters", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.octoarms.category");
    public static final KeyMapping TOGGLE_VISOR = new KeyMapping(
            "key.octoarms.toggle_visor", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.octoarms.category");
    public static final KeyMapping TOGGLE_CLING = new KeyMapping(
            "key.octoarms.toggle_cling", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.octoarms.category");

    private OctoKeyBindings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(FIRE_ARMS);
        event.register(TOGGLE_THRUSTERS);
        event.register(TOGGLE_VISOR);
        event.register(TOGGLE_CLING);
    }
}
