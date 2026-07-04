package com.mahghuuuls.fulldurabilityfinds.correction;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class DurabilityCorrectionService {

    public CorrectionResult correct(ItemStack stack, CorrectionContext context) {
        context = normalizeContext(context);

        try {
            if (!isAffectedNormalDurabilityStack(stack)) {
                return CorrectionResult.unchanged(context);
            }

            int previousDamage = stack.getItemDamage();
            String itemIdentifier = itemIdentifier(stack);
            int stackCount = stack.getCount();
            stack.setItemDamage(0);
            return CorrectionResult.corrected(context, itemIdentifier, stackCount, previousDamage, stack.getItemDamage());
        } catch (RuntimeException e) {
            return CorrectionResult.failed(context, safeItemIdentifier(stack), e.getMessage());
        }
    }

    public CorrectionResult correctAll(Iterable<ItemStack> stacks, CorrectionContext context) {
        context = normalizeContext(context);

        if (stacks == null) {
            return CorrectionResult.batch(context, 0, 0, 0);
        }

        int inspectedStacks = 0;
        int correctedStacks = 0;
        int failedStacks = 0;

        for (ItemStack stack : stacks) {
            inspectedStacks++;
            CorrectionResult result = correct(stack, context);
            if (result.isCorrected()) {
                correctedStacks++;
            } else if (result.isFailed()) {
                failedStacks++;
            }
        }

        return CorrectionResult.batch(context, inspectedStacks, correctedStacks, failedStacks);
    }

    public boolean isAffectedNormalDurabilityStack(ItemStack stack) {
        return stack != null
                && !stack.isEmpty()
                && stack.isItemStackDamageable()
                && stack.getItemDamage() > 0;
    }

    private CorrectionContext normalizeContext(CorrectionContext context) {
        return context == null ? CorrectionContext.ITEM_PICKUP : context;
    }

    private String safeItemIdentifier(ItemStack stack) {
        try {
            return itemIdentifier(stack);
        } catch (RuntimeException e) {
            return "unknown";
        }
    }

    private String itemIdentifier(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return "empty";
        }

        Item item = stack.getItem();
        if (item == null) {
            return "unknown";
        }

        ResourceLocation registryName = item.getRegistryName();
        if (registryName != null) {
            return registryName.toString();
        }

        return item.getClass().getName();
    }
}
