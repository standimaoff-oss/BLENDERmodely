package com.octoarms.client.model;

import java.util.HashMap;
import java.util.Map;

/**
 * GENERATED FILE - do not edit by hand.
 * Produced by tools/gen_armor.py together with the armor textures in
 * assets/octoarms/textures/models/armor/.
 *
 * Every box below maps 1:1 onto pixels of the 256x256 mech armor texture,
 * which is what keeps the in-game model and the texture perfectly in sync.
 */
public final class ArmorSpecs {

    /** One cube of a mech suit. */
    public static final class Box {
        public final String part;
        public final int u;
        public final int v;
        public final float w;
        public final float h;
        public final float d;
        public final float ox;
        public final float oy;
        public final float oz;

        Box(String part, int u, int v, float w, float h, float d, float ox, float oy, float oz) {
            this.part = part;
            this.u = u;
            this.v = v;
            this.w = w;
            this.h = h;
            this.d = d;
            this.ox = ox;
            this.oy = oy;
            this.oz = oz;
        }
    }

    public static final int TEXTURE_SIZE = 256;

    /** Pivot offsets of the humanoid rig (must match the vanilla player model). */
    public static final Map<String, float[]> PIVOTS = new HashMap<>();

    private static final Map<String, Box[]> SUITS = new HashMap<>();

