package com.prunoideae.powerfuljs.capabilities.forge.mods.pnc;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class CapabilityAir {
    public static final String AIR_TAG = "pnc:air";

    public ItemStackBuilder itemStack(int capacity, float maxPressure) {
        return new ItemStackBuilder(capacity, maxPressure);
    }

    public ItemStackBuilderCustom itemStackCustom() {
        return new ItemStackBuilderCustom();
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, IAirHandlerItem> {
        private final int capacity;
        private final float maxPressure;

        public ItemStackBuilder(int capacity, float maxPressure) {
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
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:air_pnc");
        }
    }

    public static class ItemStackBuilderCustom extends CapabilityBuilderForge<ItemStack, IAirHandlerItem> {

        private Function<ItemStack, Float> getPressure;
        private ToIntFunction<ItemStack> getAir;
        private BiConsumer<ItemStack, Integer> addAir;
        private ToIntFunction<ItemStack> getBaseVolume;
        private BiConsumer<ItemStack, Integer> setBaseVolume;
        private ToIntFunction<ItemStack> getVolume;
        private Function<ItemStack, Float> maxPressure;

        public ItemStackBuilderCustom getPressure(Function<ItemStack, Float> getPressure) {
            this.getPressure = getPressure;
            return this;
        }

        public ItemStackBuilderCustom getAir(ToIntFunction<ItemStack> getAir) {
            this.getAir = getAir;
            return this;
        }

        public ItemStackBuilderCustom addAir(BiConsumer<ItemStack, Integer> addAir) {
            this.addAir = addAir;
            return this;
        }

        public ItemStackBuilderCustom getBaseVolume(ToIntFunction<ItemStack> getBaseVolume) {
            this.getBaseVolume = getBaseVolume;
            return this;
        }

        public ItemStackBuilderCustom setBaseVolume(BiConsumer<ItemStack, Integer> setBaseVolume) {
            this.setBaseVolume = setBaseVolume;
            return this;
        }

        public ItemStackBuilderCustom getVolume(ToIntFunction<ItemStack> getVolume) {
            this.getVolume = getVolume;
            return this;
        }

        public ItemStackBuilderCustom maxPressure(Function<ItemStack, Float> maxPressure) {
            this.maxPressure = maxPressure;
            return this;
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
                    return getPressure == null ? 0 : getPressure.apply(instance);
                }

                @Override
                public int getAir() {
                    return getAir == null ? 0 : getAir.applyAsInt(instance);
                }

                @Override
                public void addAir(int amount) {
                    if (addAir != null)
                        addAir.accept(instance, amount);
                }

                @Override
                public int getBaseVolume() {
                    return getBaseVolume == null ? 1 : getBaseVolume.applyAsInt(instance);
                }

                @Override
                public void setBaseVolume(int newBaseVolume) {
                    if (setBaseVolume != null)
                        setBaseVolume.accept(instance, newBaseVolume);
                }

                @Override
                public int getVolume() {
                    return getVolume == null ? 1 : getVolume.applyAsInt(instance);
                }

                @Override
                public float maxPressure() {
                    return maxPressure == null ? 1 : maxPressure.apply(instance);
                }
            };
        }

        @Override
        public Capability<IAirHandlerItem> getCapabilityKey() {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:air_pnc_item_custom");
        }
    }
}
