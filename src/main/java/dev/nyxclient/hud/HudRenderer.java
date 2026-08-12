package dev.nyxclient.hud;

import dev.nyxclient.NyxClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class HudRenderer {
    private HudRenderer() {}

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> render(drawContext));
    }

    private static void render(DrawContext ctx) {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        int fps = client.getCurrentFps();
        ctx.drawTextWithShadow(client.textRenderer,
                Text.literal("Nyx Client V4  •  " + fps + " FPS"),
                6, 6, NyxClient.configs().activeTheme().hud());
    }
}
