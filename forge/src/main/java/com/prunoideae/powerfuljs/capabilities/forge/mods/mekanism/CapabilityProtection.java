package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.lasers.ILaserDissipation;
import mekanism.api.radiation.capability.IRadiationShielding;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.ToDoubleFunction;

public class CapabilityProtection {
    public LaserItemStackBuilder itemStackLaserProtection() {
        return new LaserItemStackBuilder();
    }

    public RadiationItemStackBuilder itemStackRadiationProtection() {
        return new RadiationItemStackBuilder();
    }

    public static class LaserItemStackBuilder extends CapabilityBuilderForge<ItemStack, ILaserDissipation> {

        private ToDoubleFunction<ItemStack> getDissipationPercent;
        private ToDoubleFunction<ItemStack> getRefractionPercent;

        public LaserItemStackBuilder getDissipationPercent(ToDoubleFunction<ItemStack> getDissipationPercent) {
            this.getDissipationPercent = getDissipationPercent;
            return this;
        }

        public LaserItemStackBuilder getRefractionPercent(ToDoubleFunction<ItemStack> getRefractionPercent) {
            this.getRefractionPercent = getRefractionPercent;
            return this;
        }

        @Override
        public ILaserDissipation getCapability(ItemStack instance) {
            return new ILaserDissipation() {
                @Override
                public double getDissipationPercent() {
                    return getDissipationPercent == null ? 0 : getDissipationPercent.applyAsDouble(instance);
                }

                @Override
                public double getRefractionPercent() {
                    return getRefractionPercent == null ? 0 : getRefractionPercent.applyAsDouble(instance);
                }
            };
        }

        @Override
        public Capability<ILaserDissipation> getCapabilityKey() {
            return Capabilities.LASER_DISSIPATION;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:laser_protection_item");
        }
    }

    public static class RadiationItemStackBuilder extends CapabilityBuilderForge<ItemStack, IRadiationShielding> {

        private ToDoubleFunction<ItemStack> getRadiationShielding;

        public RadiationItemStackBuilder getRadiationShielding(ToDoubleFunction<ItemStack> getRadiationShielding) {
            this.getRadiationShielding = getRadiationShielding;
            return this;
        }

        @Override
        public IRadiationShielding getCapability(ItemStack instance) {
            return () -> getRadiationShielding == null ? 0 : getRadiationShielding.applyAsDouble(instance);
        }

        @Override
        public Capability<IRadiationShielding> getCapabilityKey() {
            return Capabilities.RADIATION_SHIELDING;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:radiation_protection_item");
        }
    }

}
