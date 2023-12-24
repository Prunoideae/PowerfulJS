package com.prunoideae.powerfuljs.capabilities.forge.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CapabilityItem {

    public BlockEntityBuilder blockEntity() {
        return blockEntity(27);
    }

    public BlockEntityBuilder blockEntity(int slots) {
        return new BlockEntityBuilder(slots);
    }

    public static class BlockEntityBuilder extends ItemHandlerJS.Builder<BlockEntity> {
        public BlockEntityBuilder(int capacity) {
            super(capacity);
        }
    }


    public ItemStackBuilder itemStack() {
        return itemStack(27);
    }

    public ItemStackBuilder itemStack(int slots) {
        return new ItemStackBuilder(slots);
    }

    public static class ItemStackBuilder extends ItemHandlerJS.Builder<ItemStack> {
        public ItemStackBuilder(int capacity) {
            super(capacity);
        }
    }

    public EntityBuilder entity() {
        return entity(27);
    }

    public EntityBuilder entity(int slots) {
        return new EntityBuilder(slots);
    }

    public static class EntityBuilder extends ItemHandlerJS.Builder<ItemStack> {
        public EntityBuilder(int capacity) {
            super(capacity);
        }
    }
}
