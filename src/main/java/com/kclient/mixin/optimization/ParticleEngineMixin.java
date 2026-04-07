package com.kclient.mixin.optimization;

import com.kclient.KClientMod;
import com.kclient.config.KClientConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Particle Reduction: Randomly skips rendering particles based on config level.
 */
@Mixin(Particle.class)
public abstract class ParticleEngineMixin {
    private static final Random kClient$random = new Random();

    @Inject(method = "buildGeometry", at = @At("HEAD"), cancellable = true)
    private void kclient$reduceParticles(VertexConsumer vertexConsumer, Camera camera, float tickDelta, CallbackInfo ci) {
        KClientConfig.ParticleLevel level = KClientMod.getConfig().particleLevel;
        
        if (level == KClientConfig.ParticleLevel.HIGH) return;
        
        if (kClient$random.nextFloat() > level.multiplier) {
            ci.cancel();
        }
    }
}
