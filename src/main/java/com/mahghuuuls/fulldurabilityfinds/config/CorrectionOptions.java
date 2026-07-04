package com.mahghuuuls.fulldurabilityfinds.config;

public final class CorrectionOptions {

    public static final boolean DEFAULT_DEBUG = false;
    public static final boolean DEFAULT_CORRECT_MOB_DROPS = true;
    public static final boolean DEFAULT_CORRECT_BLOCK_DROPS = true;
    public static final boolean DEFAULT_CORRECT_ITEM_PICKUPS = true;
    public static final boolean DEFAULT_CORRECT_CRAFTING_RESULTS = true;
    public static final boolean DEFAULT_CORRECT_SMELTING_RESULTS = true;
    public static final boolean DEFAULT_CORRECT_OPENED_CONTAINERS = true;

    private final boolean debug;
    private final boolean correctMobDrops;
    private final boolean correctBlockDrops;
    private final boolean correctItemPickups;
    private final boolean correctCraftingResults;
    private final boolean correctSmeltingResults;
    private final boolean correctOpenedContainers;

    public CorrectionOptions(
            boolean debug,
            boolean correctMobDrops,
            boolean correctBlockDrops,
            boolean correctItemPickups,
            boolean correctCraftingResults,
            boolean correctSmeltingResults,
            boolean correctOpenedContainers) {
        this.debug = debug;
        this.correctMobDrops = correctMobDrops;
        this.correctBlockDrops = correctBlockDrops;
        this.correctItemPickups = correctItemPickups;
        this.correctCraftingResults = correctCraftingResults;
        this.correctSmeltingResults = correctSmeltingResults;
        this.correctOpenedContainers = correctOpenedContainers;
    }

    public static CorrectionOptions defaults() {
        return new CorrectionOptions(
                DEFAULT_DEBUG,
                DEFAULT_CORRECT_MOB_DROPS,
                DEFAULT_CORRECT_BLOCK_DROPS,
                DEFAULT_CORRECT_ITEM_PICKUPS,
                DEFAULT_CORRECT_CRAFTING_RESULTS,
                DEFAULT_CORRECT_SMELTING_RESULTS,
                DEFAULT_CORRECT_OPENED_CONTAINERS);
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isCorrectMobDrops() {
        return correctMobDrops;
    }

    public boolean isCorrectBlockDrops() {
        return correctBlockDrops;
    }

    public boolean isCorrectItemPickups() {
        return correctItemPickups;
    }

    public boolean isCorrectCraftingResults() {
        return correctCraftingResults;
    }

    public boolean isCorrectSmeltingResults() {
        return correctSmeltingResults;
    }

    public boolean isCorrectOpenedContainers() {
        return correctOpenedContainers;
    }
}
