# Nyx Client V4

Minecraft Java Edition 1.21.11 / Fabric client framework.

## Requirements

- JDK 21
- Minecraft 1.21.11
- Fabric Loader
- Internet access on the first Gradle build

Fabric's documentation identifies Loom as the Gradle development toolchain and 1.21.11 as the final obfuscated Minecraft release; this project therefore uses the remapping Loom plugin and Yarn mappings for 1.21.11.

## Build

Linux/macOS:

```bash
./gradlew build
```

Windows:

```powershell
./gradlew.bat build
```

The remapped JAR is produced under `build/libs/`.

## Run the development client

```bash
./gradlew runClient
```

Default Click GUI keybind: **Right Shift**.

## Configuration

Runtime configs are stored in:

```text
.minecraft/config/nyxclient/
```

Files are JSON. The manager supports profiles, save/load, rename, duplicate, delete, import and export.

## Architecture

- `dev.nyxclient.module` — module lifecycle and registry
- `dev.nyxclient.setting` — typed settings
- `dev.nyxclient.config` — persistent profiles
- `dev.nyxclient.gui` — Click GUI
- `dev.nyxclient.hud` — HUD renderer
- `dev.nyxclient.event` — lightweight client tick/render hooks
- `dev.nyxclient.performance` — throttling and low-FPS policy
- `dev.nyxclient.module.catalog` — 100+ uniquely named module stubs

## Important scope note

The high-risk combat automation entries from the design prompt are represented as clearly labelled extension stubs rather than packet/anti-cheat evasion implementations. Developer/Test Mode is provided for transparent timing/action diagnostics and rate limiting.

## Themes

Built-in themes:

- Nyx Dark
- Midnight
- AMOLED
- Light
- Purple
- Red
- Blue
- Custom

Theme values are persisted in the active config.

## Extending a module

Subclass `Module`, add settings in the constructor, and register it in `ModuleManager#registerCatalogModules()`.

All modules have:

- enable/disable lifecycle
- category
- keybind
- settings list
- optional tick/render callbacks
- persistent enabled state

