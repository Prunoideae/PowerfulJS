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

    public MultiTankBlockEntityBuilder multiTankBlockEntity() {
        return multiTankBlockEntity(1000);
    }

    public MultiTankBlockEntityBuilder multiTankBlockEntity(int... capacities) {
        return new MultiTankBlockEntityBuilder(capacities);
    }

    public static class BlockEntityBuilder extends FluidHandlerJS.Builder<BlockEntity> {

        public BlockEntityBuilder(int capacity) {
            super(capacity);
        }
    }

    public static class MultiTankBlockEntityBuilder extends MultiHandlerJS.Builder<BlockEntity> {
        public MultiTankBlockEntityBuilder(int... capacities) {
            super(capacities);
        }
    }
}
