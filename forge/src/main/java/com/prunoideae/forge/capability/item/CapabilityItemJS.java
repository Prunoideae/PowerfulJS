package com.prunoideae.forge.capability.item;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class CapabilityItemJS extends BasicItemJS {
    private final CapabilityItemBuilder.InitCapabilityCallback onInitCaps;

    public CapabilityItemJS(Builder p) {
        super(p);
        onInitCaps = p.onInitCapabilities;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return onInitCaps != null ? onInitCaps.apply(ItemStackJS.of(stack), nbt) : null;
    }

    public static class Builder extends CapabilityItemBuilder {

        public Builder(ResourceLocation i) {
            super(i);
        }

        @Override
        public Item createObject() {
            return new CapabilityItemJS(this);
        }
    }
}
