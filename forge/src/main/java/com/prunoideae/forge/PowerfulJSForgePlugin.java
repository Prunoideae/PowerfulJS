package com.prunoideae.forge;

import com.prunoideae.PowerfulJSPlugin;
import com.prunoideae.forge.capability.energy.EnergyItemStack;
import com.prunoideae.forge.capability.item.CapabilityItemJS;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class PowerfulJSForgePlugin extends PowerfulJSPlugin {
    @Override
    public void init() {
        super.init();
        RegistryObjectBuilderTypes.ITEM.addType("capability_basic", CapabilityItemJS.Builder.class, CapabilityItemJS.Builder::new);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        super.addBindings(event);
        event.add("EnergyItemStack", EnergyItemStack.class);
    }
}
