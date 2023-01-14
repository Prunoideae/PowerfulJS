package com.prunoideae.powerfuljs.capabilities.forge;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityProvider implements ICapabilityProvider {

    public static CapabilityProvider of(Object capability, Object instance) {
        return new CapabilityProvider((Capability<?>) capability, LazyOptional.of(() -> instance));
    }

    protected final Capability<?> capability;
    protected final LazyOptional<?> instance;

    public CapabilityProvider(Capability<?> capability, LazyOptional<?> instance) {
        this.capability = capability;
        this.instance = instance;
    }

    @Override
    public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> capability, @Nullable Direction arg) {
        //Types are wiped as fuck here
        return this.capability == capability ? instance.cast() : LazyOptional.empty();
    }
}
