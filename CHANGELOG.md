# Changelog

## 1.0.0

### Added

- Added full-durability correction for supported mob and entity drops.
- Added full-durability correction for supported block drops.
- Added full-durability correction for supported player item pickups.
- Added full-durability correction for supported crafting and smelting results.
- Added full-durability correction for directly opened chest inventories.
- Added behavior toggles for each correction category.
- Added disabled-by-default debug chat output for development and pack testing.

### Compatibility

- Preserves unrelated item state such as NBT, enchantments, custom names, attributes, capabilities, and stack count.
- Affects normal Minecraft item damage only; custom durability-like systems are not reset.
- Requires installation on both the server and connecting clients for multiplayer.
- Does not require Simply Unbreakable, but pairs well with mods that prevent durability loss during item use.

### Known Limits

- Does not perform world-wide cleanup, chunk scans, tile-entity scans, or general tick-based player inventory scans.
- Does not cover villager trades, arbitrary modded reward screens, direct inventory insertion paths that bypass supported events, player death drops, or non-chest opened containers.
