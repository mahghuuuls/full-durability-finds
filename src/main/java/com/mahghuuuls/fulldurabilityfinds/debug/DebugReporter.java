package com.mahghuuuls.fulldurabilityfinds.debug;

import com.mahghuuuls.fulldurabilityfinds.Tags;
import com.mahghuuuls.fulldurabilityfinds.config.CorrectionOptions;
import com.mahghuuuls.fulldurabilityfinds.correction.CorrectionResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public final class DebugReporter {

    public void reportIfEnabled(EntityPlayer player, CorrectionResult result, CorrectionOptions options) {
        if (player == null || result == null || options == null || !options.isDebug()) {
            return;
        }

        String message = messageFor(result);
        if (!message.isEmpty()) {
            player.sendMessage(new TextComponentString(message));
        }
    }

    public String messageFor(CorrectionResult result) {
        if (result == null) {
            return "";
        }

        if (result.isCorrected()) {
            return prefix() + contextName(result)
                    + ": corrected " + result.getItemIdentifier()
                    + " damage " + result.getPreviousDamage()
                    + " -> " + result.getCorrectedDamage()
                    + " (x" + result.getStackCount() + ")";
        }

        if (result.isBatch() && result.getCorrectedStacks() > 0) {
            return prefix() + contextName(result)
                    + ": corrected " + result.getCorrectedStacks()
                    + " stack(s) out of " + result.getInspectedStacks()
                    + inspectedFailureSuffix(result);
        }

        if (result.isFailed()) {
            return prefix() + contextName(result)
                    + ": skipped " + result.getItemIdentifier()
                    + " because " + safeFailureMessage(result);
        }

        return "";
    }

    private String inspectedFailureSuffix(CorrectionResult result) {
        if (result.getFailedStacks() <= 0) {
            return "";
        }

        return ", " + result.getFailedStacks() + " failed";
    }

    private String safeFailureMessage(CorrectionResult result) {
        String failureMessage = result.getFailureMessage();
        if (failureMessage == null || failureMessage.trim().isEmpty()) {
            return "the stack could not be inspected safely";
        }

        return failureMessage;
    }

    private String contextName(CorrectionResult result) {
        if (result.getContext() == null) {
            return "unknown context";
        }

        return result.getContext().getDisplayName();
    }

    private String prefix() {
        return "[" + Tags.MOD_NAME + "] ";
    }
}
