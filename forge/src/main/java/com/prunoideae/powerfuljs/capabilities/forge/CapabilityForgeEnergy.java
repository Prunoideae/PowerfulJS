package com.prunoideae.powerfuljs.capabilities.forge;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class CapabilityForgeEnergy {
    @FunctionalInterface
    public interface EnergyIOItemStack {
        int transfer(ItemStack itemStack, int i, boolean simulate);
    }

    @FunctionalInterface
    public interface EnergyIOBlockEntity {
        int transfer(BlockEntity blockEntity, int i, boolean simulate);
    }

    public ItemStorageBuilder normalItemStack(int capacity, boolean canExtract, boolean canReceive) {
        return new ItemStorageBuilder(capacity, canExtract, canReceive);
    }

    public ItemStackBuilder customItemStack() {
        return new ItemStackBuilder();
    }

    public static class ItemStorageBuilder extends CapabilityBuilderForge<ItemStack, IEnergyStorage> {
        private final int capacity;
        private final boolean canExtract;
        private final boolean canReceive;

        private int receiveRate = Integer.MAX_VALUE;
        private int extractRate = Integer.MAX_VALUE;
        private static final String ENERGY_TAG = "pjs:fe_energy";

        public ItemStorageBuilder(int capacity, boolean canExtract, boolean canReceive) {
            this.capacity = capacity;
            this.canExtract = canExtract;
            this.canReceive = canReceive;
        }

        public ItemStorageBuilder receiveRate(int receiveRate) {
            this.receiveRate = receiveRate;
            return this;
        }

        public ItemStorageBuilder extractRate(int extractRate) {
            this.extractRate = extractRate;
            return this;
        }

        @Override
        public IEnergyStorage getCapability(ItemStack instance) {
            return new IEnergyStorage() {
                @Override
                public int receiveEnergy(int i, boolean bl) {
                    if (!canReceive)
                        return 0;
                    int received = Math.min(receiveRate, Math.min(getMaxEnergyStored() - getEnergyStored(), i));

                    if (!bl) {
                        instance.getOrCreateTag().putInt(ENERGY_TAG, received + getEnergyStored());
                    }
                    return received;
                }

                @Override
                public int extractEnergy(int i, boolean bl) {
                    if (!canExtract)
                        return 0;
                    int extracted = Math.min(extractRate, Math.min(getEnergyStored(), i));
                    if (!bl) {
                        instance.getOrCreateTag().putInt(ENERGY_TAG, getEnergyStored() - extracted);
                    }
                    return extracted;
                }

                @Override
                public int getEnergyStored() {
                    return instance.getOrCreateTag().getInt(ENERGY_TAG);
                }

                @Override
                public int getMaxEnergyStored() {
                    return capacity;
                }

                @Override
                public boolean canExtract() {
                    return canExtract;
                }

                @Override
                public boolean canReceive() {
                    return canReceive;
                }
            };
        }

        @Override
        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:fe_item");
        }
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, IEnergyStorage> {
        private EnergyIOItemStack receiveEnergy;
        private EnergyIOItemStack extractEnergy;

        private ToIntFunction<ItemStack> getEnergyStored;
        private ToIntFunction<ItemStack> getMaxEnergyStored;
        private Predicate<ItemStack> canExtract;
        private Predicate<ItemStack> canReceive;

        public ItemStackBuilder getEnergyStored(ToIntFunction<ItemStack> getEnergyStored) {
            this.getEnergyStored = getEnergyStored;
            return this;
        }

        public ItemStackBuilder withCapacity(int capacity) {
            return getMaxEnergyStored(i -> capacity);
        }

        public ItemStackBuilder getMaxEnergyStored(ToIntFunction<ItemStack> getMaxEnergyStored) {
            this.getMaxEnergyStored = getMaxEnergyStored;
            return this;
        }

        public ItemStackBuilder canExtract(Predicate<ItemStack> canExtract) {
            this.canExtract = canExtract;
            return this;
        }

        public ItemStackBuilder canReceive(Predicate<ItemStack> canReceive) {
            this.canReceive = canReceive;
            return this;
        }


        public ItemStackBuilder receiveEnergy(EnergyIOItemStack receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        public ItemStackBuilder extractEnergy(EnergyIOItemStack extractEnergy) {
            this.extractEnergy = extractEnergy;
            return this;
        }

        @HideFromJS
        @Override
        public IEnergyStorage getCapability(ItemStack instance) {
            return new IEnergyStorage() {
                @Override
                public int receiveEnergy(int i, boolean bl) {
                    return receiveEnergy == null ? 0 : receiveEnergy.transfer(instance, i, bl);
                }

                @Override
                public int extractEnergy(int i, boolean bl) {
                    return extractEnergy == null ? 0 : extractEnergy.transfer(instance, i, bl);
                }

                @Override
                public int getEnergyStored() {
                    return getEnergyStored == null ? 0 : getEnergyStored.applyAsInt(instance);
                }

                @Override
                public int getMaxEnergyStored() {
                    return getMaxEnergyStored == null ? 0 : getMaxEnergyStored.applyAsInt(instance);
                }

                @Override
                public boolean canExtract() {
                    return canExtract != null && canExtract.test(instance);
                }

                @Override
                public boolean canReceive() {
                    return canReceive != null && canReceive.test(instance);
                }
            };
        }

        @HideFromJS
        @Override
        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:fe_item_custom");
        }
    }

    // No default implementation for BE caps since we're not likely to do data sync here
    public BlockEntityBuilder customBlockEntity() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IEnergyStorage> {
        private EnergyIOBlockEntity receiveEnergy;
        private EnergyIOBlockEntity extractEnergy;

        private ToIntFunction<BlockEntity> getEnergyStored;
        private ToIntFunction<BlockEntity> getMaxEnergyStored;
        private Predicate<BlockEntity> canExtract;
        private Predicate<BlockEntity> canReceive;

        public BlockEntityBuilder receiveEnergy(EnergyIOBlockEntity receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        public BlockEntityBuilder extractEnergy(EnergyIOBlockEntity extractEnergy) {
            this.extractEnergy = extractEnergy;
            return this;
        }

        public BlockEntityBuilder getEnergyStored(ToIntFunction<BlockEntity> getEnergyStored) {
            this.getEnergyStored = getEnergyStored;
            return this;
        }

        public BlockEntityBuilder withCapacity(int capacity) {
            return getMaxEnergyStored(be -> capacity);
        }

        public BlockEntityBuilder getMaxEnergyStored(ToIntFunction<BlockEntity> getMaxEnergyStored) {
            this.getMaxEnergyStored = getMaxEnergyStored;
            return this;
        }

        public BlockEntityBuilder canExtract(Predicate<BlockEntity> canExtract) {
            this.canExtract = canExtract;
            return this;
        }

        public BlockEntityBuilder canReceive(Predicate<BlockEntity> canReceive) {
            this.canReceive = canReceive;
            return this;
        }

        @Override
        @HideFromJS
        public IEnergyStorage getCapability(BlockEntity instance) {
            return new IEnergyStorage() {
                @Override
                public int receiveEnergy(int i, boolean bl) {
                    return receiveEnergy == null ? 0 : receiveEnergy.transfer(instance, i, bl);
                }

                @Override
                public int extractEnergy(int i, boolean bl) {
                    return extractEnergy == null ? 0 : extractEnergy.transfer(instance, i, bl);
                }

                @Override
                public int getEnergyStored() {
                    return getEnergyStored == null ? 0 : getEnergyStored.applyAsInt(instance);
                }

                @Override
                public int getMaxEnergyStored() {
                    return getMaxEnergyStored == null ? 0 : getMaxEnergyStored.applyAsInt(instance);
                }

                @Override
                public boolean canExtract() {
                    return canExtract != null && canExtract.test(instance);
                }

                @Override
                public boolean canReceive() {
                    return canReceive != null && canReceive.test(instance);
                }
            };
        }

        @Override
        @HideFromJS
        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @Override
        @HideFromJS
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:fe_be_custom");
        }
    }
}
