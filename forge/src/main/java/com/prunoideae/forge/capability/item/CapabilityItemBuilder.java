package com.prunoideae.forge.capability.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public abstract class CapabilityItemBuilder extends ItemBuilder {

    @FunctionalInterface
    public interface InitCapabilityCallback {
        ICapabilityProvider apply(ItemStackJS stack, @Nullable CompoundTag nbt);
    }

    public transient InitCapabilityCallback onInitCapabilities;

    public CapabilityItemBuilder(ResourceLocation i) {
        super(i);
    }

    public void initCapabilities(InitCapabilityCallback onInitCapabilities) {
        this.onInitCapabilities = onInitCapabilities;
    }
}
