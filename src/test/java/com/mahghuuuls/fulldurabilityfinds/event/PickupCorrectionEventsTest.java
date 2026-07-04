package com.mahghuuuls.fulldurabilityfinds.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import java.util.Arrays;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PickupCorrectionEventsTest {

    private final PickupCorrectionEvents events = new PickupCorrectionEvents();

    @BeforeAll
    static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    void correctsPickupStackWhenEnabled() {
        ItemStack pickedUpStack = new ItemStack(Items.IRON_SWORD, 1, 25);
        ItemStack inventoryStack = pickedUpStack.copy();
        inventoryStack.setAnimationsToGo(5);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handlePickupStack(pickedUpStack, Arrays.asList(inventoryStack), null, options);

        assertTrue(result.isCorrected());
        assertEquals(25, pickedUpStack.getItemDamage());
        assertEquals(0, inventoryStack.getItemDamage());
        assertEquals(25, result.getPreviousDamage());
        assertEquals(0, result.getCorrectedDamage());
    }

    @Test
    void leavesPickupStackUnchangedWhenDisabled() {
        ItemStack pickedUpStack = new ItemStack(Items.IRON_SWORD, 1, 25);
        ItemStack inventoryStack = pickedUpStack.copy();
        inventoryStack.setAnimationsToGo(5);
        CorrectionOptions options = new CorrectionOptions(false, true, true, false, true, true, true);

        CorrectionResult result = events.handlePickupStack(pickedUpStack, Arrays.asList(inventoryStack), null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(25, pickedUpStack.getItemDamage());
        assertEquals(25, inventoryStack.getItemDamage());
    }

    @Test
    void leavesGroundStackUnchangedWhenNoMatchingInventoryStackExists() {
        ItemStack pickedUpStack = new ItemStack(Items.IRON_SWORD, 1, 25);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handlePickupStack(pickedUpStack, Arrays.asList(ItemStack.EMPTY), null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.FAILED, result.getStatus());
        assertEquals(25, pickedUpStack.getItemDamage());
    }

    @Test
    void correctsFreshlyPickedUpStackInsteadOfOlderIdenticalStack() {
        ItemStack pickedUpStack = new ItemStack(Items.IRON_SWORD, 1, 25);
        ItemStack olderInventoryStack = pickedUpStack.copy();
        olderInventoryStack.setAnimationsToGo(0);
        ItemStack pickedUpInventoryStack = pickedUpStack.copy();
        pickedUpInventoryStack.setAnimationsToGo(5);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handlePickupStack(
                pickedUpStack,
                Arrays.asList(olderInventoryStack, pickedUpInventoryStack),
                null,
                options);

        assertTrue(result.isCorrected());
        assertEquals(25, olderInventoryStack.getItemDamage());
        assertEquals(0, pickedUpInventoryStack.getItemDamage());
    }

    @Test
    void unaffectedPickupIsUnchangedInsteadOfFailure() {
        ItemStack pickedUpStack = new ItemStack(Items.DIAMOND);
        ItemStack inventoryStack = pickedUpStack.copy();
        inventoryStack.setAnimationsToGo(5);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handlePickupStack(pickedUpStack, Arrays.asList(inventoryStack), null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
    }
}
