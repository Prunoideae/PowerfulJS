package com.prunoideae.forge.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CapabilitiesProvider implements ICapabilityProvider {
    private final List<ICapabilityProvider> providers;

    public CapabilitiesProvider(List<ICapabilityProvider> providers) {
        this.providers = providers;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        for (ICapabilityProvider provider : providers) {
            LazyOptional<T> provided = provider.getCapability(capability, arg);
            if (provided.isPresent())
                return provided;
        }
        return LazyOptional.empty();
    }
}