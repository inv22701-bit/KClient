package com.kclient.system;

import com.kclient.KClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Smart Performance AI that tracks FPS per biome and learns
 * which biomes are performance-heavy for pre-emptive adjustment.
 */
public class SmartPerformanceAI {
    private final PerformanceMonitor monitor;
    private final Map<String, BiomeData> biomeMap = new HashMap<>();
    private String currentBiome = "";
    private int tickCounter = 0;
    private static final int CHECK_INTERVAL = 100;

    public SmartPerformanceAI(PerformanceMonitor monitor) {
        this.monitor = monitor;
    }

    public void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;
        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) return;
        tickCounter = 0;

        Optional<RegistryKey<Biome>> key = client.world
                .getBiome(client.player.getBlockPos()).getKey();
        String name = key.map(k -> k.getValue().toString()).orElse("unknown");

        BiomeData data = biomeMap.computeIfAbsent(name, k -> new BiomeData());
        data.addSample(monitor.getAverageFps());

        if (!name.equals(currentBiome)) {
            currentBiome = name;
            if (data.count > 3 && data.getAvg() < KClientMod.getConfig().targetFps * 0.8f) {
                KClientMod.LOGGER.info("[KClient] AI: Heavy biome '{}' avg={}", name, (int) data.getAvg());
            }
        }
    }

    public boolean isCurrentBiomeHeavy() {
        BiomeData d = biomeMap.get(currentBiome);
        return d != null && d.count > 3 && d.getAvg() < KClientMod.getConfig().targetFps * 0.75f;
    }

    public static class BiomeData {
        float total = 0;
        int count = 0;
        void addSample(float fps) { total += fps; count++; }
        float getAvg() { return count > 0 ? total / count : 0; }
    }
}
