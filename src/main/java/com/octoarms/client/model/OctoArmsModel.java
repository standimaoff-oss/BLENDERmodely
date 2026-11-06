package com.octoarms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.octoarms.mech.ClimbHandler;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

/**
 * The animated octo-limb rig: eight servo legs, twin arm cannons, thruster and
 * sensor cluster. UV rectangles match tools/gen_entity.py exactly.
 */
public class OctoArmsModel {

    private static OctoArmsModel instance;

    public final ModelPart root;
    public final ModelPart[] legs = new ModelPart[8];
    public final ModelPart[] femurs = new ModelPart[8];
    public final ModelPart[] tibias = new ModelPart[8];
    public final ModelPart cannonLeft;
    public final ModelPart cannonRight;
    public final ModelPart thruster;
    public final ModelPart flame;
    public final ModelPart eyes;

    public static OctoArmsModel instance() {
        if (instance == null) {
            instance = new OctoArmsModel();
        }
        return instance;
    }

    private OctoArmsModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition rootDef = mesh.getRoot();

        rootDef.addOrReplaceChild("spine", CubeListBuilder.create()
                .texOffs(0, 46).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 8),
                PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, -0.5F, 0.0F, 0.0F));
        rootDef.addOrReplaceChild("mount_left", CubeListBuilder.create()
                .texOffs(20, 0).addBox(-2.5F, -2.5F, -2.0F, 5, 5, 4),
                PartPose.offset(4.5F, 3.0F, 2.5F));
        rootDef.addOrReplaceChild("mount_right", CubeListBuilder.create()
                .texOffs(20, 0).addBox(-2.5F, -2.5F, -2.0F, 5, 5, 4),
                PartPose.offset(-4.5F, 3.0F, 2.5F));

        PartDefinition cannonLeftDef = rootDef.addOrReplaceChild("cannon_left", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-2.0F, -2.0F, -3.0F, 4, 4, 6),
                PartPose.offset(6.5F, 4.0F, 1.0F));
        cannonLeftDef.addOrReplaceChild("barrel_left", CubeListBuilder.create()
                .texOffs(20, 24).addBox(-1.0F, -1.0F, -7.0F, 2, 2, 7),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cannonRightDef = rootDef.addOrReplaceChild("cannon_right", CubeListBuilder.create()
                .texOffs(20, 12).addBox(-2.0F, -2.0F, -3.0F, 4, 4, 6),
                PartPose.offset(-6.5F, 4.0F, 1.0F));
        cannonRightDef.addOrReplaceChild("barrel_right", CubeListBuilder.create()
                .texOffs(20, 24).addBox(-1.0F, -1.0F, -7.0F, 2, 2, 7),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition thrusterDef = rootDef.addOrReplaceChild("thruster", CubeListBuilder.create()
                .texOffs(40, 0).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4),
                PartPose.offset(0.0F, 6.0F, 4.0F));
        thrusterDef.addOrReplaceChild("flame", CubeListBuilder.create()
                .texOffs(40, 10).addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3),
                PartPose.offset(0.0F, 2.0F, 0.0F));
        rootDef.addOrReplaceChild("eyes", CubeListBuilder.create()
                .texOffs(40, 20).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1),
                PartPose.offset(0.0F, -1.0F, -3.2F));

        for (int i = 0; i < 8; i++) {
            boolean left = i < 4;
            int j = i % 4;
            float side = left ? 1.0F : -1.0F;
            float yaw = side * (0.55F + j * 0.42F);
            float px = side * 3.2F;
            float pz = -2.0F + j * 2.6F;
            PartDefinition coxa = rootDef.addOrReplaceChild("leg" + i, CubeListBuilder.create()
                    .texOffs(0, 0).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 5),
                    PartPose.offsetAndRotation(px, 2.0F, pz, -0.45F, yaw, 0.0F));
            PartDefinition femur = coxa.addOrReplaceChild("femur" + i, CubeListBuilder.create()
                    .texOffs(0, 12).addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.95F, 0.0F, 0.0F));
            PartDefinition tibia = femur.addOrReplaceChild("tibia" + i, CubeListBuilder.create()
                    .texOffs(0, 24).addBox(-1.0F, -1.0F, 0.0F, 2, 2, 9),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, -1.35F, 0.0F, 0.0F));
            tibia.addOrReplaceChild("claw" + i, CubeListBuilder.create()
                    .texOffs(0, 38).addBox(-1.0F, -1.5F, 0.0F, 2, 3, 2),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.6F, 0.0F, 0.0F));
        }

        this.root = rootDef.bake(64, 64);
        cannonLeft = root.getChild("cannon_left");
        cannonRight = root.getChild("cannon_right");
        thruster = root.getChild("thruster");
        flame = thruster.getChild("flame");
        eyes = root.getChild("eyes");
        for (int i = 0; i < 8; i++) {
            legs[i] = root.getChild("leg" + i);
            femurs[i] = legs[i].getChild("femur" + i);
            tibias[i] = femurs[i].getChild("tibia" + i);
        }
    }

    /**
     * Pose the rig: idle sway while walking, tucked spread while crawling a
     * wall (legs re-aim towards the surface being gripped).
     */
    public void setup(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks) {
        boolean climbing = ClimbHandler.isClimbing(player);
        boolean flying = player.getAbilities().flying;
        float tick = ageInTicks;

        for (int i = 0; i < 8; i++) {
            boolean left = i < 4;
            int j = i % 4;
            float side = left ? 1.0F : -1.0F;
            float baseYaw = side * (0.55F + j * 0.42F);
            float phase = j * 1.7F + (left ? 0.0F : 3.1F);

            float sway = Mth.sin(limbSwing * 0.9F + phase) * 0.22F * Mth.clamp(limbSwingAmount, 0.0F, 1.0F);
            float idle = Mth.sin(tick * 0.06F + phase) * 0.05F;

            if (climbing) {
                legs[i].xRot = -1.15F + sway * 0.5F;
                legs[i].yRot = baseYaw * 0.55F;
                femurs[i].xRot = 1.35F + idle;
                tibias[i].xRot = -1.6F;
            } else if (flying) {
                legs[i].xRot = -0.85F + idle;
                legs[i].yRot = baseYaw * 1.25F;
                femurs[i].xRot = 1.1F;
                tibias[i].xRot = -1.9F;
            } else {
                legs[i].xRot = -0.45F + sway + idle;
                legs[i].yRot = baseYaw + sway * 0.35F * side;
                femurs[i].xRot = 0.95F - sway * 0.6F;
                tibias[i].xRot = -1.35F + sway * 0.4F;
            }
        }

        float recoil = Mth.sin(tick * 0.35F) * 0.05F;
        cannonLeft.xRot = recoil;
        cannonRight.xRot = -recoil;
        flame.visible = flying;
        flame.yScale = flying ? 1.0F + Mth.sin(tick * 0.9F) * 0.25F : 1.0F;
        eyes.visible = true;
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                       float r, float g, float b, float a) {
        root.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    public static boolean isWorn(AbstractClientPlayer player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem()
                instanceof com.octoarms.item.MechArmorItem armor
                && armor.tier() == com.octoarms.mech.MechSuitTier.SPIDER;
    }
}
