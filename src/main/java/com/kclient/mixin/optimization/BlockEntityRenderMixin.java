package com.kclient.mixin.optimization;

import com.kclient.KClientMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Block Entity Culling: Skips rendering block entities like chests/signs that are too far.
 */
@Mixin(BlockEntityRenderer.class)
public interface BlockEntityRenderMixin<T extends BlockEntity> {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void kclient$cullBlockEntity(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        // Simple distance-based culling for block entities
        // Block entities usually render up to 64 blocks by default, we can make it stricter
        double distanceSq = entity.getPos().getSquaredDistance(
            net.minecraft.client.MinecraftClient.getInstance().player.getPos()
        );
        
        // 48 blocks for block entities if not explicitly configured
        if (distanceSq > 2304.0) { // 48 * 48
            ci.cancel();
        }
    }
}