    static {
        PIVOTS.put("head", new float[]{0.0F, 24.0F, 0.0F});
        PIVOTS.put("hat", new float[]{0.0F, 24.0F, 0.0F});
        PIVOTS.put("body", new float[]{0.0F, 24.0F, 0.0F});
        PIVOTS.put("right_arm", new float[]{-5.0F, 22.0F, 0.0F});
        PIVOTS.put("left_arm", new float[]{5.0F, 22.0F, 0.0F});
        PIVOTS.put("right_leg", new float[]{-1.9F, 12.0F, 0.0F});
        PIVOTS.put("left_leg", new float[]{1.9F, 12.0F, 0.0F});
        SUITS.put("copper_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 10F, 10F, 10F, -5.0F, -9.0F, -5.0F),
            new Box("head", 42, 0, 9F, 3F, 1F, -4.5F, -7.0F, -5.6F),
            new Box("head", 64, 0, 11F, 2F, 11F, -5.5F, -1.2F, -5.5F),
            new Box("body", 110, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 140, 0, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 150, 0, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 186, 0, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 214, 0, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 226, 0, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 22, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 22, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 22, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 22, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 22, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 122, 22, 6F, 7F, 6F, -3.7F, -1.2F, -3.2F),
            new Box("left_leg", 148, 22, 6F, 7F, 6F, -2.3F, -1.2F, -3.2F),
            new Box("right_leg", 174, 22, 3F, 3F, 3F, -3.0F, 4.6F, -4.6F),
            new Box("left_leg", 188, 22, 3F, 3F, 3F, 0.0F, 4.6F, -4.6F),
            new Box("right_leg", 202, 22, 6F, 7F, 6F, -3.7F, 5.2F, -3.2F),
            new Box("left_leg", 228, 22, 6F, 7F, 6F, -2.3F, 5.2F, -3.2F),
            new Box("right_leg", 0, 37, 1F, 5F, 4F, -4.5F, 5.6F, -2.0F),
            new Box("left_leg", 12, 37, 1F, 5F, 4F, 3.5F, 5.6F, -2.0F),
            new Box("right_leg", 24, 37, 7F, 3F, 9F, -4.0F, 9.0F, -5.0F),
            new Box("left_leg", 58, 37, 7F, 3F, 9F, -3.0F, 9.0F, -5.0F),
            new Box("right_leg", 92, 37, 7F, 2F, 2F, -4.0F, 10.0F, -6.4F),
            new Box("left_leg", 112, 37, 7F, 2F, 2F, -3.0F, 10.0F, -6.4F),
            new Box("right_leg", 132, 37, 2F, 2F, 2F, -3.4F, 10.0F, 3.6F),
            new Box("left_leg", 142, 37, 2F, 2F, 2F, -0.5F, 10.0F, 3.6F),
            new Box("head", 152, 37, 1F, 4F, 1F, -6.4F, -12.0F, 1.0F),
            new Box("head", 158, 37, 1F, 1F, 1F, -6.4F, -12.6F, 1.0F),
            new Box("body", 164, 37, 2F, 2F, 3F, -5.6F, 3.0F, -3.6F),
            new Box("body", 176, 37, 2F, 2F, 3F, 3.6F, 3.0F, -3.6F)
        });
        SUITS.put("iron_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 10F, 10F, 10F, -5.0F, -9.0F, -5.0F),
            new Box("head", 42, 0, 9F, 3F, 1F, -4.5F, -7.0F, -5.6F),
            new Box("head", 64, 0, 11F, 2F, 11F, -5.5F, -1.2F, -5.5F),
            new Box("body", 110, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 140, 0, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 150, 0, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 186, 0, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 214, 0, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 226, 0, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 22, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 22, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 22, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 22, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 22, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 122, 22, 6F, 7F, 6F, -3.7F, -1.2F, -3.2F),
            new Box("left_leg", 148, 22, 6F, 7F, 6F, -2.3F, -1.2F, -3.2F),
            new Box("right_leg", 174, 22, 3F, 3F, 3F, -3.0F, 4.6F, -4.6F),
            new Box("left_leg", 188, 22, 3F, 3F, 3F, 0.0F, 4.6F, -4.6F),
            new Box("right_leg", 202, 22, 6F, 7F, 6F, -3.7F, 5.2F, -3.2F),
            new Box("left_leg", 228, 22, 6F, 7F, 6F, -2.3F, 5.2F, -3.2F),
            new Box("right_leg", 0, 37, 1F, 5F, 4F, -4.5F, 5.6F, -2.0F),
            new Box("left_leg", 12, 37, 1F, 5F, 4F, 3.5F, 5.6F, -2.0F),
            new Box("right_leg", 24, 37, 7F, 3F, 9F, -4.0F, 9.0F, -5.0F),
            new Box("left_leg", 58, 37, 7F, 3F, 9F, -3.0F, 9.0F, -5.0F),
            new Box("right_leg", 92, 37, 7F, 2F, 2F, -4.0F, 10.0F, -6.4F),
            new Box("left_leg", 112, 37, 7F, 2F, 2F, -3.0F, 10.0F, -6.4F),
            new Box("right_leg", 132, 37, 2F, 2F, 2F, -3.4F, 10.0F, 3.6F),
            new Box("left_leg", 142, 37, 2F, 2F, 2F, -0.5F, 10.0F, 3.6F),
            new Box("right_arm", 152, 37, 7F, 3F, 7F, -4.2F, -3.4F, -3.5F),
            new Box("left_arm", 182, 37, 7F, 3F, 7F, -2.8F, -3.4F, -3.5F),
            new Box("body", 212, 37, 9F, 2F, 2F, -4.5F, 6.4F, -3.4F),
            new Box("body", 236, 37, 4F, 6F, 3F, -6.6F, 1.0F, 2.8F),
            new Box("body", 0, 51, 4F, 6F, 3F, 2.6F, 1.0F, 2.8F)
        });
        SUITS.put("gold_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 10F, 10F, 10F, -5.0F, -9.0F, -5.0F),
            new Box("head", 42, 0, 9F, 3F, 1F, -4.5F, -7.0F, -5.6F),
            new Box("head", 64, 0, 11F, 2F, 11F, -5.5F, -1.2F, -5.5F),
            new Box("body", 110, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 140, 0, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 150, 0, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 186, 0, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 214, 0, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 226, 0, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 22, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 22, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 22, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 22, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 22, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 122, 22, 6F, 7F, 6F, -3.7F, -1.2F, -3.2F),
            new Box("left_leg", 148, 22, 6F, 7F, 6F, -2.3F, -1.2F, -3.2F),
            new Box("right_leg", 174, 22, 3F, 3F, 3F, -3.0F, 4.6F, -4.6F),
            new Box("left_leg", 188, 22, 3F, 3F, 3F, 0.0F, 4.6F, -4.6F),
            new Box("right_leg", 202, 22, 6F, 7F, 6F, -3.7F, 5.2F, -3.2F),
            new Box("left_leg", 228, 22, 6F, 7F, 6F, -2.3F, 5.2F, -3.2F),
            new Box("right_leg", 0, 37, 1F, 5F, 4F, -4.5F, 5.6F, -2.0F),
            new Box("left_leg", 12, 37, 1F, 5F, 4F, 3.5F, 5.6F, -2.0F),
            new Box("right_leg", 24, 37, 7F, 3F, 9F, -4.0F, 9.0F, -5.0F),
            new Box("left_leg", 58, 37, 7F, 3F, 9F, -3.0F, 9.0F, -5.0F),
            new Box("right_leg", 92, 37, 7F, 2F, 2F, -4.0F, 10.0F, -6.4F),
            new Box("left_leg", 112, 37, 7F, 2F, 2F, -3.0F, 10.0F, -6.4F),
            new Box("right_leg", 132, 37, 2F, 2F, 2F, -3.4F, 10.0F, 3.6F),
            new Box("left_leg", 142, 37, 2F, 2F, 2F, -0.5F, 10.0F, 3.6F),
            new Box("head", 152, 37, 2F, 4F, 8F, -1.0F, -12.4F, -3.0F),
            new Box("head", 174, 37, 2F, 1F, 8F, -1.0F, -13.0F, -3.0F),
            new Box("body", 196, 37, 11F, 4F, 6F, -5.5F, 9.0F, -3.0F),
            new Box("right_arm", 232, 37, 2F, 6F, 2F, -4.4F, 2.0F, -3.4F),
            new Box("left_arm", 242, 37, 2F, 6F, 2F, 2.4F, 2.0F, -3.4F)
        });
        SUITS.put("diamond_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 10F, 10F, 10F, -5.0F, -9.0F, -5.0F),
            new Box("head", 42, 0, 9F, 3F, 1F, -4.5F, -7.0F, -5.6F),
            new Box("head", 64, 0, 11F, 2F, 11F, -5.5F, -1.2F, -5.5F),
            new Box("body", 110, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 140, 0, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 150, 0, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 186, 0, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 214, 0, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 226, 0, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 22, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 22, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 22, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 22, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 22, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 122, 22, 6F, 7F, 6F, -3.7F, -1.2F, -3.2F),
            new Box("left_leg", 148, 22, 6F, 7F, 6F, -2.3F, -1.2F, -3.2F),
            new Box("right_leg", 174, 22, 3F, 3F, 3F, -3.0F, 4.6F, -4.6F),
            new Box("left_leg", 188, 22, 3F, 3F, 3F, 0.0F, 4.6F, -4.6F),
            new Box("right_leg", 202, 22, 6F, 7F, 6F, -3.7F, 5.2F, -3.2F),
            new Box("left_leg", 228, 22, 6F, 7F, 6F, -2.3F, 5.2F, -3.2F),
            new Box("right_leg", 0, 37, 1F, 5F, 4F, -4.5F, 5.6F, -2.0F),
            new Box("left_leg", 12, 37, 1F, 5F, 4F, 3.5F, 5.6F, -2.0F),
            new Box("right_leg", 24, 37, 7F, 3F, 9F, -4.0F, 9.0F, -5.0F),
            new Box("left_leg", 58, 37, 7F, 3F, 9F, -3.0F, 9.0F, -5.0F),
            new Box("right_leg", 92, 37, 7F, 2F, 2F, -4.0F, 10.0F, -6.4F),
            new Box("left_leg", 112, 37, 7F, 2F, 2F, -3.0F, 10.0F, -6.4F),
            new Box("right_leg", 132, 37, 2F, 2F, 2F, -3.4F, 10.0F, 3.6F),
            new Box("left_leg", 142, 37, 2F, 2F, 2F, -0.5F, 10.0F, 3.6F),
            new Box("body", 152, 37, 2F, 9F, 5F, -7.2F, -1.0F, 2.6F),
            new Box("body", 168, 37, 2F, 9F, 5F, 5.2F, -1.0F, 2.6F),
            new Box("body", 184, 37, 3F, 3F, 3F, -5.4F, 7.0F, 3.4F),
            new Box("body", 198, 37, 3F, 3F, 3F, 2.4F, 7.0F, 3.4F),
            new Box("head", 212, 37, 10F, 1F, 2F, -5.0F, -9.6F, -1.0F)
        });
        SUITS.put("netherite_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 10F, 10F, 10F, -5.0F, -9.0F, -5.0F),
            new Box("head", 42, 0, 9F, 3F, 1F, -4.5F, -7.0F, -5.6F),
            new Box("head", 64, 0, 11F, 2F, 11F, -5.5F, -1.2F, -5.5F),
            new Box("body", 110, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 140, 0, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 150, 0, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 186, 0, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 214, 0, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 226, 0, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 22, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 22, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 22, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 22, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 22, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 122, 22, 6F, 7F, 6F, -3.7F, -1.2F, -3.2F),
            new Box("left_leg", 148, 22, 6F, 7F, 6F, -2.3F, -1.2F, -3.2F),
            new Box("right_leg", 174, 22, 3F, 3F, 3F, -3.0F, 4.6F, -4.6F),
            new Box("left_leg", 188, 22, 3F, 3F, 3F, 0.0F, 4.6F, -4.6F),
            new Box("right_leg", 202, 22, 6F, 7F, 6F, -3.7F, 5.2F, -3.2F),
            new Box("left_leg", 228, 22, 6F, 7F, 6F, -2.3F, 5.2F, -3.2F),
            new Box("right_leg", 0, 37, 1F, 5F, 4F, -4.5F, 5.6F, -2.0F),
            new Box("left_leg", 12, 37, 1F, 5F, 4F, 3.5F, 5.6F, -2.0F),
            new Box("right_leg", 24, 37, 7F, 3F, 9F, -4.0F, 9.0F, -5.0F),
            new Box("left_leg", 58, 37, 7F, 3F, 9F, -3.0F, 9.0F, -5.0F),
            new Box("right_leg", 92, 37, 7F, 2F, 2F, -4.0F, 10.0F, -6.4F),
            new Box("left_leg", 112, 37, 7F, 2F, 2F, -3.0F, 10.0F, -6.4F),
            new Box("right_leg", 132, 37, 2F, 2F, 2F, -3.4F, 10.0F, 3.6F),
            new Box("left_leg", 142, 37, 2F, 2F, 2F, -0.5F, 10.0F, 3.6F),
            new Box("head", 152, 37, 2F, 4F, 2F, -5.6F, -11.4F, -3.0F),
            new Box("head", 162, 37, 2F, 4F, 2F, 3.6F, -11.4F, -3.0F),
            new Box("right_arm", 172, 37, 8F, 4F, 8F, -4.8F, -3.8F, -4.0F),
            new Box("left_arm", 206, 37, 8F, 4F, 8F, -3.2F, -3.8F, -4.0F),
            new Box("body", 240, 37, 4F, 8F, 4F, -6.8F, 0.0F, 3.0F),
            new Box("body", 0, 51, 4F, 8F, 4F, 2.8F, 0.0F, 3.0F),
            new Box("body", 18, 51, 3F, 2F, 3F, -6.2F, -2.4F, 3.0F),
            new Box("body", 32, 51, 3F, 2F, 3F, 3.2F, -2.4F, 3.0F),
            new Box("right_leg", 46, 51, 6F, 4F, 6F, -3.7F, 4.4F, -3.4F),
            new Box("left_leg", 72, 51, 6F, 4F, 6F, -2.3F, 4.4F, -3.4F),
            new Box("right_leg", 98, 51, 2F, 6F, 2F, -5.4F, 2.0F, -1.0F),
            new Box("left_leg", 108, 51, 2F, 6F, 2F, 3.4F, 2.0F, -1.0F)
        });
        SUITS.put("spider_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 10F, 10F, 10F, -5.0F, -9.0F, -5.0F),
            new Box("head", 42, 0, 9F, 3F, 1F, -4.5F, -7.0F, -5.6F),
            new Box("head", 64, 0, 11F, 2F, 11F, -5.5F, -1.2F, -5.5F),
            new Box("body", 110, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 140, 0, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 150, 0, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 186, 0, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 214, 0, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 226, 0, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 22, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 22, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 22, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 22, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 22, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 122, 22, 6F, 7F, 6F, -3.7F, -1.2F, -3.2F),
            new Box("left_leg", 148, 22, 6F, 7F, 6F, -2.3F, -1.2F, -3.2F),
            new Box("right_leg", 174, 22, 3F, 3F, 3F, -3.0F, 4.6F, -4.6F),
            new Box("left_leg", 188, 22, 3F, 3F, 3F, 0.0F, 4.6F, -4.6F),
            new Box("right_leg", 202, 22, 6F, 7F, 6F, -3.7F, 5.2F, -3.2F),
            new Box("left_leg", 228, 22, 6F, 7F, 6F, -2.3F, 5.2F, -3.2F),
            new Box("right_leg", 0, 37, 1F, 5F, 4F, -4.5F, 5.6F, -2.0F),
            new Box("left_leg", 12, 37, 1F, 5F, 4F, 3.5F, 5.6F, -2.0F),
            new Box("right_leg", 24, 37, 7F, 3F, 9F, -4.0F, 9.0F, -5.0F),
            new Box("left_leg", 58, 37, 7F, 3F, 9F, -3.0F, 9.0F, -5.0F),
            new Box("right_leg", 92, 37, 7F, 2F, 2F, -4.0F, 10.0F, -6.4F),
            new Box("left_leg", 112, 37, 7F, 2F, 2F, -3.0F, 10.0F, -6.4F),
            new Box("right_leg", 132, 37, 2F, 2F, 2F, -3.4F, 10.0F, 3.6F),
            new Box("left_leg", 142, 37, 2F, 2F, 2F, -0.5F, 10.0F, 3.6F),
            new Box("head", 152, 37, 9F, 3F, 3F, -4.5F, -2.6F, -5.6F),
            new Box("head", 178, 37, 2F, 4F, 2F, -3.6F, -1.0F, -6.0F),
            new Box("head", 188, 37, 2F, 4F, 2F, 1.6F, -1.0F, -6.0F),
            new Box("head", 198, 37, 1F, 1F, 1F, -3.4F, -7.4F, -5.4F),
            new Box("head", 204, 37, 1F, 1F, 1F, -1.4F, -7.4F, -5.4F),
            new Box("head", 210, 37, 1F, 1F, 1F, 0.6F, -7.4F, -5.4F),
            new Box("head", 216, 37, 1F, 1F, 1F, 2.6F, -7.4F, -5.4F),
            new Box("body", 222, 37, 11F, 8F, 6F, -5.5F, -1.0F, 2.2F),
            new Box("body", 0, 53, 9F, 6F, 5F, -4.5F, 0.0F, 6.0F),
            new Box("body", 30, 53, 2F, 2F, 2F, -5.6F, 2.0F, 10.2F),
            new Box("body", 40, 53, 2F, 2F, 2F, 3.6F, 2.0F, 10.2F),
            new Box("body", 50, 53, 3F, 3F, 3F, -6.6F, 3.0F, 2.6F),
            new Box("body", 64, 53, 3F, 3F, 3F, 3.6F, 3.0F, 2.6F),
            new Box("right_arm", 78, 53, 5F, 5F, 5F, -3.4F, 8.0F, -2.8F),
            new Box("left_arm", 100, 53, 5F, 5F, 5F, -1.6F, 8.0F, -2.8F),
            new Box("right_leg", 122, 53, 2F, 6F, 2F, -5.4F, 1.6F, -1.0F),
            new Box("left_leg", 132, 53, 2F, 6F, 2F, 3.4F, 1.6F, -1.0F),
            new Box("right_leg", 142, 53, 8F, 2F, 8F, -4.4F, 8.6F, -4.4F),
            new Box("left_leg", 176, 53, 8F, 2F, 8F, -3.6F, 8.6F, -4.4F)
        });
    }

    public static Box[] forTier(String tierId) {
        Box[] boxes = SUITS.get(tierId);
        return boxes == null ? SUITS.get("copper_mech") : boxes;
    }

    public static String[] tierIds() {
        return SUITS.keySet().toArray(new String[0]);
    }

    private ArmorSpecs() {
    }
}
