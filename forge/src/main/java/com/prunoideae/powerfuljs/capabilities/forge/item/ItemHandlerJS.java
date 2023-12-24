package com.prunoideae.powerfuljs.capabilities.forge.item;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.block.entity.BlockEntityJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

@RemapPrefixForJS("pjs$")
@SuppressWarnings("UnstableApiUsage")
public class ItemHandlerJS<T extends CapabilityProvider<T>> implements IItemHandler, INBTSerializable<ListTag> {

    @FunctionalInterface
    public interface GetSlots<T extends CapabilityProvider<T>> {
        int get(T instance, ItemHandlerJS<T> storage);
    }

    @FunctionalInterface
    public interface GetStackInSlot<T extends CapabilityProvider<T>> {
        ItemStack get(T instance, ItemHandlerJS<T> storage, int i);
    }

    @FunctionalInterface
    public interface InsertItem<T extends CapabilityProvider<T>> {
        ItemStack insertItem(T instance, ItemHandlerJS<T> storage, int i, @NotNull ItemStack stack, boolean simulate);
    }

    @FunctionalInterface
    public interface ExtractItem<T extends CapabilityProvider<T>> {
        ItemStack extractItem(T instance, ItemHandlerJS<T> storage, int slot, int amount, boolean simulate);
    }

    @FunctionalInterface
    public interface SlotLimit<T extends CapabilityProvider<T>> {
        int get(T instance, ItemHandlerJS<T> storage, int slot);
    }

    @FunctionalInterface
    public interface FilterItem<T extends CapabilityProvider<T>> {
        boolean test(T instance, ItemHandlerJS<T> storage, int i, @NotNull ItemStack stack);
    }


    protected final T instance;
    public NonNullList<ItemStack> stacks;
    private final GetSlots<T> getSlots;
    private final GetStackInSlot<T> getStackInSlot;
    private final InsertItem<T> insertItem;
    private final ExtractItem<T> extractItem;
    private final SlotLimit<T> slotLimit;
    private final FilterItem<T> filterItem;

    public ItemHandlerJS(T instance, int capacity,
                         GetSlots<T> getSlots, GetStackInSlot<T> getStackInSlot,
                         InsertItem<T> insertItem, ExtractItem<T> extractItem,
                         SlotLimit<T> slotLimit, FilterItem<T> filterItem
    ) {
        this.instance = instance;
        stacks = NonNullList.withSize(capacity, ItemStack.EMPTY);

        this.getSlots = getSlots;
        this.getStackInSlot = getStackInSlot;
        this.insertItem = insertItem;
        this.extractItem = extractItem;
        this.slotLimit = slotLimit;
        this.filterItem = filterItem;
    }


    @Override
    @HideFromJS
    public ListTag serializeNBT() {
        ListTag stacksNbt = new ListTag();
        for (ItemStack stack : stacks) {
            CompoundTag stackNbt = new CompoundTag();
            stack.save(stackNbt);
            stacksNbt.add(stackNbt);
        }
        return stacksNbt;
    }

    @Override
    @HideFromJS
    public void deserializeNBT(ListTag arg) {
        for (int i = 0; i < arg.size(); i++) {
            CompoundTag stackNbt = arg.getCompound(i);
            stacks.set(i, ItemStack.of(stackNbt));
        }
    }

    @Override
    public int getSlots() {
        if (getSlots != null) return getSlots.get(instance, this);
        if (instance instanceof BlockEntityJS blockEntityJS && blockEntityJS.inventory != null) {
            return blockEntityJS.inventory.kjs$getSlots();
        }
        return stacks.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        if (getStackInSlot != null) return getStackInSlot.get(instance, this, i);
        if (instance instanceof BlockEntityJS blockEntityJS && blockEntityJS.inventory != null) {
            return blockEntityJS.inventory.kjs$getStackInSlot(i);
        }
        if (i >= stacks.size()) return ItemStack.EMPTY;
        return stacks.get(i);
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
        if (insertItem != null) return insertItem.insertItem(instance, this, i, arg, bl);
        return pjs$insertItemRaw(i, arg, bl);
    }

