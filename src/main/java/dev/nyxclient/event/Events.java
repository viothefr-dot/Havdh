package dev.nyxclient.event;

import dev.nyxclient.hud.HudRenderer;

public final class Events {
    private Events() {}

    public static void register() {
        HudRenderer.register();
    }
}
