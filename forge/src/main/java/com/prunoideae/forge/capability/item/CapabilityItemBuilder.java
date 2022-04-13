package com.prunoideae.forge.capability.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class CapabilityItemBuilder extends ItemBuilder {

    @FunctionalInterface
    public interface InitCapabilityCallback {
        ICapabilityProvider apply(ItemStackJS stack, @Nullable CompoundTag nbt);
    }

    public transient List<InitCapabilityCallback> onInitCapabilities = new ArrayList<>();

    public CapabilityItemBuilder(ResourceLocation i) {
        super(i);
    }

    public CapabilityItemBuilder addCapability(InitCapabilityCallback onInitCapabilities) {
        this.onInitCapabilities.add(onInitCapabilities);
        return this;
    }
}
