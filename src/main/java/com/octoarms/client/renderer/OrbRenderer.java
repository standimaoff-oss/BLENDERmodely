package com.octoarms.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Renders projectiles as their glowing item form, billboarded to the camera.
 */
public class OrbRenderer<T extends Entity> extends EntityRenderer<T> {

    private final Supplier<Item> item;
    private final float scale;

    public OrbRenderer(EntityRendererProvider.Context context, Supplier<Item> item, float scale) {
        super(context);
        this.item = item;
        this.scale = scale;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
        poseStack.scale(1.0F, -1.0F, 1.0F);
        entityRenderDispatcher.getItemRenderer().renderStatic(
                new ItemStack(item.get()), ItemDisplayContext.GROUND,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }
}
