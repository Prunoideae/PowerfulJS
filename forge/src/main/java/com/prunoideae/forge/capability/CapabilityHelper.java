package com.prunoideae.forge.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class CapabilityHelper {
    public static Capability<IEnergyStorage> ENERGY = CapabilityEnergy.ENERGY;
    public static Capability<IFluidHandlerItem> FLUID_ITEM = CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;

}
