# Full Durability Finds

This mod makes obtained tools and other normal-durability items have full durability. I play with [Simply Unbreakable](https://www.curseforge.com/minecraft/mc-mods/simply-unbreakable-1-12-2), which prevents items from losing durability, but items can still be obtained already damaged. This mod was made with that scenario in mind. Used on its own, it may give players ways to restore durability without using an anvil.

For multiplayer, install the mod on both the server and every connecting client. It affects normal Minecraft item damage only and does not reset custom durability-like systems such as mana, RF/FE charge, ammo, spell charge, or mod-specific NBT resource bars.

## Features

- Corrects normal durability damage on supported mob and entity drops.
- Corrects normal durability damage on supported block drops.
- Corrects normal durability damage when players pick up supported item entities.
- Corrects supported crafting and smelting results.
- Corrects damaged normal-durability items in directly opened chest inventories.
- Preserves unrelated item state such as NBT, enchantments, names, attributes, capabilities, and stack count.
- Stays silent during normal gameplay unless debug mode is enabled.

## Configuration

All correction categories are enabled by default:

- `correctMobDrops`
- `correctBlockDrops`
- `correctItemPickups`
- `correctCraftingResults`
- `correctSmeltingResults`
- `correctOpenedContainers`

`debug` is disabled by default. When enabled, it sends chat messages that describe corrections in player-associated contexts, which is useful while testing a pack.

## Known Limits

It does not scan the world, loaded chunks, tile entities, or every player inventory tick. Existing damaged items in old containers are corrected only when they appear through a supported path, such as opening a supported chest.

It does not cover villager trades, arbitrary modded reward screens, direct inventory insertion paths that bypass supported events, player death drops, or non-chest opened containers such as furnaces, hoppers, dispensers, shulker boxes, and horse inventories.

Source code is available on [GitHub](https://github.com/mahghuuuls/full-durability-finds).
