package com.mahghuuuls.fulldurabilityfinds.event;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.config.ModConfig;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import com.mahghuuuls.fulldurabilityfinds.correction.DurabilityCorrectionService;
import com.mahghuuuls.fulldurabilityfinds.debug.DebugReporter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ContainerCorrectionEvents {

    private final DurabilityCorrectionService correctionService;
    private final DebugReporter debugReporter;

    public ContainerCorrectionEvents() {
        this(new DurabilityCorrectionService(), new DebugReporter());
    }

    ContainerCorrectionEvents(DurabilityCorrectionService correctionService, DebugReporter debugReporter) {
        this.correctionService = correctionService;
        this.debugReporter = debugReporter;
    }

    @SubscribeEvent
    public void onContainerOpen(PlayerContainerEvent.Open event) {
        if (event == null || !isServerPlayer(event.getEntityPlayer())) {
            return;
        }

        handleOpenedContainer(event.getContainer(), event.getEntityPlayer(), ModConfig.getOptions());
    }

    CorrectionResult handleOpenedContainer(Container container, EntityPlayer player, CorrectionOptions options) {
        if (options == null || !options.isCorrectOpenedContainers()) {
            return CorrectionResult.unchanged(CorrectionContext.OPENED_CONTAINER);
        }

        IInventory inventory = supportedOpenedInventory(container);
        if (inventory == null) {
            return CorrectionResult.unchanged(CorrectionContext.OPENED_CONTAINER);
        }

        CorrectionResult result = correctionService.correctAll(stacksIn(inventory), CorrectionContext.OPENED_CONTAINER);
        if (result.hasCorrections()) {
            inventory.markDirty();
            container.detectAndSendChanges();
        }
        debugReporter.reportIfEnabled(player, result, options);
        return result;
    }

    private boolean isServerPlayer(EntityPlayer player) {
        return player != null
                && player.world != null
                && !player.world.isRemote;
    }

    private IInventory supportedOpenedInventory(Container container) {
        if (container instanceof ContainerChest) {
            return ((ContainerChest) container).getLowerChestInventory();
        }

        return null;
    }

    private Iterable<ItemStack> stacksIn(IInventory inventory) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            stacks.add(inventory.getStackInSlot(slot));
        }
        return stacks;
    }
}
