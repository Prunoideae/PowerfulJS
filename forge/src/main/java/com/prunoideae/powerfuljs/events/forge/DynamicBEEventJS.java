package com.prunoideae.powerfuljs.events.forge;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import com.prunoideae.powerfuljs.forge.CapabilityServiceForge;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public class DynamicBEEventJS extends DynamicAttachEventJS<BlockEntity> {
    @Override
    public DynamicAttachEventJS<BlockEntity> add(Predicate<BlockEntity> predicate, CapabilityBuilderForge<BlockEntity, ?> provider) {
        CapabilityServiceForge.INSTANCE.addBE(predicate, provider);
        return this;
    }
}
