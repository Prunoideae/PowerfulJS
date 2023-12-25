package com.prunoideae.powerfuljs.capabilities.forge.fluid;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class FluidHandlerItemJS extends FluidHandlerJS<ItemStack> implements IFluidHandlerItem {

    private final BiFunction<ItemStack, FluidHandlerItemJS, ItemStack> getContainer;

    public FluidHandlerItemJS(
            ItemStack instance, int capacity,
            GetFluidInTank<ItemStack> getFluidInTank, GetTankCapacity<ItemStack> getTankCapacity,
            IsFluidValid<ItemStack> isFluidValid,
            FluidTransfer<ItemStack> fill, FluidTransfer<ItemStack> drain,
            BiFunction<ItemStack, FluidHandlerItemJS, ItemStack> getContainer
    ) {
        super(instance, capacity, getFluidInTank, getTankCapacity, isFluidValid, fill, drain);
        this.getContainer = getContainer;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        if (getContainer != null) return getContainer.apply(instance, this);
        return ItemStack.EMPTY;
    }

    public static class Builder extends CapabilityBuilderForge<ItemStack, IFluidHandlerItem> {
        protected final int capacity;
        protected GetFluidInTank<ItemStack> getFluidInTank;
        protected GetTankCapacity<ItemStack> getTankCapacity;
        protected IsFluidValid<ItemStack> isFluidValid;
        protected FluidTransfer<ItemStack> fill;
        protected FluidTransfer<ItemStack> drain;
        protected BiFunction<ItemStack, FluidHandlerItemJS, ItemStack> getContainer;

        public Builder getFluidInTank(GetFluidInTank<ItemStack> getFluidInTank) {
            this.getFluidInTank = getFluidInTank;
            return this;
        }

        public Builder getTankCapacity(GetTankCapacity<ItemStack> getTankCapacity) {
            this.getTankCapacity = getTankCapacity;
            return this;
        }

        public Builder isFluidValid(IsFluidValid<ItemStack> isFluidValid) {
            this.isFluidValid = isFluidValid;
            return this;
        }

        public Builder fill(FluidTransfer<ItemStack> fill) {
            this.fill = fill;
            return this;
        }

        public Builder drain(FluidTransfer<ItemStack> drain) {
            this.drain = drain;
            return this;
        }

        public Builder getContainer(BiFunction<ItemStack, FluidHandlerItemJS, ItemStack> getContainer) {
            this.getContainer = getContainer;
            return this;
        }

        public Builder(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public IFluidHandlerItem getCapability(ItemStack instance) {
            return new FluidHandlerItemJS(
                    instance, capacity,
                    getFluidInTank, getTankCapacity,
                    isFluidValid,
                    fill, drain,
                    getContainer
            );
        }

        @Override
        public Capability<IFluidHandlerItem> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER_ITEM;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerfuljs:fluid");
        }

        public Builder containerItem(ItemStack stack) {
            getContainer((stack1, fluidHandlerItemJS) -> stack);
            return this;
        }
    }
}
