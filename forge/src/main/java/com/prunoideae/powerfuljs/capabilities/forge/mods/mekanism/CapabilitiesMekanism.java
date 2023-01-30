package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical.CapabilityGas;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical.CapabilityInfusion;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical.CapabilityPigment;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical.CapabilitySlurry;

public interface CapabilitiesMekanism {
    CapabilityGas GAS = new CapabilityGas();
    CapabilityInfusion INFUSION = new CapabilityInfusion();
    CapabilityPigment PIGMENT = new CapabilityPigment();
    CapabilitySlurry SLURRY = new CapabilitySlurry();
    CapabilityProtection PROTECTION = new CapabilityProtection();
    CapabilityAlloyInteractable ALLOY_INTERACTABLE = new CapabilityAlloyInteractable();
    CapabilityConfigurable CONFIGURABLE = new CapabilityConfigurable();
    CapabilityEvaporationTower EVAPORATION_TOWER = new CapabilityEvaporationTower();
    CapabilityLaser LASER = new CapabilityLaser();
}
