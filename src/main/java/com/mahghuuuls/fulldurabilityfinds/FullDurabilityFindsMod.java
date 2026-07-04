package com.mahghuuuls.fulldurabilityfinds;

import com.mahghuuuls.fulldurabilityfinds.config.ModConfig;
import com.mahghuuuls.fulldurabilityfinds.event.PickupCorrectionEvents;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class FullDurabilityFindsMod {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModConfig.load(event.getSuggestedConfigurationFile(), LOGGER);
        FMLCommonHandler.instance().bus().register(new PickupCorrectionEvents());
        LOGGER.info("{} initialized.", Tags.MOD_NAME);
    }
}
