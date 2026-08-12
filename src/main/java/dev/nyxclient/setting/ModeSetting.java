package dev.nyxclient.setting;

import java.util.Arrays;
import java.util.List;

public final class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String value, String... modes) {
        super(name, value);
        this.modes = List.copyOf(Arrays.asList(modes));
        if (!this.modes.contains(value)) {
            throw new IllegalArgumentException("Default mode is not in mode list: " + value);
        }
    }

    public List<String> modes() { return modes; }

    public void cycle() {
        int i = modes.indexOf(get());
        set(modes.get((i + 1) % modes.size()));
    }
}
