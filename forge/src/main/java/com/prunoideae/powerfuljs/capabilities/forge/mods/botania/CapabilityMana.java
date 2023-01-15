package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaReceiver;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class CapabilityMana {

    public ItemStackBuilder itemStack() {
        return new ItemStackBuilder();
    }

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, ManaItem> {
        private ToIntFunction<ItemStack> getMana;
        private ToIntFunction<ItemStack> getMaxMana;
        private BiConsumer<ItemStack, Integer> addMana;
        private BiPredicate<ItemStack, BlockEntity> canReceiveManaFromPool;
        private BiPredicate<ItemStack, ItemStack> canReceiveManaFromItem;
        private BiPredicate<ItemStack, BlockEntity> canExportManaToPool;
        private BiPredicate<ItemStack, ItemStack> canExportManaToItem;
        private boolean noExport = false;

        public ItemStackBuilder getMana(ToIntFunction<ItemStack> getMana) {
            this.getMana = getMana;
            return this;
        }

        public ItemStackBuilder getMaxMana(ToIntFunction<ItemStack> getMaxMana) {
            this.getMaxMana = getMaxMana;
            return this;
        }

        public ItemStackBuilder addMana(BiConsumer<ItemStack, Integer> addMana) {
            this.addMana = addMana;
            return this;
        }

        public ItemStackBuilder canReceiveManaFromPool(BiPredicate<ItemStack, BlockEntity> canReceiveManaFromPool) {
            this.canReceiveManaFromPool = canReceiveManaFromPool;
            return this;
        }

        public ItemStackBuilder canReceiveManaFromItem(BiPredicate<ItemStack, ItemStack> canReceiveManaFromItem) {
            this.canReceiveManaFromItem = canReceiveManaFromItem;
            return this;
        }

        public ItemStackBuilder canExportManaToPool(BiPredicate<ItemStack, BlockEntity> canExportManaToPool) {
            this.canExportManaToPool = canExportManaToPool;
            return this;
        }

        public ItemStackBuilder canExportManaToItem(BiPredicate<ItemStack, ItemStack> canExportManaToItem) {
            this.canExportManaToItem = canExportManaToItem;
            return this;
        }

        public ItemStackBuilder setNoExport(boolean noExport) {
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

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, ManaReceiver> {

        private ToIntFunction<BlockEntity> getCurrentMana;
        private Predicate<BlockEntity> isFull;
        private BiConsumer<BlockEntity, Integer> receiveMana;
        private Predicate<BlockEntity> canReceiveManaFromBurst;

        public BlockEntityBuilder getCurrentMana(ToIntFunction<BlockEntity> getCurrentMana) {
            this.getCurrentMana = getCurrentMana;
            return this;
        }

        public BlockEntityBuilder isFull(Predicate<BlockEntity> isFull) {
            this.isFull = isFull;
            return this;
        }

        public BlockEntityBuilder receiveMana(BiConsumer<BlockEntity, Integer> receiveMana) {
            this.receiveMana = receiveMana;
            return this;
        }

        public BlockEntityBuilder canReceiveManaFromBurst(Predicate<BlockEntity> canReceiveManaFromBurst) {
            this.canReceiveManaFromBurst = canReceiveManaFromBurst;
            return this;
        }

        @Override
        public ManaReceiver getCapability(BlockEntity instance) {
            return new ManaReceiver() {
                @Override
                public Level getManaReceiverLevel() {
                    return instance.getLevel();
                }

                @Override
                public BlockPos getManaReceiverPos() {
                    return instance.getBlockPos();
                }

                @Override
                public int getCurrentMana() {
                    return getCurrentMana == null ? 0 : getCurrentMana.applyAsInt(instance);
                }

                @Override
                public boolean isFull() {
                    return isFull != null && isFull.test(instance);
                }

                @Override
                public void receiveMana(int mana) {
                    if (receiveMana != null)
                        receiveMana.accept(instance, mana);
                }

                @Override
                public boolean canReceiveManaFromBursts() {
                    return canReceiveManaFromBurst == null ? !isFull() : canReceiveManaFromBurst.test(instance);
                }
            };
        }

        @Override
        public Capability<ManaReceiver> getCapabilityKey() {
            return BotaniaForgeCapabilities.MANA_RECEIVER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mana_be");
        }
    }
}
