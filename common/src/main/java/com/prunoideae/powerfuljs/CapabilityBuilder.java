package com.prunoideae.powerfuljs;

import net.minecraft.resources.ResourceLocation;

public abstract class CapabilityBuilder<I, T, K> {
    public abstract T getCapability(I instance);

    public abstract K getCapabilityKey();

    public abstract ResourceLocation getResourceLocation();
}
