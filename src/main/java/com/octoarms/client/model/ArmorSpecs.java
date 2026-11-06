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
            new Box("right_leg", 122, 22, 7F, 8F, 7F, -5.6F, -1.5F, -3.5F),
            new Box("left_leg", 152, 22, 7F, 8F, 7F, -1.4F, -1.5F, -3.5F),
            new Box("right_leg", 182, 22, 3F, 3F, 3F, -4.5F, 4.8F, -5.0F),
            new Box("left_leg", 196, 22, 3F, 3F, 3F, -0.4F, 4.8F, -5.0F),
            new Box("right_leg", 210, 22, 7F, 6F, 7F, -5.6F, 5.5F, -3.5F),
            new Box("left_leg", 0, 39, 7F, 6F, 7F, -1.4F, 5.5F, -3.5F),
            new Box("right_leg", 30, 39, 1F, 4F, 4F, -6.4F, 6.0F, -2.0F),
            new Box("left_leg", 42, 39, 1F, 4F, 4F, 5.4F, 6.0F, -2.0F),
            new Box("right_leg", 54, 39, 9F, 4F, 11F, -6.5F, 8.0F, -6.0F),
            new Box("left_leg", 96, 39, 9F, 4F, 11F, -2.5F, 8.0F, -6.0F),
            new Box("right_leg", 138, 39, 9F, 2F, 2F, -6.5F, 10.0F, -7.4F),
            new Box("left_leg", 162, 39, 9F, 2F, 2F, -2.5F, 10.0F, -7.4F),
            new Box("right_leg", 186, 39, 2F, 2F, 2F, -5.0F, 10.0F, 4.6F),
            new Box("left_leg", 196, 39, 2F, 2F, 2F, 3.0F, 10.0F, 4.6F),
            new Box("head", 206, 39, 1F, 4F, 1F, -6.4F, -12.0F, 1.0F),
            new Box("head", 212, 39, 1F, 1F, 1F, -6.4F, -12.6F, 1.0F),
            new Box("body", 218, 39, 2F, 2F, 3F, -5.6F, 3.0F, -3.6F),
            new Box("body", 230, 39, 2F, 2F, 3F, 3.6F, 3.0F, -3.6F)
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
            new Box("right_leg", 122, 22, 7F, 8F, 7F, -5.6F, -1.5F, -3.5F),
            new Box("left_leg", 152, 22, 7F, 8F, 7F, -1.4F, -1.5F, -3.5F),
            new Box("right_leg", 182, 22, 3F, 3F, 3F, -4.5F, 4.8F, -5.0F),
            new Box("left_leg", 196, 22, 3F, 3F, 3F, -0.4F, 4.8F, -5.0F),
            new Box("right_leg", 210, 22, 7F, 6F, 7F, -5.6F, 5.5F, -3.5F),
            new Box("left_leg", 0, 39, 7F, 6F, 7F, -1.4F, 5.5F, -3.5F),
            new Box("right_leg", 30, 39, 1F, 4F, 4F, -6.4F, 6.0F, -2.0F),
            new Box("left_leg", 42, 39, 1F, 4F, 4F, 5.4F, 6.0F, -2.0F),
            new Box("right_leg", 54, 39, 9F, 4F, 11F, -6.5F, 8.0F, -6.0F),
            new Box("left_leg", 96, 39, 9F, 4F, 11F, -2.5F, 8.0F, -6.0F),
            new Box("right_leg", 138, 39, 9F, 2F, 2F, -6.5F, 10.0F, -7.4F),
            new Box("left_leg", 162, 39, 9F, 2F, 2F, -2.5F, 10.0F, -7.4F),
            new Box("right_leg", 186, 39, 2F, 2F, 2F, -5.0F, 10.0F, 4.6F),
            new Box("left_leg", 196, 39, 2F, 2F, 2F, 3.0F, 10.0F, 4.6F),
            new Box("right_arm", 206, 39, 7F, 3F, 7F, -4.2F, -3.4F, -3.5F),
            new Box("left_arm", 0, 56, 7F, 3F, 7F, -2.8F, -3.4F, -3.5F),
            new Box("body", 30, 56, 9F, 2F, 2F, -4.5F, 6.4F, -3.4F),
            new Box("body", 54, 56, 4F, 6F, 3F, -6.6F, 1.0F, 2.8F),
            new Box("body", 70, 56, 4F, 6F, 3F, 2.6F, 1.0F, 2.8F)
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
            new Box("right_leg", 122, 22, 7F, 8F, 7F, -5.6F, -1.5F, -3.5F),
            new Box("left_leg", 152, 22, 7F, 8F, 7F, -1.4F, -1.5F, -3.5F),
            new Box("right_leg", 182, 22, 3F, 3F, 3F, -4.5F, 4.8F, -5.0F),
            new Box("left_leg", 196, 22, 3F, 3F, 3F, -0.4F, 4.8F, -5.0F),
            new Box("right_leg", 210, 22, 7F, 6F, 7F, -5.6F, 5.5F, -3.5F),
            new Box("left_leg", 0, 39, 7F, 6F, 7F, -1.4F, 5.5F, -3.5F),
            new Box("right_leg", 30, 39, 1F, 4F, 4F, -6.4F, 6.0F, -2.0F),
            new Box("left_leg", 42, 39, 1F, 4F, 4F, 5.4F, 6.0F, -2.0F),
            new Box("right_leg", 54, 39, 9F, 4F, 11F, -6.5F, 8.0F, -6.0F),
            new Box("left_leg", 96, 39, 9F, 4F, 11F, -2.5F, 8.0F, -6.0F),
            new Box("right_leg", 138, 39, 9F, 2F, 2F, -6.5F, 10.0F, -7.4F),
            new Box("left_leg", 162, 39, 9F, 2F, 2F, -2.5F, 10.0F, -7.4F),
            new Box("right_leg", 186, 39, 2F, 2F, 2F, -5.0F, 10.0F, 4.6F),
            new Box("left_leg", 196, 39, 2F, 2F, 2F, 3.0F, 10.0F, 4.6F),
            new Box("head", 206, 39, 2F, 4F, 8F, -1.0F, -12.4F, -3.0F),
            new Box("head", 228, 39, 2F, 1F, 8F, -1.0F, -13.0F, -3.0F),
            new Box("body", 0, 56, 11F, 4F, 6F, -5.5F, 9.0F, -3.0F),
            new Box("right_arm", 36, 56, 2F, 6F, 2F, -4.4F, 2.0F, -3.4F),
            new Box("left_arm", 46, 56, 2F, 6F, 2F, 2.4F, 2.0F, -3.4F)
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
            new Box("right_leg", 122, 22, 7F, 8F, 7F, -5.6F, -1.5F, -3.5F),
            new Box("left_leg", 152, 22, 7F, 8F, 7F, -1.4F, -1.5F, -3.5F),
            new Box("right_leg", 182, 22, 3F, 3F, 3F, -4.5F, 4.8F, -5.0F),
            new Box("left_leg", 196, 22, 3F, 3F, 3F, -0.4F, 4.8F, -5.0F),
            new Box("right_leg", 210, 22, 7F, 6F, 7F, -5.6F, 5.5F, -3.5F),
            new Box("left_leg", 0, 39, 7F, 6F, 7F, -1.4F, 5.5F, -3.5F),
            new Box("right_leg", 30, 39, 1F, 4F, 4F, -6.4F, 6.0F, -2.0F),
            new Box("left_leg", 42, 39, 1F, 4F, 4F, 5.4F, 6.0F, -2.0F),
            new Box("right_leg", 54, 39, 9F, 4F, 11F, -6.5F, 8.0F, -6.0F),
            new Box("left_leg", 96, 39, 9F, 4F, 11F, -2.5F, 8.0F, -6.0F),
            new Box("right_leg", 138, 39, 9F, 2F, 2F, -6.5F, 10.0F, -7.4F),
            new Box("left_leg", 162, 39, 9F, 2F, 2F, -2.5F, 10.0F, -7.4F),
            new Box("right_leg", 186, 39, 2F, 2F, 2F, -5.0F, 10.0F, 4.6F),
            new Box("left_leg", 196, 39, 2F, 2F, 2F, 3.0F, 10.0F, 4.6F),
            new Box("body", 206, 39, 2F, 9F, 5F, -7.2F, -1.0F, 2.6F),
            new Box("body", 222, 39, 2F, 9F, 5F, 5.2F, -1.0F, 2.6F),
            new Box("body", 238, 39, 3F, 3F, 3F, -5.4F, 7.0F, 3.4F),
            new Box("body", 0, 56, 3F, 3F, 3F, 2.4F, 7.0F, 3.4F),
            new Box("head", 14, 56, 10F, 1F, 2F, -5.0F, -9.6F, -1.0F)
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
            new Box("right_leg", 122, 22, 7F, 8F, 7F, -5.6F, -1.5F, -3.5F),
            new Box("left_leg", 152, 22, 7F, 8F, 7F, -1.4F, -1.5F, -3.5F),
            new Box("right_leg", 182, 22, 3F, 3F, 3F, -4.5F, 4.8F, -5.0F),
            new Box("left_leg", 196, 22, 3F, 3F, 3F, -0.4F, 4.8F, -5.0F),
            new Box("right_leg", 210, 22, 7F, 6F, 7F, -5.6F, 5.5F, -3.5F),
            new Box("left_leg", 0, 39, 7F, 6F, 7F, -1.4F, 5.5F, -3.5F),
            new Box("right_leg", 30, 39, 1F, 4F, 4F, -6.4F, 6.0F, -2.0F),
            new Box("left_leg", 42, 39, 1F, 4F, 4F, 5.4F, 6.0F, -2.0F),
            new Box("right_leg", 54, 39, 9F, 4F, 11F, -6.5F, 8.0F, -6.0F),
            new Box("left_leg", 96, 39, 9F, 4F, 11F, -2.5F, 8.0F, -6.0F),
            new Box("right_leg", 138, 39, 9F, 2F, 2F, -6.5F, 10.0F, -7.4F),
            new Box("left_leg", 162, 39, 9F, 2F, 2F, -2.5F, 10.0F, -7.4F),
            new Box("right_leg", 186, 39, 2F, 2F, 2F, -5.0F, 10.0F, 4.6F),
            new Box("left_leg", 196, 39, 2F, 2F, 2F, 3.0F, 10.0F, 4.6F),
            new Box("head", 206, 39, 2F, 4F, 2F, -5.6F, -11.4F, -3.0F),
            new Box("head", 216, 39, 2F, 4F, 2F, 3.6F, -11.4F, -3.0F),
            new Box("right_arm", 0, 56, 8F, 4F, 8F, -4.8F, -3.8F, -4.0F),
            new Box("left_arm", 34, 56, 8F, 4F, 8F, -3.2F, -3.8F, -4.0F),
            new Box("body", 68, 56, 4F, 8F, 4F, -6.8F, 0.0F, 3.0F),
            new Box("body", 86, 56, 4F, 8F, 4F, 2.8F, 0.0F, 3.0F),
            new Box("body", 104, 56, 3F, 2F, 3F, -6.2F, -2.4F, 3.0F),
            new Box("body", 118, 56, 3F, 2F, 3F, 3.2F, -2.4F, 3.0F),
            new Box("right_leg", 132, 56, 6F, 4F, 6F, -5.4F, 4.4F, -3.4F),
            new Box("left_leg", 158, 56, 6F, 4F, 6F, -0.6F, 4.4F, -3.4F),
            new Box("right_leg", 184, 56, 2F, 6F, 2F, -7.2F, 2.0F, -1.0F),
            new Box("left_leg", 194, 56, 2F, 6F, 2F, 5.2F, 2.0F, -1.0F)
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
            new Box("right_leg", 122, 22, 7F, 8F, 7F, -5.6F, -1.5F, -3.5F),
            new Box("left_leg", 152, 22, 7F, 8F, 7F, -1.4F, -1.5F, -3.5F),
            new Box("right_leg", 182, 22, 3F, 3F, 3F, -4.5F, 4.8F, -5.0F),
            new Box("left_leg", 196, 22, 3F, 3F, 3F, -0.4F, 4.8F, -5.0F),
            new Box("right_leg", 210, 22, 7F, 6F, 7F, -5.6F, 5.5F, -3.5F),
            new Box("left_leg", 0, 39, 7F, 6F, 7F, -1.4F, 5.5F, -3.5F),
            new Box("right_leg", 30, 39, 1F, 4F, 4F, -6.4F, 6.0F, -2.0F),
            new Box("left_leg", 42, 39, 1F, 4F, 4F, 5.4F, 6.0F, -2.0F),
            new Box("right_leg", 54, 39, 9F, 4F, 11F, -6.5F, 8.0F, -6.0F),
            new Box("left_leg", 96, 39, 9F, 4F, 11F, -2.5F, 8.0F, -6.0F),
            new Box("right_leg", 138, 39, 9F, 2F, 2F, -6.5F, 10.0F, -7.4F),
            new Box("left_leg", 162, 39, 9F, 2F, 2F, -2.5F, 10.0F, -7.4F),
            new Box("right_leg", 186, 39, 2F, 2F, 2F, -5.0F, 10.0F, 4.6F),
            new Box("left_leg", 196, 39, 2F, 2F, 2F, 3.0F, 10.0F, 4.6F),
            new Box("head", 206, 39, 9F, 3F, 3F, -4.5F, -2.6F, -5.6F),
            new Box("head", 232, 39, 2F, 4F, 2F, -3.6F, -1.0F, -6.0F),
            new Box("head", 242, 39, 2F, 4F, 2F, 1.6F, -1.0F, -6.0F),
            new Box("head", 252, 39, 1F, 1F, 1F, -3.4F, -7.4F, -5.4F),
            new Box("head", 0, 56, 1F, 1F, 1F, -1.4F, -7.4F, -5.4F),
            new Box("head", 6, 56, 1F, 1F, 1F, 0.6F, -7.4F, -5.4F),
            new Box("head", 12, 56, 1F, 1F, 1F, 2.6F, -7.4F, -5.4F),
            new Box("body", 18, 56, 11F, 8F, 6F, -5.5F, -1.0F, 2.2F),
            new Box("body", 54, 56, 9F, 6F, 5F, -4.5F, 0.0F, 6.0F),
            new Box("body", 84, 56, 2F, 2F, 2F, -5.6F, 2.0F, 10.2F),
            new Box("body", 94, 56, 2F, 2F, 2F, 3.6F, 2.0F, 10.2F),
            new Box("body", 104, 56, 3F, 3F, 3F, -6.6F, 3.0F, 2.6F),
            new Box("body", 118, 56, 3F, 3F, 3F, 3.6F, 3.0F, 2.6F),
            new Box("right_arm", 132, 56, 5F, 5F, 5F, -3.4F, 8.0F, -2.8F),
            new Box("left_arm", 154, 56, 5F, 5F, 5F, -1.6F, 8.0F, -2.8F),
            new Box("right_leg", 176, 56, 2F, 6F, 2F, -7.2F, 1.6F, -1.0F),
            new Box("left_leg", 186, 56, 2F, 6F, 2F, 5.2F, 1.6F, -1.0F),
            new Box("right_leg", 196, 56, 8F, 2F, 8F, -6.4F, 7.6F, -4.4F),
            new Box("left_leg", 0, 72, 8F, 2F, 8F, -1.6F, 7.6F, -4.4F)
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
