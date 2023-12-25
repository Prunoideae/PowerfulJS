package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.IEvaporationSolar;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.Predicate;

public class CapabilityEvaporationTower {

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IEvaporationSolar> {
        private Predicate<BlockEntity> canSeeSun;

        public BlockEntityBuilder canSeeSun(Predicate<BlockEntity> canSeeSun) {
            this.canSeeSun = canSeeSun;
            return this;
        }

        @Override
        public IEvaporationSolar getCapability(BlockEntity instance) {
            return () -> canSeeSun != null && canSeeSun.test(instance);
        }

        @Override
        public Capability<IEvaporationSolar> getCapabilityKey() {
            return Capabilities.EVAPORATION_SOLAR;
        }

        @Override
        protected ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerfuljs:mek_solar_be");
        }
    }
}
