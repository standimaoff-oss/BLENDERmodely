package com.octoarms.client.model;

import java.util.HashMap;
import java.util.Map;

/**
 * GENERATED FILE - do not edit by hand.
 * Produced by tools/gen_armor.py together with the armor textures in
 * assets/octoarms/textures/models/armor/.
 *
 * Every box below maps 1:1 onto pixels of the 128x128 mech armor texture,
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

    public static final int TEXTURE_SIZE = 128;

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
            new Box("head", 0, 0, 9F, 9F, 9F, -4.5F, -8.5F, -4.5F),
            new Box("head", 38, 0, 8F, 3F, 1F, -4.0F, -6.6F, -5.1F),
            new Box("head", 58, 0, 10F, 2F, 10F, -5.0F, -1.4F, -5.0F),
            new Box("body", 100, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 0, 20, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 10, 20, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 46, 20, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 74, 20, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 86, 20, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 34, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 34, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 34, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 34, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 34, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 0, 48, 5F, 7F, 5F, -2.7F, -0.6F, -2.5F),
            new Box("right_leg", 22, 48, 6F, 6F, 6F, -3.1F, 5.8F, -3.0F),
            new Box("right_leg", 48, 48, 6F, 3F, 8F, -3.1F, 10.2F, -3.6F),
            new Box("left_leg", 78, 48, 5F, 7F, 5F, -2.3F, -0.6F, -2.5F),
            new Box("left_leg", 100, 48, 6F, 6F, 6F, -2.9F, 5.8F, -3.0F),
            new Box("left_leg", 0, 62, 6F, 3F, 8F, -2.9F, 10.2F, -3.6F),
            new Box("head", 30, 62, 1F, 4F, 1F, -6.4F, -12.0F, 1.0F),
            new Box("head", 36, 62, 1F, 1F, 1F, -6.4F, -12.6F, 1.0F),
            new Box("body", 42, 62, 2F, 2F, 3F, -5.6F, 3.0F, -3.6F),
            new Box("body", 54, 62, 2F, 2F, 3F, 3.6F, 3.0F, -3.6F)
        });
        SUITS.put("iron_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 9F, 9F, 9F, -4.5F, -8.5F, -4.5F),
            new Box("head", 38, 0, 8F, 3F, 1F, -4.0F, -6.6F, -5.1F),
            new Box("head", 58, 0, 10F, 2F, 10F, -5.0F, -1.4F, -5.0F),
            new Box("body", 100, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 0, 20, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 10, 20, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 46, 20, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 74, 20, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 86, 20, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 34, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 34, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 34, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 34, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 34, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 0, 48, 5F, 7F, 5F, -2.7F, -0.6F, -2.5F),
            new Box("right_leg", 22, 48, 6F, 6F, 6F, -3.1F, 5.8F, -3.0F),
            new Box("right_leg", 48, 48, 6F, 3F, 8F, -3.1F, 10.2F, -3.6F),
            new Box("left_leg", 78, 48, 5F, 7F, 5F, -2.3F, -0.6F, -2.5F),
            new Box("left_leg", 100, 48, 6F, 6F, 6F, -2.9F, 5.8F, -3.0F),
            new Box("left_leg", 0, 62, 6F, 3F, 8F, -2.9F, 10.2F, -3.6F),
            new Box("right_arm", 30, 62, 7F, 3F, 7F, -4.2F, -3.4F, -3.5F),
            new Box("left_arm", 60, 62, 7F, 3F, 7F, -2.8F, -3.4F, -3.5F),
            new Box("body", 90, 62, 9F, 2F, 2F, -4.5F, 6.4F, -3.4F),
            new Box("body", 114, 62, 4F, 6F, 3F, -6.6F, 1.0F, 2.8F),
            new Box("body", 0, 75, 4F, 6F, 3F, 2.6F, 1.0F, 2.8F)
        });
        SUITS.put("gold_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 9F, 9F, 9F, -4.5F, -8.5F, -4.5F),
            new Box("head", 38, 0, 8F, 3F, 1F, -4.0F, -6.6F, -5.1F),
            new Box("head", 58, 0, 10F, 2F, 10F, -5.0F, -1.4F, -5.0F),
            new Box("body", 100, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 0, 20, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 10, 20, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 46, 20, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 74, 20, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 86, 20, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 34, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 34, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 34, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 34, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 34, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 0, 48, 5F, 7F, 5F, -2.7F, -0.6F, -2.5F),
            new Box("right_leg", 22, 48, 6F, 6F, 6F, -3.1F, 5.8F, -3.0F),
            new Box("right_leg", 48, 48, 6F, 3F, 8F, -3.1F, 10.2F, -3.6F),
            new Box("left_leg", 78, 48, 5F, 7F, 5F, -2.3F, -0.6F, -2.5F),
            new Box("left_leg", 100, 48, 6F, 6F, 6F, -2.9F, 5.8F, -3.0F),
            new Box("left_leg", 0, 62, 6F, 3F, 8F, -2.9F, 10.2F, -3.6F),
            new Box("head", 30, 62, 2F, 4F, 8F, -1.0F, -12.4F, -3.0F),
            new Box("head", 52, 62, 2F, 1F, 8F, -1.0F, -13.0F, -3.0F),
            new Box("body", 74, 62, 11F, 4F, 6F, -5.5F, 9.0F, -3.0F),
            new Box("right_arm", 110, 62, 2F, 6F, 2F, -4.4F, 2.0F, -3.4F),
            new Box("left_arm", 120, 62, 2F, 6F, 2F, 2.4F, 2.0F, -3.4F)
        });
        SUITS.put("diamond_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 9F, 9F, 9F, -4.5F, -8.5F, -4.5F),
            new Box("head", 38, 0, 8F, 3F, 1F, -4.0F, -6.6F, -5.1F),
            new Box("head", 58, 0, 10F, 2F, 10F, -5.0F, -1.4F, -5.0F),
            new Box("body", 100, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 0, 20, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 10, 20, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 46, 20, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 74, 20, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 86, 20, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 34, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 34, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 34, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 34, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 34, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 0, 48, 5F, 7F, 5F, -2.7F, -0.6F, -2.5F),
            new Box("right_leg", 22, 48, 6F, 6F, 6F, -3.1F, 5.8F, -3.0F),
            new Box("right_leg", 48, 48, 6F, 3F, 8F, -3.1F, 10.2F, -3.6F),
            new Box("left_leg", 78, 48, 5F, 7F, 5F, -2.3F, -0.6F, -2.5F),
            new Box("left_leg", 100, 48, 6F, 6F, 6F, -2.9F, 5.8F, -3.0F),
            new Box("left_leg", 0, 62, 6F, 3F, 8F, -2.9F, 10.2F, -3.6F),
            new Box("body", 30, 62, 2F, 9F, 5F, -7.2F, -1.0F, 2.6F),
            new Box("body", 46, 62, 2F, 9F, 5F, 5.2F, -1.0F, 2.6F),
            new Box("body", 62, 62, 3F, 3F, 3F, -5.4F, 7.0F, 3.4F),
            new Box("body", 76, 62, 3F, 3F, 3F, 2.4F, 7.0F, 3.4F),
            new Box("head", 90, 62, 10F, 1F, 2F, -5.0F, -9.6F, -1.0F)
        });
        SUITS.put("netherite_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 9F, 9F, 9F, -4.5F, -8.5F, -4.5F),
            new Box("head", 38, 0, 8F, 3F, 1F, -4.0F, -6.6F, -5.1F),
            new Box("head", 58, 0, 10F, 2F, 10F, -5.0F, -1.4F, -5.0F),
            new Box("body", 100, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 0, 20, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 10, 20, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 46, 20, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 74, 20, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 86, 20, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 34, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 34, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 34, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 34, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 34, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 0, 48, 5F, 7F, 5F, -2.7F, -0.6F, -2.5F),
            new Box("right_leg", 22, 48, 6F, 6F, 6F, -3.1F, 5.8F, -3.0F),
            new Box("right_leg", 48, 48, 6F, 3F, 8F, -3.1F, 10.2F, -3.6F),
            new Box("left_leg", 78, 48, 5F, 7F, 5F, -2.3F, -0.6F, -2.5F),
            new Box("left_leg", 100, 48, 6F, 6F, 6F, -2.9F, 5.8F, -3.0F),
            new Box("left_leg", 0, 62, 6F, 3F, 8F, -2.9F, 10.2F, -3.6F),
            new Box("head", 30, 62, 2F, 4F, 2F, -5.6F, -11.4F, -3.0F),
            new Box("head", 40, 62, 2F, 4F, 2F, 3.6F, -11.4F, -3.0F),
            new Box("right_arm", 50, 62, 8F, 4F, 8F, -4.8F, -3.8F, -4.0F),
            new Box("left_arm", 84, 62, 8F, 4F, 8F, -3.2F, -3.8F, -4.0F),
            new Box("body", 0, 76, 4F, 8F, 4F, -6.8F, 0.0F, 3.0F),
            new Box("body", 18, 76, 4F, 8F, 4F, 2.8F, 0.0F, 3.0F),
            new Box("body", 36, 76, 3F, 2F, 3F, -6.2F, -2.4F, 3.0F),
            new Box("body", 50, 76, 3F, 2F, 3F, 3.2F, -2.4F, 3.0F),
            new Box("right_leg", 64, 76, 7F, 4F, 7F, -3.8F, 4.6F, -3.5F),
            new Box("left_leg", 94, 76, 7F, 4F, 7F, -3.2F, 4.6F, -3.5F)
        });
        SUITS.put("spider_mech", new Box[]{
            new Box("hat", 0, 0, 0F, 0F, 0F, 0.0F, 0.0F, 0.0F),
            new Box("head", 0, 0, 9F, 9F, 9F, -4.5F, -8.5F, -4.5F),
            new Box("head", 38, 0, 8F, 3F, 1F, -4.0F, -6.6F, -5.1F),
            new Box("head", 58, 0, 10F, 2F, 10F, -5.0F, -1.4F, -5.0F),
            new Box("body", 100, 0, 9F, 13F, 5F, -4.5F, -0.6F, -2.5F),
            new Box("body", 0, 20, 3F, 3F, 1F, -1.5F, 3.2F, -3.2F),
            new Box("body", 10, 20, 10F, 3F, 7F, -5.0F, -1.8F, -3.5F),
            new Box("body", 46, 20, 9F, 8F, 4F, -4.5F, 0.6F, 2.4F),
            new Box("body", 74, 20, 3F, 3F, 2F, -1.5F, 9.4F, 3.2F),
            new Box("right_arm", 86, 20, 6F, 5F, 6F, -3.6F, -2.8F, -3.0F),
            new Box("right_arm", 0, 34, 5F, 7F, 5F, -3.2F, 1.6F, -2.5F),
            new Box("right_arm", 22, 34, 6F, 6F, 6F, -3.6F, 7.6F, -3.0F),
            new Box("left_arm", 48, 34, 6F, 5F, 6F, -2.4F, -2.8F, -3.0F),
            new Box("left_arm", 74, 34, 5F, 7F, 5F, -1.8F, 1.6F, -2.5F),
            new Box("left_arm", 96, 34, 6F, 6F, 6F, -2.4F, 7.6F, -3.0F),
            new Box("right_leg", 0, 48, 5F, 7F, 5F, -2.7F, -0.6F, -2.5F),
            new Box("right_leg", 22, 48, 6F, 6F, 6F, -3.1F, 5.8F, -3.0F),
            new Box("right_leg", 48, 48, 6F, 3F, 8F, -3.1F, 10.2F, -3.6F),
            new Box("left_leg", 78, 48, 5F, 7F, 5F, -2.3F, -0.6F, -2.5F),
            new Box("left_leg", 100, 48, 6F, 6F, 6F, -2.9F, 5.8F, -3.0F),
            new Box("left_leg", 0, 62, 6F, 3F, 8F, -2.9F, 10.2F, -3.6F),
            new Box("head", 30, 62, 9F, 3F, 3F, -4.5F, -2.6F, -5.6F),
            new Box("head", 56, 62, 2F, 4F, 2F, -3.6F, -1.0F, -6.0F),
            new Box("head", 66, 62, 2F, 4F, 2F, 1.6F, -1.0F, -6.0F),
            new Box("head", 76, 62, 1F, 1F, 1F, -3.4F, -7.4F, -5.4F),
            new Box("head", 82, 62, 1F, 1F, 1F, -1.4F, -7.4F, -5.4F),
            new Box("head", 88, 62, 1F, 1F, 1F, 0.6F, -7.4F, -5.4F),
            new Box("head", 94, 62, 1F, 1F, 1F, 2.6F, -7.4F, -5.4F),
            new Box("body", 0, 75, 11F, 8F, 6F, -5.5F, -1.0F, 2.2F),
            new Box("body", 36, 75, 9F, 6F, 5F, -4.5F, 0.0F, 6.0F),
            new Box("body", 66, 75, 2F, 2F, 2F, -5.6F, 2.0F, 10.2F),
            new Box("body", 76, 75, 2F, 2F, 2F, 3.6F, 2.0F, 10.2F),
            new Box("body", 86, 75, 3F, 3F, 3F, -6.6F, 3.0F, 2.6F),
            new Box("body", 100, 75, 3F, 3F, 3F, 3.6F, 3.0F, 2.6F),
            new Box("right_arm", 0, 91, 5F, 5F, 5F, -3.4F, 8.0F, -2.8F),
            new Box("left_arm", 22, 91, 5F, 5F, 5F, -1.6F, 8.0F, -2.8F)
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
