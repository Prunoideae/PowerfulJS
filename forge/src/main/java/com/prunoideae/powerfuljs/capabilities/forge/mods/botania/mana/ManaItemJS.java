package com.prunoideae.powerfuljs.capabilities.forge.mods.botania.mana;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;

public class ManaItemJS implements ManaItem, INBTSerializable<IntTag> {

    @FunctionalInterface
    public interface GetManaStats {
        int get(ItemStack instance, ManaItemJS storage);
    }

    @FunctionalInterface
    public interface AddMana {
        void addMana(ItemStack instance, ManaItemJS storage, int mana);
    }

    @FunctionalInterface
    public interface CanReceiveManaFrom<T> {
        boolean test(ItemStack instance, ManaItemJS storage, T from);
    }

    @FunctionalInterface
    public interface CanExportManaTo<T> {
        boolean test(ItemStack instance, ManaItemJS storage, T to);
    }

    @FunctionalInterface
    public interface CanExport {
        boolean test(ItemStack instance, ManaItemJS storage);
    }

    private final ItemStack instance;
    public int mana;
    private final int capacity;
    private final GetManaStats getMana;
    private final GetManaStats getMaxMana;
    private final AddMana addMana;
    private final CanReceiveManaFrom<BlockEntity> canReceiveManaFromPool;
    private final CanReceiveManaFrom<ItemStack> canReceiveManaFromItem;
    private final CanExportManaTo<BlockEntity> canExportManaToPool;
    private final CanExportManaTo<ItemStack> canExportManaToItem;
    private final CanExport canExport;

    public ManaItemJS(
            ItemStack instance, int capacity,
            GetManaStats getMana, GetManaStats getMaxMana,
            AddMana addMana,
            CanReceiveManaFrom<BlockEntity> canReceiveManaFromPool, CanReceiveManaFrom<ItemStack> canReceiveManaFromItem,
            CanExportManaTo<BlockEntity> canExportManaToPool, CanExportManaTo<ItemStack> canExportManaToItem,
            CanExport canExport
    ) {
        this.instance = instance;
        this.capacity = capacity;

        this.addMana = addMana;
        this.getMana = getMana;
        this.getMaxMana = getMaxMana;
        this.canReceiveManaFromPool = canReceiveManaFromPool;
        this.canReceiveManaFromItem = canReceiveManaFromItem;
        this.canExportManaToPool = canExportManaToPool;
        this.canExportManaToItem = canExportManaToItem;
        this.canExport = canExport;
    }

    @Override
    public IntTag serializeNBT() {
        return IntTag.valueOf(mana);
    }

    @Override
    public void deserializeNBT(IntTag arg) {
        mana = arg.getAsInt();
    }

    @Override
    public int getMana() {
        if (getMana != null) return getMana.get(instance, this);
        return mana;
    }

    @Override
    public int getMaxMana() {
        if (getMaxMana != null) return getMaxMana.get(instance, this);
        return capacity;
    }

    @Override
    public void addMana(int mana) {
        if (addMana != null) addMana.addMana(instance, this, mana);
        else this.mana += mana;
    }

    @Override
    public boolean canReceiveManaFromPool(BlockEntity pool) {
        return canReceiveManaFromPool == null || canReceiveManaFromPool.test(instance, this, pool);
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack otherStack) {
        return canReceiveManaFromItem == null || canReceiveManaFromItem.test(instance, this, otherStack);
    }

    @Override
    public boolean canExportManaToPool(BlockEntity pool) {
        return canExportManaToPool == null || canExportManaToPool.test(instance, this, pool);
    }

    @Override
    public boolean canExportManaToItem(ItemStack otherStack) {
        return canExportManaToItem == null || canExportManaToItem.test(instance, this, otherStack);
    }

    @Override
    public boolean isNoExport() {
        return canExport != null && canExport.test(instance, this);
    }

    public static class Builder extends CapabilityBuilderForge<ItemStack, ManaItem> {

        private final int capacity;
        private GetManaStats getMana;
        private GetManaStats getMaxMana;
        private AddMana addMana;
        private CanReceiveManaFrom<BlockEntity> canReceiveManaFromPool;
        private CanReceiveManaFrom<ItemStack> canReceiveManaFromItem;
        private CanExportManaTo<BlockEntity> canExportManaToPool;
        private CanExportManaTo<ItemStack> canExportManaToItem;
        private CanExport canExport;

        public Builder getMana(GetManaStats getMana) {
            this.getMana = getMana;
            return this;
        }

        public Builder getMaxMana(GetManaStats getMaxMana) {
            this.getMaxMana = getMaxMana;
            return this;
        }

        public Builder addMana(AddMana addMana) {
            this.addMana = addMana;
            return this;
        }

        public Builder canReceiveManaFromPool(CanReceiveManaFrom<BlockEntity> canReceiveManaFromPool) {
            this.canReceiveManaFromPool = canReceiveManaFromPool;
            return this;
        }

        public Builder canReceiveManaFromItem(CanReceiveManaFrom<ItemStack> canReceiveManaFromItem) {
            this.canReceiveManaFromItem = canReceiveManaFromItem;
            return this;
        }

        public Builder canExportManaToPool(CanExportManaTo<BlockEntity> canExportManaToPool) {
            this.canExportManaToPool = canExportManaToPool;
            return this;
        }

        public Builder canExportManaToItem(CanExportManaTo<ItemStack> canExportManaToItem) {
            this.canExportManaToItem = canExportManaToItem;
            return this;
        }

        public Builder canExport(CanExport canExport) {
            this.canExport = canExport;
            return this;
        }

        public Builder(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public ManaItem getCapability(ItemStack instance) {
            return null;
        }

        @Override
        public Capability<ManaItem> getCapabilityKey() {
            return BotaniaForgeCapabilities.MANA_ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerfuljs:mana");
        }

        public Builder noReceiveManaFromPool() {
            canReceiveManaFromPool((instance1, storage, from) -> false);
            return this;
        }

        public Builder noReceiveManaFromItem() {
            canReceiveManaFromItem((instance1, storage, from) -> false);
            return this;
        }

        public Builder noExportManaToPool() {
            canExportManaToPool((instance1, storage, to) -> false);
            return this;
        }

        public Builder noExportManaToItem() {
            canExportManaToItem((instance1, storage, to) -> false);
            return this;
        }

        public Builder noExport() {
            canExport((instance1, storage) -> false);
            return this;
        }
    }
}
