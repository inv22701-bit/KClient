package com.kclient.gui;

import com.kclient.KClientMod;
import com.kclient.config.ConfigPreset;
import com.kclient.config.KClientConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Custom settings screen for KClient with dark theme.
 */
public class BoostSettingsScreen extends Screen {
    private final Screen parent;

    public BoostSettingsScreen(Screen parent) {
        super(Text.literal("⚡ KClient Settings").formatted(Formatting.BOLD, Formatting.AQUA));
        this.parent = parent;
    }

    @Override
    protected void init() {
        KClientConfig config = KClientMod.getConfig();
        int centerX = this.width / 2;
        int startY = 40;
        int spacing = 24;

        // Toggles
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.shadowsEnabled)
            .build(centerX - 155, startY, 150, 20, Text.literal("Shadows"), (button, value) -> config.shadowsEnabled = value));

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.fpsStabilizerEnabled)
            .build(centerX + 5, startY, 150, 20, Text.literal("FPS Stabilizer"), (button, value) -> config.fpsStabilizerEnabled = value));

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showHud)
            .build(centerX - 155, startY + spacing, 150, 20, Text.literal("Show HUD"), (button, value) -> config.showHud = value));

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.dynamicRenderDistance)
            .build(centerX + 5, startY + spacing, 150, 20, Text.literal("Dynamic RD"), (button, value) -> config.dynamicRenderDistance = value));

        // Particle Level
        this.addDrawableChild(CyclingButtonWidget.builder(KClientConfig.ParticleLevel::name)
            .values(KClientConfig.ParticleLevel.values())
            .initially(config.particleLevel)
            .build(centerX - 155, startY + spacing * 2, 150, 20, Text.literal("Particles"), (button, value) -> config.particleLevel = value));

        // Presets
        int presetY = startY + spacing * 4;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Ultra Low"), button -> { config.applyPreset(ConfigPreset.ULTRA_LOW); this.refresh(); })
            .dimensions(centerX - 155, presetY, 70, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Low"), button -> { config.applyPreset(ConfigPreset.LOW); this.refresh(); })
            .dimensions(centerX - 75, presetY, 70, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Medium"), button -> { config.applyPreset(ConfigPreset.MEDIUM); this.refresh(); })
            .dimensions(centerX + 5, presetY, 70, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("High"), button -> { config.applyPreset(ConfigPreset.HIGH); this.refresh(); })
            .dimensions(centerX + 85, presetY, 70, 20).build());

        // Target FPS Slider (Using Button for simplicity in this draft)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Target FPS: " + config.targetFps), button -> {
                config.targetFps = config.targetFps >= 240 ? 30 : config.targetFps + 30;
                button.setMessage(Text.literal("Target FPS: " + config.targetFps));
            })
            .dimensions(centerX - 155, startY + spacing * 6, 150, 20).build());

        // Reset & Done
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Reset"), button -> {
                KClientMod.getInstance().getConfigManager().resetToDefaults();
                this.refresh();
            })
            .dimensions(centerX - 155, this.height - 30, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> {
                KClientMod.getInstance().getConfigManager().save();
                this.client.setScreen(this.parent);
            })
            .dimensions(centerX + 5, this.height - 30, 150, 20).build());
    }

    private void refresh() {
        this.clearAndInit();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Performance Presets").formatted(Formatting.GRAY), this.width / 2, 40 + 24 * 3 + 5, 0xFFFFFF);
        
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        KClientMod.getInstance().getConfigManager().save();
        this.client.setScreen(this.parent);
    }
}
