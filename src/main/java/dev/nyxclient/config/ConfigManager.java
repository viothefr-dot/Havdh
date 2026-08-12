package dev.nyxclient.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.nyxclient.NyxClient;
import dev.nyxclient.module.Module;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigManager {
    private final Path root;
    private final Map<String, Theme> themes = new LinkedHashMap<>();
    private Theme activeTheme = Theme.nyxDark();

    public ConfigManager() {
        root = FabricPaths.configDir().resolve("nyxclient");
        themes.put("Nyx Dark", Theme.nyxDark());
        themes.put("Midnight", new Theme("Midnight", 0xF20A0B10, 0xF2151722, 0xFF4D8DFF, 0xFFF1F4FF, 0xFF8D93A6, 0xFF4D8DFF, 0xFF22283A, 0xFF343A50, 0xFF4D8DFF));
        themes.put("AMOLED", new Theme("AMOLED", 0xFF000000, 0xFF050505, 0xFFFFFFFF, 0xFFFFFFFF, 0xFF999999, 0xFFFFFFFF, 0xFF202020, 0xFF303030, 0xFFFFFFFF));
        themes.put("Light", new Theme("Light", 0xF2F4F6FA, 0xFFFFFFFF, 0xFF5C4BFF, 0xFF14151A, 0xFF606572, 0xFF5C4BFF, 0xFFE8EAF0, 0xFFD3D6DE, 0xFF5C4BFF));
        themes.put("Purple", new Theme("Purple", 0xEE130C20, 0xF0201730, 0xFFC16CFF, 0xFFF7EEFF, 0xFFB8A8C9, 0xFFC16CFF, 0xFF312243, 0xFF513866, 0xFFC16CFF));
        themes.put("Red", new Theme("Red", 0xEE180C0C, 0xF0221414, 0xFFFF5D68, 0xFFFFF0F1, 0xFFB99B9E, 0xFFFF5D68, 0xFF3A2224, 0xFF60363A, 0xFFFF5D68));
        themes.put("Blue", new Theme("Blue", 0xEE09121D, 0xF015202D, 0xFF4CC9FF, 0xFFEEF9FF, 0xFF9EB5C2, 0xFF4CC9FF, 0xFF213444, 0xFF355264, 0xFF4CC9FF));
    }

    public Path root() { return root; }
    public Theme activeTheme() { return activeTheme; }
    public Map<String, Theme> themes() { return Map.copyOf(themes); }

    public void setTheme(String name) {
        Theme theme = themes.get(name);
        if (theme != null) activeTheme = theme;
    }

    public void save(String profile) {
        try {
            Files.createDirectories(root);
            JsonObject obj = new JsonObject();
            obj.addProperty("version", 1);
            obj.addProperty("theme", activeTheme.name());

            JsonObject modules = new JsonObject();
            for (Module m : NyxClient.modules().all()) {
                modules.addProperty(m.name(), m.enabled());
            }
            obj.add("modules", modules);

            Path target = root.resolve(safe(profile) + ".json");
            Path tmp = root.resolve(safe(profile) + ".json.tmp");
            Files.writeString(tmp, new GsonBuilder().setPrettyPrinting().create().toJson(obj),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            try {
                Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            NyxClient.LOGGER.warn("Unable to save config {}", profile, e);
        }
    }

    public void load(String profile) {
        Path file = root.resolve(safe(profile) + ".json");
        if (!Files.exists(file)) {
            save(profile);
            return;
        }
        try {
            JsonObject obj = new com.google.gson.Gson().fromJson(Files.readString(file), JsonObject.class);
            if (obj.has("theme")) setTheme(obj.get("theme").getAsString());
            if (obj.has("modules")) {
                JsonObject modules = obj.getAsJsonObject("modules");
                for (Module m : NyxClient.modules().all()) {
                    if (modules.has(m.name())) m.setEnabled(modules.get(m.name()).getAsBoolean());
                }
            }
        } catch (Exception e) {
            NyxClient.LOGGER.warn("Unable to load config {}", profile, e);
        }
    }

    public void delete(String profile) throws IOException {
        Files.deleteIfExists(root.resolve(safe(profile) + ".json"));
    }

    public void rename(String from, String to) throws IOException {
        Files.move(root.resolve(safe(from) + ".json"), root.resolve(safe(to) + ".json"),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public void duplicate(String from, String to) throws IOException {
        Files.copy(root.resolve(safe(from) + ".json"), root.resolve(safe(to) + ".json"),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public void exportConfig(String profile, Path destination) throws IOException {
        Files.copy(root.resolve(safe(profile) + ".json"), destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public void importConfig(Path source, String profile) throws IOException {
        Files.copy(source, root.resolve(safe(profile) + ".json"), StandardCopyOption.REPLACE_EXISTING);
    }

    private static String safe(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static final class FabricPaths {
        static Path configDir() {
            return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir();
        }
    }
}
