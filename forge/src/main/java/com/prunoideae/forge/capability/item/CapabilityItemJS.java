package com.prunoideae.forge.capability.item;

import com.prunoideae.forge.capability.CapabilitiesProvider;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CapabilityItemJS extends BasicItemJS {
    private final List<CapabilityItemBuilder.InitCapabilityCallback> onInitCaps;

    public CapabilityItemJS(Builder p) {
        super(p);
        onInitCaps = p.onInitCapabilities;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        List<ICapabilityProvider> inited = onInitCaps.stream()
                .map(callback -> callback.apply(ItemStackJS.of(stack), nbt))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return inited.isEmpty() ? null : new CapabilitiesProvider(inited);
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
