package com.prunoideae.powerfuljs.capabilities.forge;

import com.prunoideae.powerfuljs.capabilities.forge.energy.CapabilityForgeEnergy;
import com.prunoideae.powerfuljs.capabilities.forge.fluid.CapabilityFluid;
import com.prunoideae.powerfuljs.capabilities.forge.item.CapabilityItem;

public interface CapabilitiesForge {
    CapabilityForgeEnergy ENERGY = new CapabilityForgeEnergy();
    CapabilityFluid FLUID = new CapabilityFluid();
    CapabilityItem ITEM = new CapabilityItem();
}
