package com.mahghuuuls.fulldurabilityfinds.correction;

public enum CorrectionContext {
    MOB_DROP("mob drop"),
    BLOCK_DROP("block drop"),
    ITEM_PICKUP("pickup"),
    CRAFTING_RESULT("crafting"),
    SMELTING_RESULT("smelting"),
    OPENED_CONTAINER("opened container");

    private final String displayName;

    CorrectionContext(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
