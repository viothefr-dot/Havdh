package dev.nyxclient.config;

public record Theme(
        String name,
        int background,
        int panel,
        int accent,
        int text,
        int secondaryText,
        int enabled,
        int hover,
        int border,
        int hud
) {
    public static Theme nyxDark() {
        return new Theme("Nyx Dark", 0xEE0D1018, 0xF0181C28, 0xFF9B7BFF, 0xFFF3F4FF,
                0xFF9AA0B2, 0xFF9B7BFF, 0xFF252A3A, 0xFF343A50, 0xFF9B7BFF);
    }
}
