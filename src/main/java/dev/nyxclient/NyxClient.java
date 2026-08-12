package dev.nyxclient;

import dev.nyxclient.config.ConfigManager;
import dev.nyxclient.gui.ClickGui;
import dev.nyxclient.module.ModuleManager;
import dev.nyxclient.performance.PerformanceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NyxClient implements ClientModInitializer {
    public static final String MOD_ID = "nyxclient";
    public static final String NAME = "Nyx Client V4";
    public static final String VERSION = "4.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ModuleManager moduleManager;
    private static ConfigManager configManager;
    private static PerformanceManager performanceManager;
    private static KeyBinding clickGuiKey;

    @Override
    public void onInitializeClient() {
        configManager = new ConfigManager();
        moduleManager = new ModuleManager();
        performanceManager = new PerformanceManager();
        dev.nyxclient.event.Events.register();

        moduleManager.registerCatalogModules();
        configManager.load("default");

        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nyxclient.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.nyxclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (clickGuiKey.wasPressed() && client.currentScreen == null) {
                client.setScreen(new ClickGui());
            }
            performanceManager.tick();
            moduleManager.tick();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                configManager.save("default");
            } catch (Exception e) {
                LOGGER.warn("Failed to save Nyx configuration during shutdown", e);
            }
        }, "nyx-config-shutdown"));

        LOGGER.info("{} {} initialized with {} modules", NAME, VERSION, moduleManager.size());
    }

    public static ModuleManager modules() {
        return moduleManager;
    }

    public static ConfigManager configs() {
        return configManager;
    }

    public static PerformanceManager performance() {
        return performanceManager;
    }
}
