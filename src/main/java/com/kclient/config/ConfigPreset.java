package com.kclient.config;

/**
 * Performance presets that configure all settings at once.
 */
public enum ConfigPreset {
    ULTRA_LOW("Ultra Low") {
        @Override
        public void apply(KClientConfig c) {
            c.shadowsEnabled = false;
            c.particleLevel = KClientConfig.ParticleLevel.LOW;
            c.entityRenderDistance = 24;
            c.animationSimplification = true;
            c.renderDistance = 4;
            c.simulationDistance = 4;
            c.dynamicRenderDistance = true;
        }
    },
    LOW("Low") {
        @Override
        public void apply(KClientConfig c) {
            c.shadowsEnabled = false;
            c.particleLevel = KClientConfig.ParticleLevel.LOW;
            c.entityRenderDistance = 40;
            c.animationSimplification = true;
            c.renderDistance = 6;
            c.simulationDistance = 6;
            c.dynamicRenderDistance = true;
        }
    },
    MEDIUM("Medium") {
        @Override
        public void apply(KClientConfig c) {
            c.shadowsEnabled = true;
            c.particleLevel = KClientConfig.ParticleLevel.MEDIUM;
            c.entityRenderDistance = 64;
            c.animationSimplification = false;
            c.renderDistance = 12;
            c.simulationDistance = 10;
            c.dynamicRenderDistance = true;
        }
    },
    HIGH("High") {
        @Override
        public void apply(KClientConfig c) {
            c.shadowsEnabled = true;
            c.particleLevel = KClientConfig.ParticleLevel.HIGH;
            c.entityRenderDistance = 128;
            c.animationSimplification = false;
            c.renderDistance = 16;
            c.simulationDistance = 12;
            c.dynamicRenderDistance = false;
        }
    };

    public final String displayName;

    ConfigPreset(String displayName) {
        this.displayName = displayName;
    }

    public abstract void apply(KClientConfig config);
}
