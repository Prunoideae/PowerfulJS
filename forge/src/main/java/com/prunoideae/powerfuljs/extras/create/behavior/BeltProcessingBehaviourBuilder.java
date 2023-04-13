package com.prunoideae.powerfuljs.extras.create.behavior;

import com.simibubi.create.content.contraptions.relays.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.belt.BeltProcessingBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.belt.TransportedItemStackHandlerBehaviour;
import net.minecraft.nbt.CompoundTag;

public class BeltProcessingBehaviourBuilder extends CreateBehaviourBuilder<SmartTileEntity, BeltProcessingBehaviour> {

    @FunctionalInterface
    public interface BeltProcessingCallback {
        BeltProcessingBehaviour.ProcessingResult onTrigger(SmartTileEntity tileEntity, TransportedItemStack stack, TransportedItemStackHandlerBehaviour behaviour);
    }

    private BeltProcessingCallback onItemEnter = (t, i, b) -> BeltProcessingBehaviour.ProcessingResult.PASS;
    private BeltProcessingCallback continueProcessing = (t, i, b) -> BeltProcessingBehaviour.ProcessingResult.PASS;

    public BeltProcessingBehaviourBuilder onItemEnter(BeltProcessingCallback onItemEnter) {
        this.onItemEnter = onItemEnter;
        return this;
    }

    public BeltProcessingBehaviourBuilder continueProcessing(BeltProcessingCallback continueProcessing) {
        this.continueProcessing = continueProcessing;
        return this;
    }

    @Override
    public BeltProcessingBehaviour get(SmartTileEntity blockEntity) {
        return new BeltProcessingBehaviour(blockEntity) {
            @Override
            public void read(CompoundTag nbt, boolean clientPacket) {
                super.read(nbt, clientPacket);
                if (BeltProcessingBehaviourBuilder.this.read != null)
                    BeltProcessingBehaviourBuilder.this.read.handle(nbt, clientPacket);
            }

            @Override
            public void write(CompoundTag nbt, boolean clientPacket) {
                super.write(nbt, clientPacket);
                if (BeltProcessingBehaviourBuilder.this.write != null)
                    BeltProcessingBehaviourBuilder.this.write.handle(nbt, clientPacket);
            }
        }
                .whenItemEnters((i, b) -> BeltProcessingBehaviourBuilder.this.onItemEnter.onTrigger(blockEntity, i, b))
                .whileItemHeld((i, b) -> BeltProcessingBehaviourBuilder.this.continueProcessing.onTrigger(blockEntity, i, b));
    }
}
