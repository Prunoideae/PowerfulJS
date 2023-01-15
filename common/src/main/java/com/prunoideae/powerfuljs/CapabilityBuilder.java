package com.prunoideae.powerfuljs;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

public abstract class CapabilityBuilder<I, T, K> {

    @HideFromJS
    public abstract T getCapability(I instance);

    @HideFromJS
    public abstract K getCapabilityKey();

    @HideFromJS
    public abstract ResourceLocation getResourceLocation();
}
