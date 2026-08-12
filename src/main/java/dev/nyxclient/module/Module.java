package dev.nyxclient.module;

import dev.nyxclient.NyxClient;
import dev.nyxclient.setting.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String name;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;
    private int keyCode = -1;

    protected Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public final String name() { return name; }
    public final Category category() { return category; }
    public final boolean enabled() { return enabled; }
    public final List<Setting<?>> settings() { return Collections.unmodifiableList(settings); }
    public final int keyCode() { return keyCode; }

    public final void keyCode(int keyCode) { this.keyCode = keyCode; }

    protected final <T extends Setting<?>> T add(T setting) {
        settings.add(setting);
        return setting;
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        try {
            if (enabled) onEnable();
            else onDisable();
        } catch (Throwable t) {
            NyxClient.LOGGER.error("Module {} failed during state change", name, t);
            this.enabled = false;
        }
    }

    protected void onEnable() {}
    protected void onDisable() {}
    public void onTick() {}
    public void onRender(float tickDelta) {}
}
