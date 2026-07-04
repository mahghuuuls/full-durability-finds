package com.mahghuuuls.fulldurabilityfinds.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ContainerCorrectionEventsTest {

    private final ContainerCorrectionEvents events = new ContainerCorrectionEvents();

    @BeforeAll
    static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    void correctsChestInventoryWhenEnabled() {
        InventoryBasic chestInventory = new InventoryBasic("test", false, 9);
        ItemStack damaged = new ItemStack(Items.IRON_SWORD, 1, 22);
        chestInventory.setInventorySlotContents(0, damaged);
        chestInventory.setInventorySlotContents(1, new ItemStack(Items.DIAMOND));
        ContainerChest container = new ContainerChest(new InventoryBasic("player", false, 36), chestInventory, null);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleOpenedContainer(container, null, options);

        assertTrue(result.isBatch());
        assertEquals(CorrectionContext.OPENED_CONTAINER, result.getContext());
        assertEquals(9, result.getInspectedStacks());
        assertEquals(1, result.getCorrectedStacks());
        assertEquals(0, damaged.getItemDamage());
    }

    @Test
    void leavesChestInventoryUnchangedWhenDisabled() {
        InventoryBasic chestInventory = new InventoryBasic("test", false, 9);
        ItemStack damaged = new ItemStack(Items.IRON_SWORD, 1, 22);
        chestInventory.setInventorySlotContents(0, damaged);
        ContainerChest container = new ContainerChest(new InventoryBasic("player", false, 36), chestInventory, null);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, false);

        CorrectionResult result = events.handleOpenedContainer(container, null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(CorrectionContext.OPENED_CONTAINER, result.getContext());
        assertEquals(22, damaged.getItemDamage());
    }

    @Test
    void ignoresUnsupportedContainers() {
        final InventoryBasic unsupportedInventory = new InventoryBasic("unsupported", false, 1);
        ItemStack damaged = new ItemStack(Items.IRON_SWORD, 1, 22);
        unsupportedInventory.setInventorySlotContents(0, damaged);
        Container unsupported = new Container() {
            {
                addSlotToContainer(new Slot(unsupportedInventory, 0, 0, 0));
            }

            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        };
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleOpenedContainer(unsupported, null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(22, damaged.getItemDamage());
    }

    @Test
    void ignoresNullContainer() {
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleOpenedContainer(null, null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
    }
}
