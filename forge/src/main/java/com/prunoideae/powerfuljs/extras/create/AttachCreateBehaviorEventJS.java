package com.prunoideae.powerfuljs.extras.create;

import com.prunoideae.powerfuljs.extras.create.behavior.CreateBehaviourBuilder;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AttachCreateBehaviorEventJS extends EventJS {
    public static final Map<BlockEntityType<?>, List<CreateBehaviourBuilder<?, ?>>> REGISTERED_TILE = new HashMap<>();

    public <T extends SmartTileEntity> void attachTo(BlockEntityType<T> smartBlockEntityType, CreateBehaviourBuilder<T, ?> behaviour) {
        REGISTERED_TILE.computeIfAbsent(smartBlockEntityType, k -> new ArrayList<>()).add(behaviour);
    }

    public static List<TileEntityBehaviour> getBehaviors(BlockEntity entity) {
        if (entity instanceof SmartTileEntity smartEntity && REGISTERED_TILE.containsKey(smartEntity.getType())) {
            return REGISTERED_TILE.get(smartEntity.getType()).stream().map(builder -> builder.get(smartEntity)).collect(Collectors.toList());
        }
        return List.of();
    }
}
