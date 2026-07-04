package com.mahghuuuls.fulldurabilityfinds.correction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DurabilityCorrectionServiceTest {

    private final DurabilityCorrectionService service = new DurabilityCorrectionService();

    @BeforeAll
    static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    void correctsDamagedNormalDurabilityStackAndPreservesUnrelatedState() {
        ItemStack stack = new ItemStack(Items.IRON_SWORD, 2, 37);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("customKey", "customValue");
        stack.setTagCompound(tag);

        CorrectionResult result = service.correct(stack, CorrectionContext.ITEM_PICKUP);

        assertTrue(result.isCorrected());
        assertEquals(0, stack.getItemDamage());
        assertEquals(2, stack.getCount());
        assertSame(tag, stack.getTagCompound());
        assertEquals("customValue", stack.getTagCompound().getString("customKey"));
        assertEquals(37, result.getPreviousDamage());
        assertEquals(0, result.getCorrectedDamage());
        assertEquals("minecraft:iron_sword", result.getItemIdentifier());
    }

    @Test
    void leavesNonDamageableStackUnchanged() {
        ItemStack stack = new ItemStack(Items.DIAMOND);

        CorrectionResult result = service.correct(stack, CorrectionContext.ITEM_PICKUP);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(0, stack.getItemDamage());
    }

    @Test
    void leavesFullDurabilityStackUnchanged() {
        ItemStack stack = new ItemStack(Items.IRON_SWORD, 1, 0);

        CorrectionResult result = service.correct(stack, CorrectionContext.ITEM_PICKUP);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(0, stack.getItemDamage());
    }

    @Test
    void batchResultCountsInspectedCorrectedAndFailedStacks() {
        ItemStack damaged = new ItemStack(Items.IRON_PICKAXE, 1, 10);
        ItemStack full = new ItemStack(Items.IRON_AXE, 1, 0);
        ItemStack plain = new ItemStack(Items.DIAMOND);

        CorrectionResult result = service.correctAll(Arrays.asList(damaged, full, plain), CorrectionContext.OPENED_CONTAINER);

        assertTrue(result.isBatch());
        assertEquals(3, result.getInspectedStacks());
        assertEquals(1, result.getCorrectedStacks());
        assertEquals(0, result.getFailedStacks());
        assertEquals(0, damaged.getItemDamage());
    }
}
