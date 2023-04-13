package com.prunoideae.powerfuljs.extras.create;

import com.prunoideae.powerfuljs.PowerfulJS;
import com.simibubi.create.api.event.TileEntityBehaviourEvent;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;

public class CreateEvents {
    public static void modifyBehaviour(TileEntityBehaviourEvent<?> event) {
        SmartTileEntity blockEntity = event.getTileEntity();
        for (TileEntityBehaviour behavior : AttachCreateBehaviorEventJS.getBehaviors(blockEntity)) {
            event.attach(behavior);
        }
    }
}
