package com.prunoideae.powerfuljs.forge;

import com.mojang.datafixers.util.Pair;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CapabilityServiceForge {
    public static final CapabilityServiceForge INSTANCE = new CapabilityServiceForge();

    protected final List<Pair<Predicate<ItemStack>, CapabilityBuilderForge<ItemStack, ?>>> itemTester = new ArrayList<>();
    protected final List<Pair<Predicate<BlockEntity>, CapabilityBuilderForge<BlockEntity, ?>>> beTester = new ArrayList<>();
    protected final List<Pair<Predicate<Entity>, CapabilityBuilderForge<Entity, ?>>> entityTester = new ArrayList<>();

    public Stream<CapabilityBuilderForge<ItemStack, ?>> getCapabilitiesFor(ItemStack stack) {
        return itemTester.stream().filter(pair -> pair.getFirst().test(stack)).map(Pair::getSecond);
    }

    public Stream<CapabilityBuilderForge<BlockEntity, ?>> getCapabilitiesFor(BlockEntity be) {
        return beTester.stream().filter(pair -> pair.getFirst().test(be)).map(Pair::getSecond);
    }

    public Stream<CapabilityBuilderForge<Entity, ?>> getCapabilitiesFor(Entity entity) {
        return entityTester.stream().filter(pair -> pair.getFirst().test(entity)).map(Pair::getSecond);
    }

    public void addItem(Predicate<ItemStack> predicate, CapabilityBuilderForge<ItemStack, ?> builder) {
        itemTester.add(new Pair<>(predicate, builder));
    }

    public void addBE(Predicate<BlockEntity> predicate, CapabilityBuilderForge<BlockEntity, ?> builder) {
        beTester.add(new Pair<>(predicate, builder));
    }

    public void addEntity(Predicate<Entity> predicate, CapabilityBuilderForge<Entity, ?> builder) {
        entityTester.add(new Pair<>(predicate, builder));
    }
}
