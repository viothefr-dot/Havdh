package dev.nyxclient.module;

import dev.nyxclient.setting.BooleanSetting;
import dev.nyxclient.setting.ModeSetting;
import dev.nyxclient.setting.NumberSetting;

/**
 * Safe extension stub used for catalog entries that require game-specific logic.
 * It intentionally performs no automated combat, packet spoofing or anti-cheat evasion.
 */
public final class GenericModule extends Module {
    private final BooleanSetting enabledSetting;
    private final NumberSetting updateRate;
    private final ModeSetting mode;

    public GenericModule(String name, Category category) {
        super(name, category);
        enabledSetting = add(new BooleanSetting("Active", false));
        updateRate = add(new NumberSetting("Update Rate", 10, 1, 60, 1));
        mode = add(new ModeSetting("Mode", "Default", "Default", "Conservative", "Experimental"));
    }

    public BooleanSetting activeSetting() { return enabledSetting; }
    public NumberSetting updateRate() { return updateRate; }
    public ModeSetting mode() { return mode; }
}
