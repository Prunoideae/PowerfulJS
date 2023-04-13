package com.prunoideae.powerfuljs.extras.create.behavior;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;

public abstract class CreateBehaviourBuilder<T extends SmartTileEntity, B extends TileEntityBehaviour> {
    @HideFromJS
    public abstract B get(SmartTileEntity blockEntity);

    @FunctionalInterface
    public interface DataHandler {
        void handle(CompoundTag nbt, boolean clientPacket);
    }

    DataHandler read;
    DataHandler write;

    public CreateBehaviourBuilder<T, B> read(DataHandler read) {
        this.read = read;
        return this;
    }

    public CreateBehaviourBuilder<T, B> write(DataHandler write) {
        this.write = write;
        return this;
    }

    public static class Simple extends CreateBehaviourBuilder<SmartTileEntity, TileEntityBehaviour> {
        private static final BehaviourType<?> BEHAVIOUR_TYPE = new BehaviourType<>();
        private int tickrate = 10;
        private Consumer<SmartTileEntity> lazyTick = null;

        public Simple tickrate(int tickrate) {
            this.tickrate = tickrate;
            return this;
        }

        public Simple lazyTick(Consumer<SmartTileEntity> lazyTick) {
            this.lazyTick = lazyTick;
            return this;
        }

        @Override
        public TileEntityBehaviour get(SmartTileEntity blockEntity) {
            return new TileEntityBehaviour(blockEntity) {

                {
                    setLazyTickRate(tickrate);
                }

                @Override
                public void lazyTick() {
                    if (lazyTick != null)
                        lazyTick.accept(blockEntity);
                }

                @Override
                public void read(CompoundTag nbt, boolean clientPacket) {
                    super.read(nbt, clientPacket);
                    if (Simple.this.read != null)
                        Simple.this.read.handle(nbt, clientPacket);
                }

                @Override
                public void write(CompoundTag nbt, boolean clientPacket) {
                    super.write(nbt, clientPacket);
                    if (Simple.this.write != null)
                        Simple.this.write.handle(nbt, clientPacket);
                }

                @Override
                public BehaviourType<?> getType() {
                    return BEHAVIOUR_TYPE;
                }
            };
        }
    }
}
