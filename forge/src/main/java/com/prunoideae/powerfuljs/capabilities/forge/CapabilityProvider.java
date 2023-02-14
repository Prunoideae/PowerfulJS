package com.prunoideae.powerfuljs.capabilities.forge;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class CapabilityProvider implements ICapabilityProvider {

    public static CapabilityProvider of(Object capability, Object instance, Predicate<Direction> available) {
        return new CapabilityProvider((Capability<?>) capability, LazyOptional.of(() -> instance), available);
    }

    protected final Capability<?> capability;
    protected final LazyOptional<?> instance;
    protected final Predicate<Direction> available;

    public CapabilityProvider(Capability<?> capability, LazyOptional<?> instance, Predicate<Direction> available) {
        this.capability = capability;
        this.instance = instance;
        this.available = available;
    }

    @Override
    public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> capability, @Nullable Direction arg) {
        //Types are wiped as fuck here
        return this.capability == capability && available.test(arg) ? instance.cast() : LazyOptional.empty();
    }
}
