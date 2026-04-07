package com.kclient.config;

/**
 * Configuration data class for KClient.
 * All fields are serialized to/from JSON via Gson.
 */
public class KClientConfig {

    // --- Rendering ---
    public boolean shadowsEnabled = true;
    public ParticleLevel particleLevel = ParticleLevel.HIGH;
    public int entityRenderDistance = 64;
    public boolean animationSimplification = false;
    public int renderDistance = 12;
    public int simulationDistance = 10;

    // --- FPS Stabilizer ---
    public boolean fpsStabilizerEnabled = true;
    public int targetFps = 60;
    public boolean dynamicRenderDistance = true;

    // --- Display ---
    public boolean showHud = true;
    public boolean improvedVsync = false;

    // --- Preset ---
    public ConfigPreset activePreset = ConfigPreset.MEDIUM;

    public enum ParticleLevel {
        LOW(0.25f, "Low"),
        MEDIUM(0.6f, "Medium"),
        HIGH(1.0f, "High");

        public final float multiplier;
        public final String displayName;

        ParticleLevel(float multiplier, String displayName) {
            this.multiplier = multiplier;
            this.displayName = displayName;
        }
    }

    public void applyPreset(ConfigPreset preset) {
        this.activePreset = preset;
        preset.apply(this);
    }

    /** Clamp all values to valid ranges */
    public void validate() {
        renderDistance = clamp(renderDistance, 2, 32);
        simulationDistance = clamp(simulationDistance, 2, 32);
        entityRenderDistance = clamp(entityRenderDistance, 8, 256);
        targetFps = clamp(targetFps, 15, 300);
        if (particleLevel == null) particleLevel = ParticleLevel.HIGH;
        if (activePreset == null) activePreset = ConfigPreset.MEDIUM;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
