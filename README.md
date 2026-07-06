# Full Durability Finds

Keeps newly found or encountered normal-durability items at full durability.

Full Durability Finds is a Minecraft 1.12.2 Forge mod. It corrects normal Minecraft durability damage on supported newly found or encountered item stacks, while leaving custom durability-like systems alone.

This repository contains the source code, project metadata, and release documentation for the mod.

## Public Mod Page Copy

Player-facing download-page copy is kept in [`MOD-PAGE.md`](MOD-PAGE.md).

Release notes are kept in [`CHANGELOG.md`](CHANGELOG.md).

## Project Details

- Minecraft: 1.12.2
- Loader: Forge
- Java target: Java 8
- Mod ID: `fulldurabilityfinds`
- Package: `com.mahghuuuls.fulldurabilityfinds`
- License: MIT

## Configuration

The generated config includes behavior toggles for:

- `debug`
- `correctMobDrops`
- `correctBlockDrops`
- `correctItemPickups`
- `correctCraftingResults`
- `correctSmeltingResults`
- `correctOpenedContainers`

Correction categories are enabled by default. `debug` is disabled by default. Config changes may require a game or server restart.

## Build

Use the included Gradle wrapper:

```bat
.\gradlew.bat build
```

The release artifact is produced during the normal Gradle build flow.

## Scope Notes

The mod corrects normal Minecraft item damage through supported event and container paths. It does not perform world-wide scans, chunk scans, tile-entity scans, or general tick-based player inventory scans.

The first release does not cover villager trades, arbitrary modded reward screens, direct inventory insertion paths that bypass supported events, player death drops, or non-chest opened containers.

## License

Full Durability Finds is licensed under the MIT License. See [`LICENSE`](LICENSE).

This project was initialized from CleanroomMC ForgeDevEnv. See [`THIRD-PARTY-NOTICES.md`](THIRD-PARTY-NOTICES.md) for the template notice.
