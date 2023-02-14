package com.prunoideae.powerfuljs.capabilities.forge;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;

import java.util.function.BiPredicate;

@SuppressWarnings("UnstableApiUsage")
public abstract class CapabilityBuilderForge<I extends CapabilityProvider<I>, T> extends CapabilityBuilder<I, T, Capability<T>> {
}
