package com.kclient.keybind;

import com.kclient.KClientMod;
import com.kclient.gui.BoostSettingsScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

/**
 * Handles mod-specific keybindings.
 */
public class KClientKeybinds {
    private static KeyBinding toggleStabilizer;
    private static KeyBinding toggleHud;
    private static KeyBinding openSettings;

    public static void register() {
        toggleStabilizer = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kclient.toggle_stabilizer",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            "category.kclient"
        ));

        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kclient.toggle_hud",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F8,
            "category.kclient"
        ));

        openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kclient.open_settings",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F9,
            "category.kclient"
        ));
    }

    public static void handleInput(MinecraftClient client) {
        while (toggleStabilizer.wasPressed()) {
            KClientMod.getConfig().fpsStabilizerEnabled = !KClientMod.getConfig().fpsStabilizerEnabled;
            boolean enabled = KClientMod.getConfig().fpsStabilizerEnabled;
            client.player.sendMessage(Text.literal("[KClient] Stabilizer: " + 
                (enabled ? Formatting.GREEN + "ON" : Formatting.RED + "OFF")), true);
            if (!enabled) KClientMod.getInstance().getFpsStabilizer().reset();
        }

        while (toggleHud.wasPressed()) {
            KClientMod.getConfig().showHud = !KClientMod.getConfig().showHud;
            client.player.sendMessage(Text.literal("[KClient] HUD: " + 
                (KClientMod.getConfig().showHud ? Formatting.GREEN + "ON" : Formatting.RED + "OFF")), true);
        }

        while (openSettings.wasPressed()) {
            client.setScreen(new BoostSettingsScreen(null));
        }
    }
}
