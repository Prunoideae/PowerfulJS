package com.prunoideae.powerfuljs.capabilities.forge;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;

public abstract class CapabilityBuilderForge<I extends CapabilityProvider<I>, T> extends CapabilityBuilder<I, T, Capability<T>> {
}
