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
public abstract class DynamicAttachEventJS<T extends CapabilityProvider<T>> extends EventJS {

    public abstract DynamicAttachEventJS<T> add(Predicate<T> predicate, CapabilityBuilderForge<T, ?> provider);
}
