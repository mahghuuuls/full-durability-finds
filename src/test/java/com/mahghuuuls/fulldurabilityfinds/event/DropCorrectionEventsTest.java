package com.mahghuuuls.fulldurabilityfinds.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import java.util.Arrays;
import java.util.Collections;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DropCorrectionEventsTest {

    private final DropCorrectionEvents events = new DropCorrectionEvents();

    @BeforeAll
    static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    void correctsMobDropStacksWhenEnabled() {
        ItemStack damaged = new ItemStack(Items.IRON_SWORD, 1, 18);
        ItemStack unaffected = new ItemStack(Items.DIAMOND);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleMobDropStacks(Arrays.asList(damaged, unaffected), null, options);

        assertTrue(result.isBatch());
        assertEquals(CorrectionContext.MOB_DROP, result.getContext());
        assertEquals(2, result.getInspectedStacks());
        assertEquals(1, result.getCorrectedStacks());
        assertEquals(0, result.getFailedStacks());
        assertEquals(0, damaged.getItemDamage());
        assertEquals(0, unaffected.getItemDamage());
    }

    @Test
    void correctsMobDropEntityItemStacksWhenEnabled() {
        ItemStack damaged = new ItemStack(Items.IRON_SWORD, 1, 18);
        EntityItem drop = new EntityItem(null, 0.0D, 0.0D, 0.0D, damaged);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleMobDropEntities(Arrays.asList(drop), null, options);

        assertTrue(result.isBatch());
        assertEquals(1, result.getInspectedStacks());
        assertEquals(1, result.getCorrectedStacks());
        assertEquals(0, drop.getItem().getItemDamage());
    }

    @Test
    void leavesMobDropStacksUnchangedWhenDisabled() {
        ItemStack damaged = new ItemStack(Items.IRON_SWORD, 1, 18);
        CorrectionOptions options = new CorrectionOptions(false, false, true, true, true, true, true);

        CorrectionResult result = events.handleMobDropStacks(Arrays.asList(damaged), null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(CorrectionContext.MOB_DROP, result.getContext());
        assertEquals(18, damaged.getItemDamage());
    }

    @Test
    void doesNotSupportPlayerDeathDropsAsMobDrops() {
        PlayerDropsEvent event = new PlayerDropsEvent(
                (EntityPlayer) null,
                DamageSource.GENERIC,
                Collections.<EntityItem>emptyList(),
                true);

        assertFalse(events.isSupportedLivingDropEvent(event));
    }

    @Test
    void correctsBlockDropStacksWhenEnabled() {
        ItemStack damaged = new ItemStack(Items.IRON_PICKAXE, 1, 42);
        ItemStack full = new ItemStack(Items.IRON_AXE, 1, 0);
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult result = events.handleBlockDropStacks(Arrays.asList(damaged, full), null, options);

        assertTrue(result.isBatch());
        assertEquals(CorrectionContext.BLOCK_DROP, result.getContext());
        assertEquals(2, result.getInspectedStacks());
        assertEquals(1, result.getCorrectedStacks());
        assertEquals(0, result.getFailedStacks());
        assertEquals(0, damaged.getItemDamage());
        assertEquals(0, full.getItemDamage());
    }

    @Test
    void leavesBlockDropStacksUnchangedWhenDisabled() {
        ItemStack damaged = new ItemStack(Items.IRON_PICKAXE, 1, 42);
        CorrectionOptions options = new CorrectionOptions(false, true, false, true, true, true, true);

        CorrectionResult result = events.handleBlockDropStacks(Arrays.asList(damaged), null, options);

        assertFalse(result.isCorrected());
        assertEquals(CorrectionResult.Status.UNCHANGED, result.getStatus());
        assertEquals(CorrectionContext.BLOCK_DROP, result.getContext());
        assertEquals(42, damaged.getItemDamage());
    }

    @Test
    void handlesNullDropCollectionsAsEmptyBatches() {
        CorrectionOptions options = new CorrectionOptions(false, true, true, true, true, true, true);

        CorrectionResult mobResult = events.handleMobDropStacks(null, null, options);
        CorrectionResult blockResult = events.handleBlockDropStacks(null, null, options);

        assertTrue(mobResult.isBatch());
        assertEquals(0, mobResult.getInspectedStacks());
        assertTrue(blockResult.isBatch());
        assertEquals(0, blockResult.getInspectedStacks());
    }
}
