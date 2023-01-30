package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CapabilityLaser {

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, ILaserReceptor> {
        private Predicate<BlockEntity> canLaserDig;
        private BiConsumer<BlockEntity, FloatingLong> receiveEnergy;

        public BlockEntityBuilder canLaserDig(Predicate<BlockEntity> canLaserDig) {
            this.canLaserDig = canLaserDig;
            return this;
        }

        public BlockEntityBuilder receiveEnergy(BiConsumer<BlockEntity, FloatingLong> receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        @Override
        public ILaserReceptor getCapability(BlockEntity instance) {
            return new ILaserReceptor() {
                @Override
                public void receiveLaserEnergy(@NotNull FloatingLong floatingLong) {
                    if (receiveEnergy != null)
                        receiveEnergy.accept(instance, floatingLong);
                }

                @Override
                public boolean canLasersDig() {
                    return canLaserDig != null && canLaserDig.test(instance);
                }
            };
        }

        @Override
        public Capability<ILaserReceptor> getCapabilityKey() {
            return Capabilities.LASER_RECEPTOR;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mek_laser_be");
        }
    }
}
