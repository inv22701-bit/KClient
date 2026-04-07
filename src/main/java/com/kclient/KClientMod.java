package com.kclient;

import com.kclient.config.ConfigManager;
import com.kclient.config.KClientConfig;
import com.kclient.gui.PerformanceHudOverlay;
import com.kclient.keybind.KClientKeybinds;
import com.kclient.system.FpsStabilizer;
import com.kclient.system.PerformanceMonitor;
import com.kclient.system.SmartPerformanceAI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KClientMod implements ClientModInitializer {
    public static final String MOD_ID = "kclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KClientMod instance;
    private ConfigManager configManager;
    private PerformanceMonitor performanceMonitor;
    private FpsStabilizer fpsStabilizer;
    private SmartPerformanceAI smartAI;
    private PerformanceHudOverlay hudOverlay;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("[KClient] Initializing KClient Performance Mod v{}...", "1.0.0");

        configManager = new ConfigManager();
        configManager.load();

        performanceMonitor = new PerformanceMonitor();
        fpsStabilizer = new FpsStabilizer(configManager, performanceMonitor);
        smartAI = new SmartPerformanceAI(performanceMonitor);
        hudOverlay = new PerformanceHudOverlay(performanceMonitor);

        KClientKeybinds.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            performanceMonitor.tick();
            if (configManager.getConfig().fpsStabilizerEnabled) {
                fpsStabilizer.tick(client);
            }
            smartAI.tick(client);
            KClientKeybinds.handleInput(client);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (configManager.getConfig().showHud) {
                hudOverlay.render(drawContext);
            }
        });

        LOGGER.info("[KClient] KClient initialized successfully!");
    }

    public static KClientMod getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public static KClientConfig getConfig() { return instance.configManager.getConfig(); }
    public PerformanceMonitor getPerformanceMonitor() { return performanceMonitor; }
    public FpsStabilizer getFpsStabilizer() { return fpsStabilizer; }
    public SmartPerformanceAI getSmartAI() { return smartAI; }
}
