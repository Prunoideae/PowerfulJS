package com.prunoideae.powerfuljs.capabilities.forge.mods.immersive;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.BiConsumer;

public class CapabilityRotationAcceptor {
    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IRotationAcceptor> {
        private BiConsumer<BlockEntity, Double> inputRotation;

        public BlockEntityBuilder inputRotation(BiConsumer<BlockEntity, Double> inputRotation) {
            this.inputRotation = inputRotation;
            return this;
        }

        @Override
        public IRotationAcceptor getCapability(BlockEntity instance) {
            return rotation -> {
                if (inputRotation != null)
                    inputRotation.accept(instance, rotation);
            };
        }

        @Override
        public Capability<IRotationAcceptor> getCapabilityKey() {
            return IRotationAcceptor.CAPABILITY;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:ie_rotation_be_custom");
        }
    }
}
