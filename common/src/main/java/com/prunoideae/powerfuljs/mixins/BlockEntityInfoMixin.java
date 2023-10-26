package com.prunoideae.powerfuljs.mixins;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import com.prunoideae.powerfuljs.CapabilityService;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockEntityInfo.class, remap = false)
public class BlockEntityInfoMixin {
    public BlockEntityInfo attachCapability(CapabilityBuilder<BlockEntity, ?, ?> builder) {
        BlockEntityInfo thisBuilder = (BlockEntityInfo) (Object) this;
        CapabilityService.INSTANCE.addLazyBECapability(thisBuilder, builder);
        return thisBuilder;
    }
}
