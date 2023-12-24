package com.prunoideae.powerfuljs.capabilities.forge.fluid;

import net.minecraft.world.level.block.entity.BlockEntity;

public class CapabilityFluid {

    public FluidHandlerItemJS.Builder itemStack() {
        return itemStack(1000);
    }

    public FluidHandlerItemJS.Builder itemStack(int capacity) {
        return new FluidHandlerItemJS.Builder(capacity);
    }


    public BlockEntityBuilder blockEntity() {
        return blockEntity(1000);
    }

    public BlockEntityBuilder blockEntity(int capacity) {
        return new BlockEntityBuilder(capacity);
    }

    public static class BlockEntityBuilder extends FluidHandlerJS.Builder<BlockEntity> {

        public BlockEntityBuilder(int capacity) {
            super(capacity);
        }
    }

}
