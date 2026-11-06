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
            new Box("right_leg", 122, 22, 10F, 10F, 10F, -9.5F, -2.0F, -5.0F),
            new Box("left_leg", 164, 22, 10F, 10F, 10F, -0.5F, -2.0F, -5.0F),
            new Box("right_leg", 206, 22, 4F, 4F, 4F, -7.0F, 1.0F, -7.0F),
            new Box("left_leg", 224, 22, 4F, 4F, 4F, 1.1F, 1.0F, -7.0F),
            new Box("right_leg", 0, 44, 10F, 6F, 10F, -9.5F, 3.0F, -5.0F),
            new Box("left_leg", 42, 44, 10F, 6F, 10F, -0.5F, 3.0F, -5.0F),
            new Box("right_leg", 84, 44, 2F, 6F, 6F, -10.5F, 4.0F, -3.0F),
            new Box("left_leg", 102, 44, 2F, 6F, 6F, 8.5F, 4.0F, -3.0F),
            new Box("right_leg", 120, 44, 12F, 5F, 20F, -10.0F, 7.0F, -10.0F),
            new Box("left_leg", 186, 44, 12F, 5F, 20F, -2.0F, 7.0F, -10.0F),
            new Box("right_leg", 0, 71, 12F, 3F, 3F, -10.0F, 9.0F, -12.5F),
            new Box("left_leg", 32, 71, 12F, 3F, 3F, -2.0F, 9.0F, -12.5F),
            new Box("right_leg", 64, 71, 3F, 3F, 3F, -8.0F, 9.0F, 9.5F),
            new Box("left_leg", 78, 71, 3F, 3F, 3F, 3.1F, 9.0F, 9.5F),
            new Box("head", 92, 71, 1F, 4F, 1F, -6.4F, -12.0F, 1.0F),
            new Box("head", 98, 71, 1F, 1F, 1F, -6.4F, -12.6F, 1.0F),
            new Box("body", 104, 71, 2F, 2F, 3F, -5.6F, 3.0F, -3.6F),
            new Box("body", 116, 71, 2F, 2F, 3F, 3.6F, 3.0F, -3.6F)
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
            new Box("right_leg", 122, 22, 10F, 10F, 10F, -9.5F, -2.0F, -5.0F),
            new Box("left_leg", 164, 22, 10F, 10F, 10F, -0.5F, -2.0F, -5.0F),
            new Box("right_leg", 206, 22, 4F, 4F, 4F, -7.0F, 1.0F, -7.0F),
            new Box("left_leg", 224, 22, 4F, 4F, 4F, 1.1F, 1.0F, -7.0F),
            new Box("right_leg", 0, 44, 10F, 6F, 10F, -9.5F, 3.0F, -5.0F),
            new Box("left_leg", 42, 44, 10F, 6F, 10F, -0.5F, 3.0F, -5.0F),
            new Box("right_leg", 84, 44, 2F, 6F, 6F, -10.5F, 4.0F, -3.0F),
            new Box("left_leg", 102, 44, 2F, 6F, 6F, 8.5F, 4.0F, -3.0F),
            new Box("right_leg", 120, 44, 12F, 5F, 20F, -10.0F, 7.0F, -10.0F),
            new Box("left_leg", 186, 44, 12F, 5F, 20F, -2.0F, 7.0F, -10.0F),
            new Box("right_leg", 0, 71, 12F, 3F, 3F, -10.0F, 9.0F, -12.5F),
            new Box("left_leg", 32, 71, 12F, 3F, 3F, -2.0F, 9.0F, -12.5F),
            new Box("right_leg", 64, 71, 3F, 3F, 3F, -8.0F, 9.0F, 9.5F),
            new Box("left_leg", 78, 71, 3F, 3F, 3F, 3.1F, 9.0F, 9.5F),
            new Box("right_arm", 92, 71, 7F, 3F, 7F, -4.2F, -3.4F, -3.5F),
            new Box("left_arm", 122, 71, 7F, 3F, 7F, -2.8F, -3.4F, -3.5F),
            new Box("body", 152, 71, 9F, 2F, 2F, -4.5F, 6.4F, -3.4F),
            new Box("body", 176, 71, 4F, 6F, 3F, -6.6F, 1.0F, 2.8F),
            new Box("body", 192, 71, 4F, 6F, 3F, 2.6F, 1.0F, 2.8F)
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
            new Box("right_leg", 122, 22, 10F, 10F, 10F, -9.5F, -2.0F, -5.0F),
            new Box("left_leg", 164, 22, 10F, 10F, 10F, -0.5F, -2.0F, -5.0F),
            new Box("right_leg", 206, 22, 4F, 4F, 4F, -7.0F, 1.0F, -7.0F),
            new Box("left_leg", 224, 22, 4F, 4F, 4F, 1.1F, 1.0F, -7.0F),
            new Box("right_leg", 0, 44, 10F, 6F, 10F, -9.5F, 3.0F, -5.0F),
            new Box("left_leg", 42, 44, 10F, 6F, 10F, -0.5F, 3.0F, -5.0F),
            new Box("right_leg", 84, 44, 2F, 6F, 6F, -10.5F, 4.0F, -3.0F),
            new Box("left_leg", 102, 44, 2F, 6F, 6F, 8.5F, 4.0F, -3.0F),
            new Box("right_leg", 120, 44, 12F, 5F, 20F, -10.0F, 7.0F, -10.0F),
            new Box("left_leg", 186, 44, 12F, 5F, 20F, -2.0F, 7.0F, -10.0F),
            new Box("right_leg", 0, 71, 12F, 3F, 3F, -10.0F, 9.0F, -12.5F),
            new Box("left_leg", 32, 71, 12F, 3F, 3F, -2.0F, 9.0F, -12.5F),
            new Box("right_leg", 64, 71, 3F, 3F, 3F, -8.0F, 9.0F, 9.5F),
            new Box("left_leg", 78, 71, 3F, 3F, 3F, 3.1F, 9.0F, 9.5F),
            new Box("head", 92, 71, 2F, 4F, 8F, -1.0F, -12.4F, -3.0F),
            new Box("head", 114, 71, 2F, 1F, 8F, -1.0F, -13.0F, -3.0F),
            new Box("body", 136, 71, 11F, 4F, 6F, -5.5F, 9.0F, -3.0F),
            new Box("right_arm", 172, 71, 2F, 6F, 2F, -4.4F, 2.0F, -3.4F),
            new Box("left_arm", 182, 71, 2F, 6F, 2F, 2.4F, 2.0F, -3.4F)
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
            new Box("right_leg", 122, 22, 10F, 10F, 10F, -9.5F, -2.0F, -5.0F),
            new Box("left_leg", 164, 22, 10F, 10F, 10F, -0.5F, -2.0F, -5.0F),
            new Box("right_leg", 206, 22, 4F, 4F, 4F, -7.0F, 1.0F, -7.0F),
            new Box("left_leg", 224, 22, 4F, 4F, 4F, 1.1F, 1.0F, -7.0F),
            new Box("right_leg", 0, 44, 10F, 6F, 10F, -9.5F, 3.0F, -5.0F),
            new Box("left_leg", 42, 44, 10F, 6F, 10F, -0.5F, 3.0F, -5.0F),
            new Box("right_leg", 84, 44, 2F, 6F, 6F, -10.5F, 4.0F, -3.0F),
            new Box("left_leg", 102, 44, 2F, 6F, 6F, 8.5F, 4.0F, -3.0F),
            new Box("right_leg", 120, 44, 12F, 5F, 20F, -10.0F, 7.0F, -10.0F),
            new Box("left_leg", 186, 44, 12F, 5F, 20F, -2.0F, 7.0F, -10.0F),
            new Box("right_leg", 0, 71, 12F, 3F, 3F, -10.0F, 9.0F, -12.5F),
            new Box("left_leg", 32, 71, 12F, 3F, 3F, -2.0F, 9.0F, -12.5F),
            new Box("right_leg", 64, 71, 3F, 3F, 3F, -8.0F, 9.0F, 9.5F),
            new Box("left_leg", 78, 71, 3F, 3F, 3F, 3.1F, 9.0F, 9.5F),
            new Box("body", 92, 71, 2F, 9F, 5F, -7.2F, -1.0F, 2.6F),
            new Box("body", 108, 71, 2F, 9F, 5F, 5.2F, -1.0F, 2.6F),
            new Box("body", 124, 71, 3F, 3F, 3F, -5.4F, 7.0F, 3.4F),
            new Box("body", 138, 71, 3F, 3F, 3F, 2.4F, 7.0F, 3.4F),
            new Box("head", 152, 71, 10F, 1F, 2F, -5.0F, -9.6F, -1.0F)
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
            new Box("right_leg", 122, 22, 10F, 10F, 10F, -9.5F, -2.0F, -5.0F),
            new Box("left_leg", 164, 22, 10F, 10F, 10F, -0.5F, -2.0F, -5.0F),
            new Box("right_leg", 206, 22, 4F, 4F, 4F, -7.0F, 1.0F, -7.0F),
            new Box("left_leg", 224, 22, 4F, 4F, 4F, 1.1F, 1.0F, -7.0F),
            new Box("right_leg", 0, 44, 10F, 6F, 10F, -9.5F, 3.0F, -5.0F),
            new Box("left_leg", 42, 44, 10F, 6F, 10F, -0.5F, 3.0F, -5.0F),
            new Box("right_leg", 84, 44, 2F, 6F, 6F, -10.5F, 4.0F, -3.0F),
            new Box("left_leg", 102, 44, 2F, 6F, 6F, 8.5F, 4.0F, -3.0F),
            new Box("right_leg", 120, 44, 12F, 5F, 20F, -10.0F, 7.0F, -10.0F),
            new Box("left_leg", 186, 44, 12F, 5F, 20F, -2.0F, 7.0F, -10.0F),
            new Box("right_leg", 0, 71, 12F, 3F, 3F, -10.0F, 9.0F, -12.5F),
            new Box("left_leg", 32, 71, 12F, 3F, 3F, -2.0F, 9.0F, -12.5F),
            new Box("right_leg", 64, 71, 3F, 3F, 3F, -8.0F, 9.0F, 9.5F),
            new Box("left_leg", 78, 71, 3F, 3F, 3F, 3.1F, 9.0F, 9.5F),
            new Box("head", 92, 71, 2F, 4F, 2F, -5.6F, -11.4F, -3.0F),
            new Box("head", 102, 71, 2F, 4F, 2F, 3.6F, -11.4F, -3.0F),
            new Box("right_arm", 112, 71, 8F, 4F, 8F, -4.8F, -3.8F, -4.0F),
            new Box("left_arm", 146, 71, 8F, 4F, 8F, -3.2F, -3.8F, -4.0F),
            new Box("body", 180, 71, 4F, 8F, 4F, -6.8F, 0.0F, 3.0F),
            new Box("body", 198, 71, 4F, 8F, 4F, 2.8F, 0.0F, 3.0F),
            new Box("body", 216, 71, 3F, 2F, 3F, -6.2F, -2.4F, 3.0F),
            new Box("body", 230, 71, 3F, 2F, 3F, 3.2F, -2.4F, 3.0F),
            new Box("right_leg", 0, 85, 10F, 6F, 4F, -9.5F, 3.0F, -7.0F),
            new Box("left_leg", 30, 85, 10F, 6F, 4F, -0.5F, 3.0F, -7.0F),
            new Box("right_leg", 60, 85, 3F, 8F, 3F, -12.0F, 0.0F, -1.5F),
            new Box("left_leg", 74, 85, 3F, 8F, 3F, 9.0F, 0.0F, -1.5F)
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
            new Box("right_leg", 122, 22, 10F, 10F, 10F, -9.5F, -2.0F, -5.0F),
            new Box("left_leg", 164, 22, 10F, 10F, 10F, -0.5F, -2.0F, -5.0F),
            new Box("right_leg", 206, 22, 4F, 4F, 4F, -7.0F, 1.0F, -7.0F),
            new Box("left_leg", 224, 22, 4F, 4F, 4F, 1.1F, 1.0F, -7.0F),
            new Box("right_leg", 0, 44, 10F, 6F, 10F, -9.5F, 3.0F, -5.0F),
            new Box("left_leg", 42, 44, 10F, 6F, 10F, -0.5F, 3.0F, -5.0F),
            new Box("right_leg", 84, 44, 2F, 6F, 6F, -10.5F, 4.0F, -3.0F),
            new Box("left_leg", 102, 44, 2F, 6F, 6F, 8.5F, 4.0F, -3.0F),
            new Box("right_leg", 120, 44, 12F, 5F, 20F, -10.0F, 7.0F, -10.0F),
            new Box("left_leg", 186, 44, 12F, 5F, 20F, -2.0F, 7.0F, -10.0F),
            new Box("right_leg", 0, 71, 12F, 3F, 3F, -10.0F, 9.0F, -12.5F),
            new Box("left_leg", 32, 71, 12F, 3F, 3F, -2.0F, 9.0F, -12.5F),
            new Box("right_leg", 64, 71, 3F, 3F, 3F, -8.0F, 9.0F, 9.5F),
            new Box("left_leg", 78, 71, 3F, 3F, 3F, 3.1F, 9.0F, 9.5F),
            new Box("head", 92, 71, 9F, 3F, 3F, -4.5F, -2.6F, -5.6F),
            new Box("head", 118, 71, 2F, 4F, 2F, -3.6F, -1.0F, -6.0F),
            new Box("head", 128, 71, 2F, 4F, 2F, 1.6F, -1.0F, -6.0F),
            new Box("head", 138, 71, 1F, 1F, 1F, -3.4F, -7.4F, -5.4F),
            new Box("head", 144, 71, 1F, 1F, 1F, -1.4F, -7.4F, -5.4F),
            new Box("head", 150, 71, 1F, 1F, 1F, 0.6F, -7.4F, -5.4F),
            new Box("head", 156, 71, 1F, 1F, 1F, 2.6F, -7.4F, -5.4F),
            new Box("body", 162, 71, 11F, 8F, 6F, -5.5F, -1.0F, 2.2F),
            new Box("body", 198, 71, 9F, 6F, 5F, -4.5F, 0.0F, 6.0F),
            new Box("body", 228, 71, 2F, 2F, 2F, -5.6F, 2.0F, 10.2F),
            new Box("body", 238, 71, 2F, 2F, 2F, 3.6F, 2.0F, 10.2F),
            new Box("body", 0, 87, 3F, 3F, 3F, -6.6F, 3.0F, 2.6F),
            new Box("body", 14, 87, 3F, 3F, 3F, 3.6F, 3.0F, 2.6F),
            new Box("right_arm", 28, 87, 5F, 5F, 5F, -3.4F, 8.0F, -2.8F),
            new Box("left_arm", 50, 87, 5F, 5F, 5F, -1.6F, 8.0F, -2.8F),
            new Box("right_leg", 72, 87, 3F, 8F, 3F, -12.0F, -0.4F, -1.5F),
            new Box("left_leg", 86, 87, 3F, 8F, 3F, 9.0F, -0.4F, -1.5F),
            new Box("right_leg", 100, 87, 12F, 3F, 12F, -10.5F, 6.5F, -6.0F),
            new Box("left_leg", 150, 87, 12F, 3F, 12F, -1.5F, 6.5F, -6.0F)
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
