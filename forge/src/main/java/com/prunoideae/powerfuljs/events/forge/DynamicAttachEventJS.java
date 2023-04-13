package com.prunoideae.powerfuljs.events.forge;

import com.mojang.datafixers.util.Pair;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraftforge.common.capabilities.CapabilityProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class DynamicAttachEventJS<T extends CapabilityProvider<T>> extends EventJS {
    private final List<Pair<Predicate<T>, CapabilityBuilderForge<T, ?>>> predicates = new ArrayList<>();

    public DynamicAttachEventJS<T> add(Predicate<T> predicate, CapabilityBuilderForge<T, ?> provider) {
        predicates.add(new Pair<>(predicate, provider));
        return this;
    }

    @HideFromJS
    public List<Pair<Predicate<T>, CapabilityBuilderForge<T, ?>>> getPredicates() {
        return predicates;
    }
}
