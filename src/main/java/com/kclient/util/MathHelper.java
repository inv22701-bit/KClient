package com.kclient.util;

/**
 * Utility functions for math operations.
 */
public class MathHelper {
    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
