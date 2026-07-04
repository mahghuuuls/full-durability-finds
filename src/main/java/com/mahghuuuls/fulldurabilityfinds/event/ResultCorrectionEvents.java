package com.mahghuuuls.fulldurabilityfinds.event;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.config.ModConfig;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import com.mahghuuuls.fulldurabilityfinds.correction.DurabilityCorrectionService;
import com.mahghuuuls.fulldurabilityfinds.debug.DebugReporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public final class ResultCorrectionEvents {

    private final DurabilityCorrectionService correctionService;
    private final DebugReporter debugReporter;

    public ResultCorrectionEvents() {
        this(new DurabilityCorrectionService(), new DebugReporter());
    }

    ResultCorrectionEvents(DurabilityCorrectionService correctionService, DebugReporter debugReporter) {
        this.correctionService = correctionService;
        this.debugReporter = debugReporter;
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event == null || !isServerPlayer(event.player)) {
            return;
        }

        handleCraftingResult(event.crafting, event.player, ModConfig.getOptions());
    }

    @SubscribeEvent
    public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event == null || !isServerPlayer(event.player)) {
            return;
        }

        handleSmeltingResult(event.smelting, event.player, ModConfig.getOptions());
    }

    CorrectionResult handleCraftingResult(ItemStack resultStack, EntityPlayer player, CorrectionOptions options) {
        return handleResultStack(
                resultStack,
                player,
                options,
                options != null && options.isCorrectCraftingResults(),
                CorrectionContext.CRAFTING_RESULT);
    }

    CorrectionResult handleSmeltingResult(ItemStack resultStack, EntityPlayer player, CorrectionOptions options) {
        return handleResultStack(
                resultStack,
                player,
                options,
                options != null && options.isCorrectSmeltingResults(),
                CorrectionContext.SMELTING_RESULT);
    }

    private boolean isServerPlayer(EntityPlayer player) {
        return player != null
                && player.world != null
                && !player.world.isRemote;
    }

    private CorrectionResult handleResultStack(
            ItemStack resultStack,
            EntityPlayer player,
            CorrectionOptions options,
            boolean enabled,
            CorrectionContext context) {
        if (options == null || !enabled) {
            return CorrectionResult.unchanged(context);
        }

        CorrectionResult result = correctionService.correct(resultStack, context);
        debugReporter.reportIfEnabled(player, result, options);
        return result;
    }
}
