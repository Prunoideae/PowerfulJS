package com.prunoideae.powerfuljs.capabilities.forge;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;

import java.util.function.BiPredicate;

@SuppressWarnings("UnstableApiUsage")
public abstract class CapabilityBuilderForge<I extends CapabilityProvider<I>, T> extends CapabilityBuilder<I, T, Capability<T>> {

    @Info("Set the key of the capability, if you have multiple capabilities attached (e.g. an input item and an output item), they must not be conflicted.")
    public CapabilityBuilderForge<I, T> key(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public CapabilityBuilderForge() {
        this.resourceLocation = getDefaultKey();
    }

    protected ResourceLocation resourceLocation;

    @Override
    public final ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    protected abstract ResourceLocation getDefaultKey();

}
