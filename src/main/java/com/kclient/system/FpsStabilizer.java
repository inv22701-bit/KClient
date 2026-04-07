package com.kclient.system;

import com.kclient.KClientMod;
import com.kclient.config.ConfigManager;
import com.kclient.config.KClientConfig;
import net.minecraft.client.MinecraftClient;

/**
 * Adaptive FPS stabilization system.
 * Monitors FPS and progressively adjusts settings to maintain target framerate.
 * Level 0 = no changes, up to Level 5 = emergency render distance reduction.
 */
public class FpsStabilizer {
    private final ConfigManager configManager;
    private final PerformanceMonitor monitor;

    private int stabilizationLevel = 0;
    private int ticksSinceAdjust = 0;
    private static final int ADJUST_COOLDOWN = 40;   // 2 seconds
    private static final int RECOVER_COOLDOWN = 100;  // 5 seconds

    private int origRenderDist;
    private int origEntityDist;
    private KClientConfig.ParticleLevel origParticles;
    private boolean storedOriginals = false;
    private boolean active = false;

    public FpsStabilizer(ConfigManager cfgMgr, PerformanceMonitor monitor) {
        this.configManager = cfgMgr;
        this.monitor = monitor;
    }

    public void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;
        ticksSinceAdjust++;

        KClientConfig cfg = configManager.getConfig();
        if (monitor.getSampleCount() < 20) return;

        if (!storedOriginals) {
            origRenderDist = cfg.renderDistance;
            origEntityDist = cfg.entityRenderDistance;
            origParticles = cfg.particleLevel;
            storedOriginals = true;
        }

        float ratio = monitor.getAverageFps() / cfg.targetFps;

        if (ratio < 0.7f && ticksSinceAdjust >= ADJUST_COOLDOWN) {
            escalate(cfg);
        } else if (ratio < 0.85f && stabilizationLevel < 3 && ticksSinceAdjust >= ADJUST_COOLDOWN) {
            escalate(cfg);
        } else if (ratio > 1.15f && stabilizationLevel > 0 && ticksSinceAdjust >= RECOVER_COOLDOWN) {
            deescalate(cfg);
        }

        if (cfg.dynamicRenderDistance && client.options != null) {
            int targetRD = cfg.renderDistance;
            if (stabilizationLevel >= 3) targetRD = Math.max(2, targetRD - 4);
            else if (stabilizationLevel >= 1) targetRD = Math.max(2, targetRD - 2);

            int currentRD = client.options.getViewDistance().getValue();
            if (currentRD != targetRD) {
                client.options.getViewDistance().setValue(targetRD);
            }
        }
    }

    private void escalate(KClientConfig cfg) {
        stabilizationLevel = Math.min(5, stabilizationLevel + 1);
        ticksSinceAdjust = 0;
        active = true;
        KClientMod.LOGGER.info("[KClient] Stabilizer escalated to level {}", stabilizationLevel);

        switch (stabilizationLevel) {
            case 1 -> { if (cfg.particleLevel == KClientConfig.ParticleLevel.HIGH) cfg.particleLevel = KClientConfig.ParticleLevel.MEDIUM; }
            case 2 -> cfg.particleLevel = KClientConfig.ParticleLevel.LOW;
            case 3 -> cfg.entityRenderDistance = Math.max(16, cfg.entityRenderDistance - 16);
            case 4 -> { cfg.entityRenderDistance = Math.max(16, cfg.entityRenderDistance - 16); cfg.animationSimplification = true; }
            case 5 -> cfg.renderDistance = Math.max(2, cfg.renderDistance - 2);
        }
    }

    private void deescalate(KClientConfig cfg) {
        KClientMod.LOGGER.info("[KClient] Stabilizer recovering from level {}", stabilizationLevel);
        switch (stabilizationLevel) {
            case 5 -> cfg.renderDistance = Math.min(origRenderDist, cfg.renderDistance + 2);
            case 4 -> { cfg.animationSimplification = false; cfg.entityRenderDistance = Math.min(origEntityDist, cfg.entityRenderDistance + 16); }
            case 3 -> cfg.entityRenderDistance = Math.min(origEntityDist, cfg.entityRenderDistance + 16);
            case 2 -> cfg.particleLevel = KClientConfig.ParticleLevel.MEDIUM;
            case 1 -> cfg.particleLevel = origParticles;
        }
        stabilizationLevel = Math.max(0, stabilizationLevel - 1);
        ticksSinceAdjust = 0;
        if (stabilizationLevel == 0) active = false;
    }

    public int getLevel() { return stabilizationLevel; }
    public boolean isActive() { return active; }

    public void reset() {
        stabilizationLevel = 0;
        active = false;
        storedOriginals = false;
        ticksSinceAdjust = 0;
    }
}
