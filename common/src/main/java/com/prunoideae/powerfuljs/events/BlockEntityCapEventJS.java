package com.prunoideae.powerfuljs.events;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import com.prunoideae.powerfuljs.CapabilityService;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityCapEventJS extends EventJS {
    public void attach(BlockEntityType<?> entityType, CapabilityBuilder<BlockEntity, ?, ?> builder) {
        CapabilityService.INSTANCE.addBECapability(entityType, builder);
    }
}
