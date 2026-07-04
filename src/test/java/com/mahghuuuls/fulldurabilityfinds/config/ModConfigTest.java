package com.mahghuuuls.fulldurabilityfinds.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ModConfigTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setForgeMinecraftHome() throws Exception {
        Field minecraftHome = FMLInjectionData.class.getDeclaredField("minecraftHome");
        minecraftHome.setAccessible(true);
        minecraftHome.set(null, tempDir.toFile());
    }

    @Test
    void defaultOptionsUseApprovedValues() {
        CorrectionOptions options = CorrectionOptions.defaults();

        assertFalse(options.isDebug());
        assertTrue(options.isCorrectMobDrops());
        assertTrue(options.isCorrectBlockDrops());
        assertTrue(options.isCorrectItemPickups());
        assertTrue(options.isCorrectCraftingResults());
        assertTrue(options.isCorrectSmeltingResults());
        assertTrue(options.isCorrectOpenedContainers());
    }

    @Test
    void freshConfigFileUsesApprovedDefaults() throws Exception {
        File configFile = tempDir.resolve("fulldurabilityfinds.cfg").toFile();

        CorrectionOptions options = ModConfig.loadOptions(new Configuration(configFile), null);

        assertFalse(options.isDebug());
        assertTrue(options.isCorrectMobDrops());
        assertTrue(options.isCorrectBlockDrops());
        assertTrue(options.isCorrectItemPickups());
        assertTrue(options.isCorrectCraftingResults());
        assertTrue(options.isCorrectSmeltingResults());
        assertTrue(options.isCorrectOpenedContainers());

        String configText = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8);
        assertTrue(configText.contains("B:debug=false"));
        assertTrue(configText.contains("B:correctMobDrops=true"));
        assertTrue(configText.contains("B:correctBlockDrops=true"));
        assertTrue(configText.contains("B:correctItemPickups=true"));
        assertTrue(configText.contains("B:correctCraftingResults=true"));
        assertTrue(configText.contains("B:correctSmeltingResults=true"));
        assertTrue(configText.contains("B:correctOpenedContainers=true"));
    }

    @Test
    void invalidBooleanFallsBackToDefault() {
        Property debug = new Property("debug", "not-a-boolean", Property.Type.BOOLEAN);

        boolean value = ModConfig.readBooleanProperty(debug, null, "debug", CorrectionOptions.DEFAULT_DEBUG);

        assertFalse(value);
        assertTrue(debug.isBooleanValue());
        assertFalse(debug.getBoolean(true));
    }
}
