package com.mahghuuuls.fulldurabilityfinds.event;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.config.ModConfig;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import com.mahghuuuls.fulldurabilityfinds.correction.DurabilityCorrectionService;
import com.mahghuuuls.fulldurabilityfinds.debug.DebugReporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DropCorrectionEvents {

    private final DurabilityCorrectionService correctionService;
    private final DebugReporter debugReporter;

    public DropCorrectionEvents() {
        this(new DurabilityCorrectionService(), new DebugReporter());
    }

    DropCorrectionEvents(DurabilityCorrectionService correctionService, DebugReporter debugReporter) {
        this.correctionService = correctionService;
        this.debugReporter = debugReporter;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDrops(LivingDropsEvent event) {
        if (!isSupportedLivingDropEvent(event)) {
            return;
        }

        handleMobDropEntities(event.getDrops(), playerFromSource(event.getSource()), ModConfig.getOptions());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event == null || event.getWorld() == null || event.getWorld().isRemote) {
            return;
        }

        handleBlockDropStacks(event.getDrops(), event.getHarvester(), ModConfig.getOptions());
    }

    CorrectionResult handleMobDropStacks(Iterable<ItemStack> stacks, EntityPlayer player, CorrectionOptions options) {
        return handleDropStacks(stacks, player, options, options != null && options.isCorrectMobDrops(), CorrectionContext.MOB_DROP);
    }

    CorrectionResult handleBlockDropStacks(Iterable<ItemStack> stacks, EntityPlayer player, CorrectionOptions options) {
        return handleDropStacks(stacks, player, options, options != null && options.isCorrectBlockDrops(), CorrectionContext.BLOCK_DROP);
    }

    boolean isSupportedLivingDropEvent(LivingDropsEvent event) {
        return event != null
                && !event.isCanceled()
                && !(event instanceof PlayerDropsEvent)
                && event.getEntityLiving() != null
                && event.getEntityLiving().world != null
                && !event.getEntityLiving().world.isRemote;
    }

    CorrectionResult handleMobDropEntities(Iterable<EntityItem> drops, EntityPlayer player, CorrectionOptions options) {
        if (options == null || !options.isCorrectMobDrops()) {
            return CorrectionResult.unchanged(CorrectionContext.MOB_DROP);
        }

        CorrectionResult result = correctEntityItemDrops(drops);
        debugReporter.reportIfEnabled(player, result, options);
        return result;
    }

    private CorrectionResult handleDropStacks(
            Iterable<ItemStack> stacks,
            EntityPlayer player,
            CorrectionOptions options,
            boolean enabled,
            CorrectionContext context) {
        if (options == null || !enabled) {
            return CorrectionResult.unchanged(context);
        }

        CorrectionResult result = correctionService.correctAll(stacks, context);
        debugReporter.reportIfEnabled(player, result, options);
        return result;
    }

    private CorrectionResult correctEntityItemDrops(Iterable<EntityItem> drops) {
        if (drops == null) {
            return CorrectionResult.batch(CorrectionContext.MOB_DROP, 0, 0, 0);
        }

        int inspectedStacks = 0;
        int correctedStacks = 0;
        int failedStacks = 0;

        for (EntityItem drop : drops) {
            inspectedStacks++;
            CorrectionResult result = correctEntityItemDrop(drop);
            if (result.isCorrected()) {
                correctedStacks++;
            } else if (result.isFailed()) {
                failedStacks++;
            }
        }

        return CorrectionResult.batch(CorrectionContext.MOB_DROP, inspectedStacks, correctedStacks, failedStacks);
    }

    private CorrectionResult correctEntityItemDrop(EntityItem drop) {
        try {
            ItemStack stack = drop == null ? ItemStack.EMPTY : drop.getItem();
            CorrectionResult result = correctionService.correct(stack, CorrectionContext.MOB_DROP);
            if (result.isCorrected() && drop != null) {
                drop.setItem(stack);
            }
            return result;
        } catch (RuntimeException e) {
            return CorrectionResult.failed(CorrectionContext.MOB_DROP, "unknown", e.getMessage());
        }
    }

    private EntityPlayer playerFromSource(DamageSource source) {
        Entity trueSource = source == null ? null : source.getTrueSource();
        return trueSource instanceof EntityPlayer ? (EntityPlayer) trueSource : null;
    }
}
