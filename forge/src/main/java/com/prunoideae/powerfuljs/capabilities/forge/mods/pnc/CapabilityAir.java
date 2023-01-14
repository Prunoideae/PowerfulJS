package com.prunoideae.powerfuljs.capabilities.forge.mods.pnc;

import com.prunoideae.powerfuljs.capabilities.forge.BuilderForgeCapability;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

public class CapabilityAir {
    public static final String AIR_TAG = "pnc:air";

    public static class AirItemBuilder extends BuilderForgeCapability<ItemStack, IAirHandlerItem> {
        private final int capacity;
        private final float maxPressure;

        public AirItemBuilder(int capacity, float maxPressure) {
            this.capacity = capacity;
            this.maxPressure = maxPressure;
        }

        @Override
        public IAirHandlerItem getCapability(ItemStack instance) {
            return new IAirHandlerItem() {
                @NotNull
                @Override
                public ItemStack getContainer() {
                    return instance;
                }

                @Override
                public float getPressure() {
                    return (float) getVolume() / getAir();
                }

                @Override
                public int getAir() {
                    return instance.getOrCreateTag().getInt(AIR_TAG);
                }

                @Override
                public void addAir(int amount) {
                    instance.getOrCreateTag().putInt(AIR_TAG, getAir() + amount);
                }

                @Override
                public int getBaseVolume() {
                    return capacity;
                }

                @Override
                public void setBaseVolume(int newBaseVolume) {

                }

                @Override
                public int getVolume() {
                    return capacity;
                }

                @Override
                public float maxPressure() {
                    return maxPressure;
                }
            };
        }

        @Override
        public Capability<IAirHandlerItem> getCapabilityKey() {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:air_pnc");
        }
    }

    public static class CustomAirItemBuilder extends BuilderForgeCapability<ItemStack, IAirHandlerItem> {

        @Override
        public IAirHandlerItem getCapability(ItemStack instance) {
            return null;
        }

        @Override
        public Capability<IAirHandlerItem> getCapabilityKey() {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:air_pnc_custom");
        }
    }
}
