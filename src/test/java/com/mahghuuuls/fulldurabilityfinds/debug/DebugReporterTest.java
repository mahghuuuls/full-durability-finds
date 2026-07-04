package com.mahghuuuls.fulldurabilityfinds.debug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mahghuuuls.fulldurabilityfinds.Tags;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionContext;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import org.junit.jupiter.api.Test;

class DebugReporterTest {

    private final DebugReporter reporter = new DebugReporter();

    @Test
    void correctedMessageIncludesCategoryItemDamageAndCount() {
        CorrectionResult result = CorrectionResult.corrected(
                CorrectionContext.ITEM_PICKUP,
                "minecraft:iron_sword",
                1,
                12,
                0);

        String message = reporter.messageFor(result);

        assertTrue(message.contains(Tags.MOD_NAME));
        assertTrue(message.contains("pickup"));
        assertTrue(message.contains("minecraft:iron_sword"));
        assertTrue(message.contains("12 -> 0"));
        assertTrue(message.contains("x1"));
    }

    @Test
    void batchMessageIncludesCorrectedAndInspectedCounts() {
        CorrectionResult result = CorrectionResult.batch(CorrectionContext.OPENED_CONTAINER, 4, 2, 1);

        String message = reporter.messageFor(result);

        assertTrue(message.contains("opened container"));
        assertTrue(message.contains("corrected 2 stack(s) out of 4"));
        assertTrue(message.contains("1 failed"));
    }

    @Test
    void unchangedAndEmptyBatchResultsDoNotCreateMessages() {
        assertEquals("", reporter.messageFor(CorrectionResult.unchanged(CorrectionContext.ITEM_PICKUP)));
        assertEquals("", reporter.messageFor(CorrectionResult.batch(CorrectionContext.OPENED_CONTAINER, 3, 0, 0)));
    }
}
