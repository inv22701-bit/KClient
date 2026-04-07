package com.kclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kclient.KClientMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages loading and saving KClient config as JSON.
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private KClientConfig config;
    private final Path configPath;

    public ConfigManager() {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve("kclient.json");
        this.config = new KClientConfig();
    }

    public void load() {
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                config = GSON.fromJson(json, KClientConfig.class);
                if (config == null) config = new KClientConfig();
                config.validate();
                KClientMod.LOGGER.info("[KClient] Config loaded from {}", configPath);
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                KClientMod.LOGGER.error("[KClient] Failed to load config, using defaults", e);
                config = new KClientConfig();
            }
        } else {
            config = new KClientConfig();
            save();
            KClientMod.LOGGER.info("[KClient] Created default config at {}", configPath);
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, GSON.toJson(config));
        } catch (IOException e) {
            KClientMod.LOGGER.error("[KClient] Failed to save config", e);
        }
    }

    public KClientConfig getConfig() { return config; }

    public void resetToDefaults() {
        this.config = new KClientConfig();
        save();
    }
}
