package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.BuilderForgeCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.ExoflameHeatable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class CapabilityExoflame {

    public BuilderBlockEntity tileEntity() {
        return new BuilderBlockEntity();
    }

    public static class BuilderBlockEntity extends BuilderForgeCapability<BlockEntity, ExoflameHeatable> {

        private Predicate<BlockEntity> canSmelt;
        private ToIntFunction<BlockEntity> getBurnTime;
        private Consumer<BlockEntity> boostBurnTime;
        private Consumer<BlockEntity> boostSpeed;

        public BuilderBlockEntity canSmelt(Predicate<BlockEntity> canSmelt) {
            this.canSmelt = canSmelt;
            return this;
        }

        public BuilderBlockEntity getBurnTime(ToIntFunction<BlockEntity> getBurnTime) {
            this.getBurnTime = getBurnTime;
            return this;
        }

        public BuilderBlockEntity boostBurnTime(Consumer<BlockEntity> boostBurnTime) {
            this.boostBurnTime = boostBurnTime;
            return this;
        }

        public BuilderBlockEntity boostSpeed(Consumer<BlockEntity> boostSpeed) {
            this.boostSpeed = boostSpeed;
            return this;
        }

        @Override
        public ExoflameHeatable getCapability(BlockEntity instance) {
            return new ExoflameHeatable() {
                @Override
                public boolean canSmelt() {
                    return canSmelt.test(instance);
                }

                @Override
                public int getBurnTime() {
                    return getBurnTime.applyAsInt(instance);
                }

                @Override
                public void boostBurnTime() {
                    if (boostBurnTime != null)
                        boostBurnTime.accept(instance);
                }

                @Override
                public void boostCookTime() {
                    if (boostSpeed != null)
                        boostSpeed.accept(instance);
                }
            };
        }

        @Override
        public Capability<ExoflameHeatable> getCapabilityKey() {
            return BotaniaForgeCapabilities.EXOFLAME_HEATABLE;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:exoflame_be");
        }
    }
}
