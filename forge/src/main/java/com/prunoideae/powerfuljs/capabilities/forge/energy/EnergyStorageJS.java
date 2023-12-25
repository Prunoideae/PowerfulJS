package com.prunoideae.powerfuljs.capabilities.forge.energy;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

@RemapPrefixForJS("pjs$")
@SuppressWarnings("UnstableApiUsage")
public class EnergyStorageJS<T extends CapabilityProvider<T>> implements IEnergyStorage, INBTSerializable<IntTag> {

    @FunctionalInterface
    public interface EnergyIO<T extends CapabilityProvider<T>> {
        int transfer(T instance, EnergyStorageJS<T> storage, int i, boolean simulate);
    }

    @FunctionalInterface
    public interface GetEnergyStat<T extends CapabilityProvider<T>> {
        int get(T instance, EnergyStorageJS<T> storage);
    }

    protected final T instance;
    public int energy;
    public final int capacity;
    private final EnergyIO<T> receiveEnergy;
    private final EnergyIO<T> extractEnergy;
    private final GetEnergyStat<T> getEnergyStored;
    private final GetEnergyStat<T> getMaxEnergyStored;
    private final boolean canExtract;
    private final boolean canReceive;


    public EnergyStorageJS(T instance, int capacity,
                           EnergyIO<T> receiveEnergy, EnergyIO<T> extractEnergy,
                           GetEnergyStat<T> getEnergyStored, GetEnergyStat<T> getMaxEnergyStored,
                           boolean canExtract, boolean canReceive) {
        this.instance = instance;

        this.capacity = capacity;
        this.receiveEnergy = receiveEnergy;
        this.extractEnergy = extractEnergy;
        this.getEnergyStored = getEnergyStored;
        this.getMaxEnergyStored = getMaxEnergyStored;
        this.canExtract = canExtract;
        this.canReceive = canReceive;
    }

    @Override
    @HideFromJS
    public IntTag serializeNBT() {
        return IntTag.valueOf(this.energy);
    }

    @Override
    @HideFromJS
    public void deserializeNBT(IntTag arg) {
        this.energy = arg.getAsInt();
    }

    @Override
    public int receiveEnergy(int i, boolean bl) {
        if (receiveEnergy != null) {
            return this.receiveEnergy.transfer(this.instance, this, i, bl);
        }
        return pjs$receiveEnergyRaw(i, bl);
    }

    @Info("Util method to add some energy from the storage. Note that this manipulates the internal `energy`.")
    public int pjs$receiveEnergyRaw(int i, boolean bl) {
        if (!canReceive())
            return 0;

        int received = Math.min(getMaxEnergyStored() - getEnergyStored(), i);
        if (!bl) energy += received;
        return received;
    }

    @Override
    public int extractEnergy(int i, boolean bl) {
        if (extractEnergy != null) {
            return this.extractEnergy.transfer(this.instance, this, i, bl);
        }
        return pjs$extractEnergyRaw(i, bl);
    }

    @Info("Util method to extract some energy from the storage. Note that this manipulates the internal `energy`.")
    public int pjs$extractEnergyRaw(int i, boolean bl) {
        if (!canReceive())
            return 0;
        int extracted = Math.min(energy, i);
        if (!bl) energy -= extracted;
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        if (getEnergyStored != null) {
            return this.getEnergyStored.get(this.instance, this);
        }
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        if (getMaxEnergyStored != null) {
            return this.getMaxEnergyStored.get(this.instance, this);
        }
        return capacity;
    }

    @Override
    @HideFromJS
    public boolean canExtract() {
        return canExtract;
    }

    @Override
    @HideFromJS
    public boolean canReceive() {
        return canReceive;
    }

    public static class Builder<T extends CapabilityProvider<T>> extends CapabilityBuilderForge<T, IEnergyStorage> {

        protected final int capacity;
        protected EnergyIO<T> receiveEnergy;
        protected EnergyIO<T> extractEnergy;
        protected GetEnergyStat<T> getEnergyStored;
        protected GetEnergyStat<T> getMaxEnergyStored;

        public Builder(int capacity) {
            this.capacity = capacity;
        }

        public Builder<T> receiveEnergy(EnergyIO<T> receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        public Builder<T> extractEnergy(EnergyIO<T> extractEnergy) {
            this.extractEnergy = extractEnergy;
            return this;
        }

        public Builder<T> dynamicEnergy(GetEnergyStat<T> getEnergyStored) {
            this.getEnergyStored = getEnergyStored;
            return this;
        }

        public Builder<T> dynamicCapacity(GetEnergyStat<T> getMaxEnergyStored) {
            this.getMaxEnergyStored = getMaxEnergyStored;
            return this;
        }

        protected boolean canExtract = true;
        protected boolean canReceive = true;

        public Builder<T> canExtract(boolean canExtract) {
            this.canExtract = canExtract;
            return this;
        }

        public Builder<T> canReceive(boolean canReceive) {
            this.canReceive = canReceive;
            return this;
        }

        @Override
        public IEnergyStorage getCapability(T instance) {
            return new EnergyStorageJS<>(instance, capacity,
                    receiveEnergy, extractEnergy,
                    getEnergyStored, getMaxEnergyStored,
                    canExtract, canReceive
            );
        }

        @Override
        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerfuljs:forge_energy");
        }
    }
}
