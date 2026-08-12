package dev.nyxclient.gui;

import dev.nyxclient.NyxClient;
import dev.nyxclient.config.Theme;
import dev.nyxclient.module.Category;
import dev.nyxclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public final class ClickGui extends Screen {
    private Category selected = Category.COMBAT;
    private String search = "";
    private int scroll;
    private final List<Module> visible = new ArrayList<>();

    public ClickGui() {
        super(Text.literal("Nyx Client V4"));
        rebuild();
    }

    private void rebuild() {
        visible.clear();
        visible.addAll(NyxClient.modules().byCategory(selected).stream()
                .filter(m -> search.isBlank() || m.name().toLowerCase().contains(search.toLowerCase()))
                .toList());
    }

    @Override
    protected void init() {
        rebuild();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        Theme t = NyxClient.configs().activeTheme();
        ctx.fill(0, 0, width, height, t.background());

        int left = 28;
        int top = 22;
        int categoryWidth = 150;
        int panelRight = width - 28;

        ctx.fill(left, top, panelRight, height - 22, t.panel());
        ctx.drawTextWithShadow(textRenderer, Text.literal("NYX CLIENT V4"), left + 18, top + 16, t.text());
        ctx.drawTextWithShadow(textRenderer, Text.literal("Search: " + (search.isBlank() ? "type to filter" : search)),
                panelRight - 190, top + 16, t.secondaryText());

        int y = top + 48;
        for (Category c : Category.values()) {
            int color = c == selected ? t.accent() : t.secondaryText();
            ctx.drawTextWithShadow(textRenderer, Text.literal(c.name()), left + 18, y, color);
            y += 18;
        }

        int x = left + categoryWidth + 12;
        int rowY = top + 48 - scroll;
        for (Module m : visible) {
            if (rowY > top + 38 && rowY < height - 40) {
                int rowColor = m.enabled() ? t.enabled() : t.panel();
                ctx.fill(x, rowY - 3, panelRight - 16, rowY + 17, rowColor);
                ctx.drawTextWithShadow(textRenderer, Text.literal(m.name()), x + 8, rowY + 2, t.text());
                ctx.drawTextWithShadow(textRenderer, Text.literal(m.enabled() ? "ON" : "OFF"),
                        panelRight - 48, rowY + 2, m.enabled() ? t.accent() : t.secondaryText());
            }
            rowY += 22;
        }

        ctx.drawTextWithShadow(textRenderer,
                Text.literal("Left-click module • Right Shift closes • Mouse wheel scrolls"),
                left + 18, height - 38, t.secondaryText());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int left = 28, top = 22, categoryWidth = 150;
        if (mouseX >= left && mouseX < left + categoryWidth) {
            int index = (int)((mouseY - (top + 48)) / 18);
            if (index >= 0 && index < Category.values().length) {
                selected = Category.values()[index];
                scroll = 0;
                rebuild();
                return true;
            }
        }

        int x = left + categoryWidth + 12;
        int rowY = top + 48 - scroll;
        for (Module m : visible) {
            if (mouseX >= x && mouseX < width - 44 && mouseY >= rowY - 3 && mouseY <= rowY + 17) {
                m.toggle();
                NyxClient.configs().save("default");
                return true;
            }
            rowY += 22;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scroll = Math.max(0, scroll - (int)(verticalAmount * 22));
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
