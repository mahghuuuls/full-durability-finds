package com.mahghuuuls.fulldurabilityfinds.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ResultCorrectionEventsTest {

    private final ResultCorrectionEvents events = new ResultCorrectionEvents();

    @BeforeAll
    static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    void correctsCraftingResultWhenEnabled() {
        ItemStack resultStack = new ItemStack(Items.IRON_SWORD, 1, 33);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleCraftingResult(resultStack, null, options);

        assertTrue(result.isCorrected());
        assertEquals(CorrectionContext.CRAFTING_RESULT, result.getContext());
        assertEquals(0, resultStack.getItemDamage());
        assertEquals(33, result.getPreviousDamage());
        assertEquals(0, result.getCorrectedDamage());
    }

    @Test
    void leavesCraftingResultUnchangedWhenDisabled() {
        ItemStack resultStack = new ItemStack(Items.IRON_SWORD, 1, 33);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, false, true, true);

        CorrectionResult result = events.handleCraftingResult(resultStack, null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(CorrectionContext.CRAFTING_RESULT, result.getContext());
        assertEquals(33, resultStack.getItemDamage());
    }

    @Test
    void correctsSmeltingResultWhenEnabled() {
        ItemStack resultStack = new ItemStack(Items.IRON_PICKAXE, 1, 48);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleSmeltingResult(resultStack, null, options);

        assertTrue(result.isCorrected());
        assertEquals(CorrectionContext.SMELTING_RESULT, result.getContext());
        assertEquals(0, resultStack.getItemDamage());
        assertEquals(48, result.getPreviousDamage());
        assertEquals(0, result.getCorrectedDamage());
    }

    @Test
    void leavesSmeltingResultUnchangedWhenDisabled() {
        ItemStack resultStack = new ItemStack(Items.IRON_PICKAXE, 1, 48);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, false, true);

        CorrectionResult result = events.handleSmeltingResult(resultStack, null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(CorrectionContext.SMELTING_RESULT, result.getContext());
        assertEquals(48, resultStack.getItemDamage());
    }

    @Test
    void leavesUnaffectedResultStacksUnchanged() {
        ItemStack resultStack = new ItemStack(Items.DIAMOND);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleCraftingResult(resultStack, null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(0, resultStack.getItemDamage());
    }
}
