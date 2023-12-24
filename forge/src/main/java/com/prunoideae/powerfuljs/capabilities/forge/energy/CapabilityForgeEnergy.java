package com.prunoideae.powerfuljs.capabilities.forge.energy;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CapabilityForgeEnergy {
    public ItemStorageBuilder itemStack(int capacity) {
        return new ItemStorageBuilder(capacity);
    }

    public ItemStorageBuilder itemStack() {
        return itemStack(1000);
    }

    public static class ItemStorageBuilder extends EnergyStorageJS.Builder<ItemStack> {
        public ItemStorageBuilder(int capacity) {
            super(capacity);
        }
    }

    public BlockEntityBuilder blockEntity(int capacity) {
        return new BlockEntityBuilder(capacity);
    }

    public BlockEntityBuilder blockEntity() {
        return blockEntity(1000);
    }

    public static class BlockEntityBuilder extends EnergyStorageJS.Builder<BlockEntity> {
        public BlockEntityBuilder(int capacity) {
            super(capacity);
        }
    }


    public EntityBuilder entity(int capacity) {
        return new EntityBuilder(capacity);
    }

    public EntityBuilder entity() {
        return entity(1000);
    }

    public static class EntityBuilder extends EnergyStorageJS.Builder<Entity> {

        public EntityBuilder(int capacity) {
            super(capacity);
        }
    }
}
