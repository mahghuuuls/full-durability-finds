# Full Durability Finds

Keeps newly found or encountered normal-durability items at full durability.

Full Durability Finds is a Minecraft 1.12.2 Forge mod that corrects normal Minecraft durability damage on supported newly found or encountered item stacks, while leaving custom durability-like systems alone.

## Release Docs

- [Mod page copy](MOD-PAGE.md)
- [Changelog](CHANGELOG.md)

## Project Details

- Minecraft: 1.12.2
- Loader: Forge
- Java target: Java 8
- Mod ID: `fulldurabilityfinds`
- Package: `com.mahghuuuls.fulldurabilityfinds`
- License: MIT

## Installation Note

For multiplayer, install the mod on both the server and every connecting client.

## Config Keys

- `debug`
- `correctMobDrops`
- `correctBlockDrops`
- `correctItemPickups`
- `correctCraftingResults`
- `correctSmeltingResults`
- `correctOpenedContainers`

Correction categories are enabled by default. `debug` is disabled by default.

## Scope

The mod corrects normal Minecraft item damage through supported event and container paths. It does not perform world-wide scans, chunk scans, tile-entity scans, or general tick-based player inventory scans.

Full Durability Finds does not cover villager trades, arbitrary modded reward screens, direct inventory insertion paths that bypass supported events, player death drops, or non-chest opened containers.

## License

Full Durability Finds is licensed under the MIT License. See [`LICENSE`](LICENSE).

This project was initialized from CleanroomMC ForgeDevEnv. See [`THIRD-PARTY-NOTICES.md`](THIRD-PARTY-NOTICES.md) for the template notice.
