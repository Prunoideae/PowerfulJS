package com.prunoideae.powerfuljs;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiPredicate;

public abstract class CapabilityBuilder<I, T, K> {
    protected BiPredicate<I, Direction> isAvailable = (a, b) -> true;

    @HideFromJS
    public abstract T getCapability(I instance);

    @HideFromJS
    public abstract K getCapabilityKey();

    @HideFromJS
    public abstract ResourceLocation getResourceLocation();

    public CapabilityBuilder<I, T, K> availableOn(BiPredicate<I, Direction> isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public CapabilityBuilder<I, T, K> side(Direction direction) {
        this.isAvailable = (a, b) -> b.equals(direction);
        return this;
    }

    @HideFromJS
    public BiPredicate<I, Direction> getDirection() {
        return isAvailable;
    }
}
