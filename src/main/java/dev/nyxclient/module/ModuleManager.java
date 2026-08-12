package dev.nyxclient.module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private boolean registered;

    public void register(Module module) {
        if (modules.stream().anyMatch(m -> m.name().equalsIgnoreCase(module.name()))) {
            throw new IllegalArgumentException("Duplicate module: " + module.name());
        }
        modules.add(module);
    }

    public void registerCatalogModules() {
        if (registered) return;
        registered = true;

        String[][] catalog = {
            {"Auto Totem","COMBAT"},{"Auto Crystal","COMBAT"},{"Auto Hit Crystal","COMBAT"},{"Crystal Aura","COMBAT"},
            {"Crystal Place","COMBAT"},{"Crystal Break","COMBAT"},{"Crystal Damage","COMBAT"},{"Crystal Prediction","COMBAT"},
            {"Crystal Optimizer","COMBAT"},{"Crystal Target","COMBAT"},{"Double Anchor","COMBAT"},{"Anchor Aura","COMBAT"},
            {"Anchor Macro","COMBAT"},{"Auto Anchor","COMBAT"},{"Auto Mace","COMBAT"},{"Mace Swap","COMBAT"},
            {"Auto Hit","COMBAT"},{"TriggerBot","COMBAT"},{"Aim Assist","COMBAT"},{"Auto Pot","COMBAT"},
            {"Pot Cheats","COMBAT"},{"NethPot","COMBAT"},{"Auto Web","COMBAT"},{"Auto XP","COMBAT"},
            {"XP Macro","COMBAT"},{"Auto Bed","COMBAT"},{"Auto Obsidian","COMBAT"},{"Auto Trap","COMBAT"},
            {"Hole Fill","COMBAT"},{"Burrow Break","COMBAT"},{"Surround","COMBAT"},{"Anti Surround","COMBAT"},
            {"Shield Disabler","COMBAT"},{"Weapon Switch","COMBAT"},{"Smart Weapon","COMBAT"},{"Criticals","COMBAT"},
            {"Hitboxes","COMBAT"},{"Target HUD","COMBAT"},{"Target Selector","COMBAT"},{"Enemy Priority","COMBAT"},
            {"Self Damage Protection","COMBAT"},{"Friend System","COMBAT"},
            {"Freecam","PLAYER"},{"Hover Totem","PLAYER"},{"KeyPearl","PLAYER"},{"Auto Eat","PLAYER"},
            {"Auto Armor","PLAYER"},{"Inventory Move","PLAYER"},{"Fast Place","PLAYER"},{"No Bounce","PLAYER"},
            {"Jump Reset","PLAYER"},{"Sprint","PLAYER"},{"Anti Web","PLAYER"},{"Elytra Helper","PLAYER"},
            {"Item Swap","PLAYER"},{"Inventory Manager","PLAYER"},{"Chest Stealer","PLAYER"},{"Auto Refill","PLAYER"},
            {"Auto Tool","PLAYER"},{"Auto Respawn","PLAYER"},{"Auto GG","PLAYER"},{"Name Hider","PLAYER"},
            {"Step","MOVEMENT"},{"Safe Walk","MOVEMENT"},{"Speed","MOVEMENT"},{"Long Jump","MOVEMENT"},
            {"Strafe","MOVEMENT"},{"No Slow","MOVEMENT"},{"Jump Assist","MOVEMENT"},{"Movement Correction","MOVEMENT"},
            {"Auto Jump","MOVEMENT"},{"Boat Fly","MOVEMENT"},{"Elytra Control","MOVEMENT"},{"Velocity Controls","MOVEMENT"},
            {"ESP","RENDER"},{"Player ESP","RENDER"},{"Storage ESP","RENDER"},{"Block ESP","RENDER"},
            {"Amethyst ESP","RENDER"},{"Name Tags","RENDER"},{"Glow","RENDER"},{"Tracers","RENDER"},
            {"Item ESP","RENDER"},{"Entity ESP","RENDER"},{"Hole ESP","RENDER"},{"Crystal ESP","RENDER"},
            {"Chunk Finder","RENDER"},{"Fullbright","RENDER"},{"Freecam Render","RENDER"},{"Target ESP","RENDER"},
            {"Damage Indicator","RENDER"},{"Skeleton ESP","RENDER"},{"Waypoints","RENDER"},{"Breadcrumbs","RENDER"},
            {"Block Overlay","RENDER"},{"Search","RENDER"},{"Camera Tweaks","RENDER"},
            {"Growth Finder","WORLD"},{"Spawner Protect","WORLD"},{"Block Finder","WORLD"},{"Ore ESP","WORLD"},
            {"Structure Finder","WORLD"},{"Container Finder","WORLD"},{"Portal Finder","WORLD"},{"Hole Finder","WORLD"},
            {"Auto Build","WORLD"},{"Scaffold Helper","WORLD"},{"World Scanner","WORLD"},{"Coordinates","WORLD"},
            {"Watermark","HUD"},{"ArrayList","HUD"},{"FPS","HUD"},{"CPS","HUD"},{"Ping","HUD"},
            {"Armor HUD","HUD"},{"Potion HUD","HUD"},{"Target HUD","HUD"},{"Totem Counter","HUD"},
            {"Inventory HUD","HUD"},{"Keybind HUD","HUD"},{"Combat HUD","HUD"},{"Server Info","HUD"},
            {"Speed Display","HUD"},{"Session Info","HUD"},{"Notifications","HUD"},{"Crosshair","HUD"},{"Keystrokes","HUD"},
            {"Developer Test Mode","MISC"},{"Friend List","MISC"},{"Chat Timestamps","MISC"},{"Server Info Debug","MISC"},
            {"Theme Editor","CLIENT"},{"Config Manager","CLIENT"},{"Performance","PERFORMANCE"}
        };

        for (String[] entry : catalog) {
            register(new GenericModule(entry[0], Category.valueOf(entry[1])));
        }
    }

    public void tick() {
        for (Module module : List.copyOf(modules)) {
            if (module.enabled()) {
                try { module.onTick(); }
                catch (Throwable t) {
                    module.setEnabled(false);
                }
            }
        }
    }

    public List<Module> all() {
        return modules.stream().sorted(Comparator.comparing(Module::name, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    public List<Module> byCategory(Category category) {
        return modules.stream().filter(m -> m.category() == category)
                .sorted(Comparator.comparing(Module::name, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    public Optional<Module> find(String name) {
        return modules.stream().filter(m -> m.name().equalsIgnoreCase(name)).findFirst();
    }

    public int size() { return modules.size(); }
}
