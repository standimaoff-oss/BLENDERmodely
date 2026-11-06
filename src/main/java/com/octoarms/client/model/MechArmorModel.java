package com.octoarms.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Bulky exosuit armor model. The cube layout comes straight from the
 * generated {@link ArmorSpecs} table, which is also what painted the pixels of
 * the matching 128x128 armor texture - so plating and UVs can never drift.
 */
public class MechArmorModel extends HumanoidModel<LivingEntity> {

    private static final Map<String, MechArmorModel> CACHE = new HashMap<>();

    public static MechArmorModel forTier(String tierId) {
        return CACHE.computeIfAbsent(tierId, MechArmorModel::new);
    }

    private MechArmorModel(String tierId) {
        super(buildRoot(tierId));
    }

    private static net.minecraft.client.model.geom.ModelPart buildRoot(String tierId) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        Map<String, CubeListBuilder> builders = new HashMap<>();
        for (ArmorSpecs.Box box : ArmorSpecs.forTier(tierId)) {
            CubeListBuilder builder = builders.computeIfAbsent(box.part, key -> CubeListBuilder.create());
            if (box.w > 0 && box.h > 0 && box.d > 0) {
                builder.texOffs(box.u, box.v).addBox(box.ox, box.oy, box.oz, box.w, box.h, box.d);
            }
        }

        String[] parts = {"head", "hat", "body", "right_arm", "left_arm", "right_leg", "left_leg"};
        for (String part : parts) {
            float[] pivot = ArmorSpecs.PIVOTS.get(part);
            CubeListBuilder builder = builders.getOrDefault(part, CubeListBuilder.create());
            root.addOrReplaceChild(part, builder, PartPose.offset(pivot[0], pivot[1], pivot[2]));
        }
        return root.bake(ArmorSpecs.TEXTURE_SIZE, ArmorSpecs.TEXTURE_SIZE);
    }
}
