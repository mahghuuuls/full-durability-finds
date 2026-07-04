package com.mahghuuuls.fulldurabilityfinds.event;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.config.ModConfig;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import com.mahghuuuls.fulldurabilityfinds.correction.DurabilityCorrectionService;
import com.mahghuuuls.fulldurabilityfinds.debug.DebugReporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class PickupCorrectionEvents {

    private final DurabilityCorrectionService correctionService;
    private final DebugReporter debugReporter;

    public PickupCorrectionEvents() {
        this(new DurabilityCorrectionService(), new DebugReporter());
    }

    PickupCorrectionEvents(DurabilityCorrectionService correctionService, DebugReporter debugReporter) {
        this.correctionService = correctionService;
        this.debugReporter = debugReporter;
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event == null) {
            return;
        }

        EntityPlayer player = event.player;
        if (player == null || player.world == null || player.world.isRemote) {
            return;
        }

        handlePickupStack(event.getStack(), player.inventory.mainInventory, player, ModConfig.getOptions());
    }

    CorrectionResult handlePickupStack(ItemStack pickedUpStack, Iterable<ItemStack> inventoryStacks, EntityPlayer player, CorrectionOptions options) {
        if (options == null || !options.isCorrectItemPickups()) {
            return CorrectionResult.unchanged(CorrectionContext.ITEM_PICKUP);
        }

        CorrectionResult result = inspectPickedUpStack(pickedUpStack);
        if (result != null) {
            debugReporter.reportIfEnabled(player, result, options);
            return result;
        }

        try {
            ItemStack inventoryStack = findPickedUpInventoryStack(pickedUpStack, inventoryStacks);
            if (inventoryStack.isEmpty()) {
                result = CorrectionResult.failed(CorrectionContext.ITEM_PICKUP, "unknown", "picked up stack was not found in inventory");
            } else {
                result = correctionService.correct(inventoryStack, CorrectionContext.ITEM_PICKUP);
            }
        } catch (RuntimeException e) {
            result = CorrectionResult.failed(CorrectionContext.ITEM_PICKUP, "unknown", e.getMessage());
        }

        debugReporter.reportIfEnabled(player, result, options);
        return result;
    }

    private CorrectionResult inspectPickedUpStack(ItemStack pickedUpStack) {
        try {
            if (!correctionService.isAffectedNormalDurabilityStack(pickedUpStack)) {
                return CorrectionResult.unchanged(CorrectionContext.ITEM_PICKUP);
            }
        } catch (RuntimeException e) {
            return CorrectionResult.failed(CorrectionContext.ITEM_PICKUP, "unknown", e.getMessage());
        }

        return null;
    }

    private ItemStack findPickedUpInventoryStack(ItemStack pickedUpStack, Iterable<ItemStack> inventoryStacks) {
        if (inventoryStacks == null) {
            return ItemStack.EMPTY;
        }

        for (ItemStack inventoryStack : inventoryStacks) {
            if (isFreshlyPickedUpMatch(pickedUpStack, inventoryStack)) {
                return inventoryStack;
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean isFreshlyPickedUpMatch(ItemStack pickedUpStack, ItemStack inventoryStack) {
        return inventoryStack != null
                && inventoryStack.getAnimationsToGo() == 5
                && ItemStack.areItemStacksEqual(pickedUpStack, inventoryStack);
    }
}
