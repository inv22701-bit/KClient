package com.kclient.system;

import net.minecraft.client.MinecraftClient;
import java.util.Arrays;

/**
 * Tracks FPS, frame time, memory, and chunk statistics.
 */
public class PerformanceMonitor {
    private static final int SAMPLE_SIZE = 60;

    private final float[] fpsSamples = new float[SAMPLE_SIZE];
    private int sampleIndex = 0;
    private int sampleCount = 0;

    private float currentFps = 0;
    private float averageFps = 0;
    private float onePercentLowFps = 0;

    private long usedMemoryMB = 0;
    private long maxMemoryMB = 0;
    private int chunkUpdates = 0;

    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();

        currentFps = client.getCurrentFps();
        fpsSamples[sampleIndex] = currentFps;
        sampleIndex = (sampleIndex + 1) % SAMPLE_SIZE;
        if (sampleCount < SAMPLE_SIZE) sampleCount++;

        calculateStats();

        Runtime rt = Runtime.getRuntime();
        usedMemoryMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
        maxMemoryMB = rt.maxMemory() / (1024 * 1024);

        if (client.worldRenderer != null) {
            chunkUpdates = client.worldRenderer.getCompletedChunkCount();
        }
    }

    private void calculateStats() {
        if (sampleCount == 0) return;
        float sum = 0;
        float[] sorted = new float[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            sum += fpsSamples[i];
            sorted[i] = fpsSamples[i];
        }
        averageFps = sum / sampleCount;
        Arrays.sort(sorted);
        onePercentLowFps = sorted[Math.max(0, (int) (sampleCount * 0.01f))];
    }

    public float getCurrentFps() { return currentFps; }
    public float getAverageFps() { return averageFps; }
    public float getOnePercentLowFps() { return onePercentLowFps; }
    public long getUsedMemoryMB() { return usedMemoryMB; }
    public long getMaxMemoryMB() { return maxMemoryMB; }
    public int getChunkUpdates() { return chunkUpdates; }
    public int getSampleCount() { return sampleCount; }
}
