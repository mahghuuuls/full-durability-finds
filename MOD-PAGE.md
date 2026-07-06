# Full Durability Finds

Keeps newly found or encountered normal-durability items at full durability.

Full Durability Finds is a small Forge utility mod for packs that want normal item durability to stay out of the way. When supported item sources produce a damaged tool, weapon, armor piece, or other normal damageable item, the mod resets that normal durability damage so the item appears at full durability.

The mod changes the actual item stack state on the server or integrated server. It does not add items, recipes, enchantments, or gameplay progression.

## Features

- Corrects normal durability damage on supported mob and entity drops.
- Corrects normal durability damage on supported block drops.
- Corrects normal durability damage when players pick up supported item entities.
- Corrects supported crafting and smelting results.
- Corrects damaged normal-durability items in directly opened chest inventories.
- Preserves unrelated item state such as NBT, enchantments, names, attributes, capabilities, and stack count.
- Stays silent during normal gameplay unless debug mode is enabled.

## Configuration

The config is generated on first launch. Changes may require a game or server restart.

All correction categories are enabled by default:

- `correctMobDrops`
- `correctBlockDrops`
- `correctItemPickups`
- `correctCraftingResults`
- `correctSmeltingResults`
- `correctOpenedContainers`

`debug` is disabled by default. When enabled, it sends chat messages that describe corrections in player-associated contexts, which is useful while testing a pack.

## Compatibility Notes

Full Durability Finds affects normal Minecraft item damage only. It does not reset custom durability-like systems such as mana, RF/FE charge, ammo, spell charge, or mod-specific NBT resource bars.

For multiplayer, install the mod on the server. A client-only install does not promise correction for server-authoritative item state.

The mod pairs well with mods that prevent durability loss during active item use, such as Simply Unbreakable, but it does not require them.

## Known Limits

Full Durability Finds does not scan the world, loaded chunks, tile entities, or every player inventory tick. Existing damaged items in old containers are corrected only when they appear through a supported path, such as opening a supported chest.

The first release does not cover villager trades, arbitrary modded reward screens, direct inventory insertion paths that bypass supported events, player death drops, or non-chest opened containers such as furnaces, hoppers, dispensers, shulker boxes, and horse inventories.
