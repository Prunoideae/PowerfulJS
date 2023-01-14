package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.BuilderForgeCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

public class CapabilityMana {

    public ManaItemBuilder itemStack() {
        return new ManaItemBuilder();
    }

    public static class ManaItemBuilder extends BuilderForgeCapability<ItemStack, ManaItem> {
        private ToIntFunction<ItemStack> getMana;
        private ToIntFunction<ItemStack> getMaxMana;
        private BiConsumer<ItemStack, Integer> addMana;
        private BiPredicate<ItemStack, BlockEntity> canReceiveManaFromPool;
        private BiPredicate<ItemStack, ItemStack> canReceiveManaFromItem;
        private BiPredicate<ItemStack, BlockEntity> canExportManaToPool;
        private BiPredicate<ItemStack, ItemStack> canExportManaToItem;
        private boolean noExport = false;

        public ManaItemBuilder getMana(ToIntFunction<ItemStack> getMana) {
            this.getMana = getMana;
            return this;
        }

        public ManaItemBuilder getMaxMana(ToIntFunction<ItemStack> getMaxMana) {
            this.getMaxMana = getMaxMana;
            return this;
        }

        public ManaItemBuilder addMana(BiConsumer<ItemStack, Integer> addMana) {
            this.addMana = addMana;
            return this;
        }

        public ManaItemBuilder canReceiveManaFromPool(BiPredicate<ItemStack, BlockEntity> canReceiveManaFromPool) {
            this.canReceiveManaFromPool = canReceiveManaFromPool;
            return this;
        }

        public ManaItemBuilder canReceiveManaFromItem(BiPredicate<ItemStack, ItemStack> canReceiveManaFromItem) {
            this.canReceiveManaFromItem = canReceiveManaFromItem;
            return this;
        }

        public ManaItemBuilder canExportManaToPool(BiPredicate<ItemStack, BlockEntity> canExportManaToPool) {
            this.canExportManaToPool = canExportManaToPool;
            return this;
        }

        public ManaItemBuilder canExportManaToItem(BiPredicate<ItemStack, ItemStack> canExportManaToItem) {
            this.canExportManaToItem = canExportManaToItem;
            return this;
        }

        public ManaItemBuilder setNoExport(boolean noExport) {
            this.noExport = noExport;
            return this;
        }

        @Override
        public ManaItem getCapability(ItemStack instance) {
            return new ManaItem() {
                @Override
                public int getMana() {
                    return getMana == null ? 0 : getMana.applyAsInt(instance);
                }

                @Override
                public int getMaxMana() {
                    return getMaxMana == null ? 0 : getMaxMana.applyAsInt(instance);
                }

                @Override
                public void addMana(int mana) {
                    if (addMana != null)
                        addMana.accept(instance, mana);
                }

                @Override
                public boolean canReceiveManaFromPool(BlockEntity pool) {
                    return canReceiveManaFromPool != null && canReceiveManaFromPool.test(instance, pool);
                }

                @Override
                public boolean canReceiveManaFromItem(ItemStack otherStack) {
                    return canReceiveManaFromItem != null && canReceiveManaFromItem.test(instance, otherStack);
                }

                @Override
                public boolean canExportManaToPool(BlockEntity pool) {
                    return canExportManaToPool != null && canExportManaToPool.test(instance, pool);
                }

                @Override
                public boolean canExportManaToItem(ItemStack otherStack) {
                    return canExportManaToItem != null && canExportManaToItem.test(instance, otherStack);
                }

                @Override
                public boolean isNoExport() {
                    return noExport;
                }
            };
        }

        @Override
        public Capability<ManaItem> getCapabilityKey() {
            return BotaniaForgeCapabilities.MANA_ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mana_item");
        }
    }
}
