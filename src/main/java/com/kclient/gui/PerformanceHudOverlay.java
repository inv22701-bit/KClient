package com.kclient.gui;

import com.kclient.KClientMod;
import com.kclient.system.PerformanceMonitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

/**
 * In-game HUD overlay showing FPS, RAM, and Chunk updates.
 */
public class PerformanceHudOverlay {
    private final PerformanceMonitor monitor;

    public PerformanceHudOverlay(PerformanceMonitor monitor) {
        this.monitor = monitor;
    }

    public void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int x = 5;
        int y = 5;
        int lineHeight = 10;

        // FPS line
        float fps = monitor.getCurrentFps();
        float avgFps = monitor.getAverageFps();
        Formatting fpsColor = getFpsColor(fps);
        drawContext.drawTextWithShadow(textRenderer, "FPS: " + fpsColor + (int)fps + Formatting.WHITE + " (Avg: " + (int)avgFps + ")", x, y, 0xFFFFFF);
        y += lineHeight;

        // RAM line
        long used = monitor.getUsedMemoryMB();
        long max = monitor.getMaxMemoryMB();
        drawContext.drawTextWithShadow(textRenderer, "RAM: " + used + "/" + max + " MB", x, y, 0xFFFFFF);
        y += lineHeight;

        // Chunks line
        int updates = monitor.getChunkUpdates();
        drawContext.drawTextWithShadow(textRenderer, "Chunks: " + updates, x, y, 0xFFFFFF);
        y += lineHeight;

        // Stabilizer status
        if (KClientMod.getInstance().getFpsStabilizer().isActive()) {
            int level = KClientMod.getInstance().getFpsStabilizer().getLevel();
            drawContext.drawTextWithShadow(textRenderer, Formatting.AQUA + "Stabilizer: Level " + level, x, y, 0xFFFFFF);
        }
    }

    private Formatting getFpsColor(float fps) {
        if (fps >= 60) return Formatting.GREEN;
        if (fps >= 30) return Formatting.YELLOW;
        return Formatting.RED;
    }
}
