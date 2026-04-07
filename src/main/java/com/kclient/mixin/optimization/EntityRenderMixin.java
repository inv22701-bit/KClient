package com.kclient.mixin.optimization;

import com.kclient.KClientMod;
import com.kclient.config.KClientConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Entity Culling: Skips rendering entities that are too far away.
 */
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void kclient$cullEntity(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        KClientConfig config = KClientMod.getConfig();
        
        // Skip distance check if config is extra high or it's the player
        if (entity.isPlayer()) return;

        double distanceSq = entity.squaredDistanceTo(x, y, z);
        int maxDistance = config.entityRenderDistance;
        
        if (distanceSq > (double) (maxDistance * maxDistance)) {
            ci.cancel();
        }
    }
}
