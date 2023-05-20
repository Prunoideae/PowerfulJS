package com.prunoideae.powerfuljs.events.forge;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import com.prunoideae.powerfuljs.forge.CapabilityServiceForge;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class DynamicItemStackEventJS extends DynamicAttachEventJS<ItemStack> {
    @Override
    public DynamicAttachEventJS<ItemStack> add(Predicate<ItemStack> predicate, CapabilityBuilderForge<ItemStack, ?> provider) {
        CapabilityServiceForge.INSTANCE.addItem(predicate, provider);
        return this;
    }
}
