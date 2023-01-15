package com.prunoideae.powerfuljs.capabilities.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public class CapabilityItem {
    @FunctionalInterface
    public interface InsertItem {
        ItemStack insert(BlockEntity be, int i, ItemStack item, boolean simulate);
    }

    @FunctionalInterface
    public interface ExtractItem {
        ItemStack extract(BlockEntity be, int i, int amount, boolean simulate);
    }

    @FunctionalInterface
    public interface IsItemValid {
        boolean isValid(BlockEntity be, int i, ItemStack arg);
    }

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IItemHandler> {

        private ToIntFunction<BlockEntity> getSlots;
        private BiFunction<BlockEntity, Integer, ItemStack> getStackInSlot;
        private InsertItem insertItem;
        private ExtractItem extractItem;
        private ToIntBiFunction<BlockEntity, Integer> getSlotLimit;
        private IsItemValid isItemValid;

        public BlockEntityBuilder getSlots(ToIntFunction<BlockEntity> getSlots) {
            this.getSlots = getSlots;
            return this;
        }

        public BlockEntityBuilder getStackInSlot(BiFunction<BlockEntity, Integer, ItemStack> getStackInSlot) {
            this.getStackInSlot = getStackInSlot;
            return this;
        }

        public BlockEntityBuilder insertItem(InsertItem insertItem) {
            this.insertItem = insertItem;
            return this;
        }

        public BlockEntityBuilder extractItem(ExtractItem extractItem) {
            this.extractItem = extractItem;
            return this;
        }

        public BlockEntityBuilder getSlotLimit(ToIntBiFunction<BlockEntity, Integer> getSlotLimit) {
            this.getSlotLimit = getSlotLimit;
            return this;
        }

        public BlockEntityBuilder isItemValid(IsItemValid isItemValid) {
            this.isItemValid = isItemValid;
            return this;
        }

        @Override
        public IItemHandler getCapability(BlockEntity instance) {
            return new IItemHandler() {
                @Override
                public int getSlots() {
                    return getSlots != null ? getSlots.applyAsInt(instance) : 0;
                }

                @Override
                public @NotNull ItemStack getStackInSlot(int i) {
                    return getStackInSlot == null ? ItemStack.EMPTY : getStackInSlot.apply(instance, i);
                }

                @Override
                public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
                    return insertItem == null ? ItemStack.EMPTY : insertItem.insert(instance, i, arg, bl);
                }

                @Override
                public @NotNull ItemStack extractItem(int i, int j, boolean bl) {
                    return extractItem == null ? ItemStack.EMPTY : extractItem.extract(instance, i, j, bl);
                }

                @Override
                public int getSlotLimit(int i) {
                    return getSlotLimit == null ? 64 : getSlotLimit.applyAsInt(instance, i);
                }

                @Override
                public boolean isItemValid(int i, @NotNull ItemStack arg) {
                    return isItemValid == null || isItemValid.isValid(instance, i, arg);
                }
            };
        }

        @Override
        public Capability<IItemHandler> getCapabilityKey() {
            return ForgeCapabilities.ITEM_HANDLER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:item_be");
        }
    }
}
