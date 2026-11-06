package com.octoarms.client;

/**
 * Client-side mirror of the player's mech mode flags (synced by the server).
 */
public final class MechClientFlags {

    public static boolean cling = true;
    public static boolean hover = false;
    public static boolean visor = false;

    private MechClientFlags() {
    }

    public static void set(boolean cling, boolean hover, boolean visor) {
        MechClientFlags.cling = cling;
        MechClientFlags.hover = hover;
        MechClientFlags.visor = visor;
    }
}
