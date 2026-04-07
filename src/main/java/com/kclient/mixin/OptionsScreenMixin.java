package com.kclient.mixin;

import com.kclient.gui.BoostSettingsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to inject the KClient Settings button into the vanilla Options screen.
 */
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void kclient$addSettingsButton(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("⚡ KClient").formatted(Formatting.AQUA, Formatting.BOLD),
                button -> this.client.setScreen(new BoostSettingsScreen(this)))
            .dimensions(this.width / 2 + 158, this.height / 6 + 42, 20, 20)
            .build());
    }
}
