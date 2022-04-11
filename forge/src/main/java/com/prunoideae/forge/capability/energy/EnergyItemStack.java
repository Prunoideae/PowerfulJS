package com.prunoideae.forge.capability.energy;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyItemStack extends EnergyStorage implements ICapabilityProvider {
    private final ItemStack container;
    public static final String ENERGY_NBT_KEY = "kubejs:forge_energy";

    public EnergyItemStack(ItemStackJS container, int capacity) {
        this(container, capacity, 100);
    }

    public EnergyItemStack(ItemStackJS container, int capacity, int maxTransfer) {
        this(container, capacity, maxTransfer, maxTransfer);
    }

    public EnergyItemStack(ItemStackJS container, int capacity, int maxReceive, int maxExtract) {
        this(container, capacity, maxReceive, maxExtract, 0);
    }

    public EnergyItemStack(ItemStackJS container, int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.container = container.getItemStack();
        this.energy = this.container.getOrCreateTag().get(ENERGY_NBT_KEY) == null ? energy : this.container.getOrCreateTag().getInt(ENERGY_NBT_KEY);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (!simulate)
            container.getOrCreateTag().put(ENERGY_NBT_KEY, serializeNBT());
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate)
            container.getOrCreateTag().put(ENERGY_NBT_KEY, serializeNBT());
        return extracted;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> ((T) this)) : LazyOptional.empty();
    }
}