    @Info("Util method to insert item into the storage. Note that this manipulates the internal `stacks` or the inventory.")
    public ItemStack pjs$insertItemRaw(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (!isItemValid(slot, stack)) return stack;

        if (instance instanceof BlockEntityJS blockEntityJS && blockEntityJS.inventory != null) {
            return blockEntityJS.inventory.kjs$insertItem(slot, stack, simulate);
        }

        ItemStack existing = this.stacks.get(slot);
        int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) return stack;
            limit -= existing.getCount();
        }
        if (limit <= 0) return stack;
        boolean reachedLimit = stack.getCount() > limit;
        if (!simulate) {
            if (existing.isEmpty())
                stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            else
                existing.grow(reachedLimit ? limit : stack.getCount());
        }
        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }


    @Override
    public @NotNull ItemStack extractItem(int i, int j, boolean bl) {
        if (extractItem != null) return extractItem.extractItem(instance, this, i, j, bl);
        return pjs$extractItemRaw(i, j, bl);
    }

    @Info("Util method to extract item into the storage. Note that this manipulates the internal `stacks` or the inventory.")
    public ItemStack pjs$extractItemRaw(int slot, int amount, boolean simulate) {
        if (instance instanceof BlockEntityJS blockEntityJS && blockEntityJS.inventory != null) {
            return blockEntityJS.inventory.kjs$extractItem(slot, amount, simulate);
        }
        if (amount == 0) return ItemStack.EMPTY;
        ItemStack existing = stacks.get(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());
        if (existing.getCount() <= toExtract) {
            if (!simulate) stacks.set(slot, ItemStack.EMPTY);
            return existing.copy();
        } else {
            if (!simulate) existing.shrink(toExtract);
            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int i) {
        if (slotLimit != null) return slotLimit.get(instance, this, i);
        if (instance instanceof BlockEntityJS blockEntityJS && blockEntityJS.inventory != null) {
            return blockEntityJS.inventory.kjs$getSlotLimit(i);
        }
        return 64;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        if (filterItem != null && filterItem.test(instance, this, i, arg)) return true;
        if (instance instanceof BlockEntityJS blockEntityJS && blockEntityJS.inventory != null) {
            return blockEntityJS.inventory.kjs$isItemValid(i, arg);
        }
        return true;
    }

    public static class Builder<T extends CapabilityProvider<T>> extends CapabilityBuilderForge<T, IItemHandler> {

        protected final int capacity;
        protected GetSlots<T> getSlots;
        protected GetStackInSlot<T> getStackInSlot;
        protected ExtractItem<T> extractItem;
        protected InsertItem<T> insertItem;
        protected SlotLimit<T> slotLimit;
        protected FilterItem<T> filterItem;

        public Builder<T> getSlots(GetSlots<T> getSlots) {
            this.getSlots = getSlots;
            return this;
        }

        public Builder<T> getStackInSlot(GetStackInSlot<T> getStackInSlot) {
            this.getStackInSlot = getStackInSlot;
            return this;
        }

        public Builder<T> extractItem(ExtractItem<T> extractItem) {
            this.extractItem = extractItem;
            return this;
        }

        public Builder<T> insertItem(InsertItem<T> insertItem) {
            this.insertItem = insertItem;
            return this;
        }

        public Builder<T> slotLimit(SlotLimit<T> slotLimit) {
            this.slotLimit = slotLimit;
            return this;
        }

        public Builder<T> filterItem(FilterItem<T> filterItem) {
            this.filterItem = filterItem;
            return this;
        }

        public Builder(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public IItemHandler getCapability(T instance) {
            return new ItemHandlerJS<>(
                    instance, capacity,
                    getSlots, getStackInSlot,
                    insertItem, extractItem,
                    slotLimit, filterItem
            );
        }

        @Override
        public Capability<IItemHandler> getCapabilityKey() {
            return ForgeCapabilities.ITEM_HANDLER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerfuljs:item");
        }

        public Builder<T> filterIngredient(Ingredient ingredient) {
            filterItem(((instance1, storage, i, stack) -> ingredient.test(stack)));
            return this;
        }

        public Builder<T> limitAllSlots(int limit) {
            slotLimit((instance1, storage, slot) -> limit);
            return this;
        }

        public Builder<T> noInsert() {
            insertItem((instance1, storage, i, stack, simulate) -> ItemStack.EMPTY);
            return this;
        }

        public Builder<T> noExtract() {
            extractItem((instance1, storage, slot, amount, simulate) -> ItemStack.EMPTY);
            return this;
        }
    }
}
