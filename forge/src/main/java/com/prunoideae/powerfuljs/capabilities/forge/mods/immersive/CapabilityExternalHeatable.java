package com.prunoideae.powerfuljs.capabilities.forge.mods.immersive;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityExternalHeatable {

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    @FunctionalInterface
    interface DoHeatTick {
        int doHeatTick(BlockEntity blockEntity, int energyAvailable, boolean redstone);
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, ExternalHeaterHandler.IExternalHeatable> {
        DoHeatTick doHeatTick = (a, b, c) -> 0;

        public BlockEntityBuilder doHeatTick(DoHeatTick doHeatTick) {
            this.doHeatTick = doHeatTick;
            return this;
        }

        @Override
        public ExternalHeaterHandler.IExternalHeatable getCapability(BlockEntity instance) {
            return (ExternalHeaterHandler.IExternalHeatable) doHeatTick;
        }

        @Override
        public Capability<ExternalHeaterHandler.IExternalHeatable> getCapabilityKey() {
            return ExternalHeaterHandler.CAPABILITY;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("ie_exheater_be_custom");
        }
    }
}
