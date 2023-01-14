package com.prunoideae.powerfuljs;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.swing.text.html.Option;
import java.util.*;

public class CapabilityService {
    public static final CapabilityService INSTANCE = new CapabilityService();
    protected final Map<ItemBuilder, List<CapabilityBuilder<ItemStack, ?, ?>>> itemBuilders = new HashMap<>();
    protected final Map<Item, List<CapabilityBuilder<ItemStack, ?, ?>>> items = new HashMap<>();

    protected final Map<BlockEntityType<?>, List<CapabilityBuilder<BlockEntity, ?, ?>>> blockEntities = new HashMap<>();

    public void addBuilderCapability(ItemBuilder builder, CapabilityBuilder<ItemStack, ?, ?> capabilityBuilder) {
        itemBuilders.computeIfAbsent(builder, b -> new ArrayList<>()).add(capabilityBuilder);
    }

    public void addItemCapability(Item item, CapabilityBuilder<ItemStack, ?, ?> capabilityBuilder) {
        items.computeIfAbsent(item, i -> new ArrayList<>()).add(capabilityBuilder);
    }

    public void addBECapability(BlockEntityType<?> type, CapabilityBuilder<BlockEntity, ?, ?> capabilityBuilder) {
        blockEntities.computeIfAbsent(type, b -> new ArrayList<>()).add(capabilityBuilder);
    }

    public void loadBuilders() {
        for (Map.Entry<ItemBuilder, List<CapabilityBuilder<ItemStack, ?, ?>>> entry : itemBuilders.entrySet()) {
            ItemBuilder builder = entry.getKey();
            List<CapabilityBuilder<ItemStack, ?, ?>> capabilityBuilder = entry.getValue();
            items.computeIfAbsent(builder.get(), i -> new ArrayList<>()).addAll(capabilityBuilder);
        }
    }

    public Optional<List<CapabilityBuilder<ItemStack, ?, ?>>> getCapabilitiesFor(ItemStack itemStack) {
        return Optional.ofNullable(items.get(itemStack.getItem()));
    }

    public Optional<List<CapabilityBuilder<BlockEntity, ?, ?>>> getCapabilitiesFor(BlockEntity blockEntity) {
        return Optional.ofNullable(blockEntities.get(blockEntity.getType()));
    }
}
