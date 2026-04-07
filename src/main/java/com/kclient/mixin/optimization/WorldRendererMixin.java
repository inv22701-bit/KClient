package com.kclient.mixin.optimization;

import com.kclient.KClientMod;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * World Renderer hooks for chunk and cloud optimizations.
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void kclient$skipClouds(CallbackInfoReturnable<Boolean> cir) {
        // Simple optimization pattern: if preset is ULTRA_LOW, we could disable clouds via hooks
        // though vanilla has a setting, this demonstrates the capability.
    }
}
