package com.mahghuuuls.fulldurabilityfinds.correction;

public final class CorrectionResult {

    public enum Status {
        UNCHANGED,
        CORRECTED,
        FAILED,
        BATCH
    }

    private final CorrectionContext context;
    private final Status status;
    private final String itemIdentifier;
    private final int stackCount;
    private final int previousDamage;
    private final int correctedDamage;
    private final int inspectedStacks;
    private final int correctedStacks;
    private final int failedStacks;
    private final String failureMessage;

    private CorrectionResult(
            CorrectionContext context,
            Status status,
            String itemIdentifier,
            int stackCount,
            int previousDamage,
            int correctedDamage,
            int inspectedStacks,
            int correctedStacks,
            int failedStacks,
            String failureMessage) {
        this.context = context;
        this.status = status;
        this.itemIdentifier = itemIdentifier;
        this.stackCount = stackCount;
        this.previousDamage = previousDamage;
        this.correctedDamage = correctedDamage;
        this.inspectedStacks = inspectedStacks;
        this.correctedStacks = correctedStacks;
        this.failedStacks = failedStacks;
        this.failureMessage = failureMessage;
    }

    public static CorrectionResult unchanged(CorrectionContext context) {
        return new CorrectionResult(context, Status.UNCHANGED, "", 0, 0, 0, 0, 0, 0, "");
    }

    public static CorrectionResult corrected(CorrectionContext context, String itemIdentifier, int stackCount, int previousDamage, int correctedDamage) {
        return new CorrectionResult(context, Status.CORRECTED, itemIdentifier, stackCount, previousDamage, correctedDamage, 1, 1, 0, "");
    }

    public static CorrectionResult failed(CorrectionContext context, String itemIdentifier, String failureMessage) {
        return new CorrectionResult(context, Status.FAILED, itemIdentifier, 0, 0, 0, 1, 0, 1, failureMessage);
    }

    public static CorrectionResult batch(CorrectionContext context, int inspectedStacks, int correctedStacks, int failedStacks) {
        return new CorrectionResult(context, Status.BATCH, "", 0, 0, 0, inspectedStacks, correctedStacks, failedStacks, "");
    }

    public CorrectionContext getContext() {
        return context;
    }

    public Status getStatus() {
        return status;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public int getStackCount() {
        return stackCount;
    }

    public int getPreviousDamage() {
        return previousDamage;
    }

    public int getCorrectedDamage() {
        return correctedDamage;
    }

    public int getInspectedStacks() {
        return inspectedStacks;
    }

    public int getCorrectedStacks() {
        return correctedStacks;
    }

    public int getFailedStacks() {
        return failedStacks;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean isCorrected() {
        return status == Status.CORRECTED;
    }

    public boolean isFailed() {
        return status == Status.FAILED;
    }

    public boolean isBatch() {
        return status == Status.BATCH;
    }

    public boolean hasCorrections() {
        return isCorrected() || (isBatch() && correctedStacks > 0);
    }
}
