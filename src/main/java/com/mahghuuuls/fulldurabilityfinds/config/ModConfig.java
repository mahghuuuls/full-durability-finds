package com.mahghuuuls.fulldurabilityfinds.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;

public final class ModConfig {

    private static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;

    private static CorrectionOptions options = CorrectionOptions.defaults();

    private ModConfig() {
    }

    public static CorrectionOptions getOptions() {
        return options;
    }

    public static void load(File configFile, Logger logger) {
        options = loadOptions(new Configuration(configFile), logger);
    }

    public static CorrectionOptions loadOptions(Configuration config, Logger logger) {
        try {
            config.load();

            CorrectionOptions loadedOptions = new CorrectionOptions(
                    readBoolean(config, logger, "debug", CorrectionOptions.DEFAULT_DEBUG,
                            "Enables detailed chat messages for correction behavior."),
                    readBoolean(config, logger, "correctMobDrops", CorrectionOptions.DEFAULT_CORRECT_MOB_DROPS,
                            "Corrects normal durability damage on supported mob or entity drops."),
                    readBoolean(config, logger, "correctBlockDrops", CorrectionOptions.DEFAULT_CORRECT_BLOCK_DROPS,
                            "Corrects normal durability damage on supported block drops."),
                    readBoolean(config, logger, "correctItemPickups", CorrectionOptions.DEFAULT_CORRECT_ITEM_PICKUPS,
                            "Corrects normal durability damage when players pick up supported item entities."),
                    readBoolean(config, logger, "correctCraftingResults", CorrectionOptions.DEFAULT_CORRECT_CRAFTING_RESULTS,
                            "Corrects normal durability damage on supported crafting results."),
                    readBoolean(config, logger, "correctSmeltingResults", CorrectionOptions.DEFAULT_CORRECT_SMELTING_RESULTS,
                            "Corrects normal durability damage on supported smelting results."),
                    readBoolean(config, logger, "correctOpenedContainers", CorrectionOptions.DEFAULT_CORRECT_OPENED_CONTAINERS,
                            "Corrects normal durability damage in directly opened supported container inventories."));

            options = loadedOptions;
            return loadedOptions;
        } catch (RuntimeException e) {
            warn(logger, "Failed to load config. Falling back to defaults.", e);
            options = CorrectionOptions.defaults();
            return options;
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static boolean readBoolean(Configuration config, Logger logger, String name, boolean defaultValue, String comment) {
        Property property = config.get(CATEGORY_GENERAL, name, defaultValue, comment + " Requires restart. [default: " + defaultValue + "]");
        property.setRequiresMcRestart(true);
        return readBooleanProperty(property, logger, name, defaultValue);
    }

    static boolean readBooleanProperty(Property property, Logger logger, String name, boolean defaultValue) {
        if (!property.isBooleanValue()) {
            warn(logger, "Invalid boolean value '{}' for config option '{}'. Using default '{}'.",
                    property.getString(), name, Boolean.toString(defaultValue));
            property.setValue(Boolean.toString(defaultValue));
            return defaultValue;
        }

        return property.getBoolean(defaultValue);
    }

    private static void warn(Logger logger, String message, Object... args) {
        if (logger != null) {
            logger.warn(message, args);
        }
    }
}
