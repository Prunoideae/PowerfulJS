package com.prunoideae.powerfuljs.capabilities.forge;

import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class CapabilityFluid {

    @FunctionalInterface
    public interface FluidIOItemStack {
        int transfer(ItemStack container, FluidStackJS resource, boolean simulate);
    }

    @FunctionalInterface
    public interface FluidIOBlockEntity {
        int transfer(BlockEntity container, FluidStackJS resource, boolean simulate);
    }

    public NormalTankBuilderItemStack itemStack(int capacity) {
        return new NormalTankBuilderItemStack(capacity);
    }

    public CustomTankBuilderItemStack customItemStack() {
        return new CustomTankBuilderItemStack();
    }

    public CustomTankBuilderBlockEntity customBlockEntity() {
        return new CustomTankBuilderBlockEntity();
    }

    public static class NormalTankBuilderItemStack extends BuilderForgeCapability<ItemStack, IFluidHandlerItem> {
        private final int capacity;

        private NormalTankBuilderItemStack(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public IFluidHandlerItem getCapability(ItemStack instance) {
            return new FluidHandlerItemStack(instance, capacity);
        }

        @Override
        public Capability<IFluidHandlerItem> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER_ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:normal_tank_item");
        }
    }

    public static class FluidHandlerItemStackCustom extends FluidHandlerItemStack {

        private final FluidIOItemStack onFill;
        private final FluidIOItemStack onDrain;
        private final BiPredicate<ItemStack, FluidStackJS> isFluidGood;
        private final ToIntFunction<ItemStack> getCapacity;

        public FluidHandlerItemStackCustom(@NotNull ItemStack container, int capacity,
                                           FluidIOItemStack onFill, FluidIOItemStack onDrain,
                                           BiPredicate<ItemStack, FluidStackJS> isFluidGood,
                                           ToIntFunction<ItemStack> getCapacity) {
            super(container, capacity);
            this.onDrain = onDrain;
            this.onFill = onFill;
            this.isFluidGood = isFluidGood;
            this.getCapacity = getCapacity;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return isFluidGood == null || isFluidGood.test(container, FluidStackJS.of(FluidStackHooksForge.fromForge(stack)));
        }

        @Override
        public int fill(FluidStack resource, FluidAction doFill) {
            return onFill == null ? super.fill(resource, doFill) : onFill.transfer(container, FluidStackJS.of(FluidStackHooksForge.fromForge(resource)), doFill.simulate());
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (onDrain == null)
                return super.drain(resource, action);
            FluidStack stack = resource.copy();
            stack.setAmount(onDrain.transfer(container, FluidStackJS.of(FluidStackHooksForge.fromForge(resource)), action.simulate()));
            return stack;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (onDrain == null)
                return super.drain(maxDrain, action);
            FluidStack stack = getFluid().copy();
            FluidStack drain = getFluid().copy();
            drain.setAmount(maxDrain);
            stack.setAmount(onDrain.transfer(container, FluidStackJS.of(FluidStackHooksForge.fromForge(drain)), action.simulate()));
            return stack;
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return isFluidValid(0, fluid);
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluid) {
            return isFluidValid(0, fluid);
        }

        @Override
        public int getTankCapacity(int tank) {
            return getCapacity == null ? 1000 : getCapacity.applyAsInt(container);
        }
    }

    public static class CustomTankBuilderItemStack extends BuilderForgeCapability<ItemStack, IFluidHandlerItem> {
        private FluidIOItemStack onFill;
        private FluidIOItemStack onDrain;
        private BiPredicate<ItemStack, FluidStackJS> isFluidGood;
        private ToIntFunction<ItemStack> getCapacity;

        public CustomTankBuilderItemStack withCapacity(int capacity) {
            return getCapacity(i -> capacity);
        }

        public CustomTankBuilderItemStack getCapacity(ToIntFunction<ItemStack> getCapacity) {
            this.getCapacity = getCapacity;
            return this;
        }

        public CustomTankBuilderItemStack acceptFluid(Fluid fluid) {
            return isFluidGood((i, fluidStackJS) -> fluidStackJS.getFluid() == fluid);
        }

        public CustomTankBuilderItemStack isFluidGood(BiPredicate<ItemStack, FluidStackJS> isFluidGood) {
            this.isFluidGood = isFluidGood;
            return this;
        }

        public CustomTankBuilderItemStack onFill(FluidIOItemStack onFill) {
            this.onFill = onFill;
            return this;
        }

        public CustomTankBuilderItemStack onDrain(FluidIOItemStack onDrain) {
            this.onDrain = onDrain;
            return this;
        }

        @Override
        @HideFromJS
        public IFluidHandlerItem getCapability(ItemStack instance) {
            return new FluidHandlerItemStackCustom(instance, 1000, onFill, onDrain, isFluidGood, getCapacity);
        }

        @Override
        @HideFromJS
        public Capability<IFluidHandlerItem> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER_ITEM;
        }

        @Override
        @HideFromJS
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:custom_tank_item");
        }
    }

    public static class CustomTankBuilderBlockEntity extends BuilderForgeCapability<BlockEntity, IFluidHandler> {
        private FluidIOBlockEntity onFill;
        private FluidIOBlockEntity onDrain;
        private BiPredicate<BlockEntity, FluidStackJS> isFluidGood;
        private ToIntFunction<BlockEntity> getCapacity;
        private Function<BlockEntity, FluidStackJS> getFluid;

        public CustomTankBuilderBlockEntity onFill(FluidIOBlockEntity onFill) {
            this.onFill = onFill;
            return this;
        }

        public CustomTankBuilderBlockEntity onDrain(FluidIOBlockEntity onDrain) {
            this.onDrain = onDrain;
            return this;
        }

        public CustomTankBuilderBlockEntity isFluidGood(BiPredicate<BlockEntity, FluidStackJS> isFluidGood) {
            this.isFluidGood = isFluidGood;
            return this;
        }

        public CustomTankBuilderBlockEntity getCapacity(ToIntFunction<BlockEntity> getCapacity) {
            this.getCapacity = getCapacity;
            return this;
        }

        public CustomTankBuilderBlockEntity getFluid(Function<BlockEntity, FluidStackJS> getFluid) {
            this.getFluid = getFluid;
            return this;
        }

        @Override
        public IFluidHandler getCapability(BlockEntity instance) {
            return new IFluidHandler() {
                @Override
                public int getTanks() {
                    return 1;
                }

                @Override
                public @NotNull FluidStack getFluidInTank(int i) {
                    return getFluid != null ? FluidStackHooksForge.toForge(getFluid.apply(instance).getFluidStack()) : FluidStack.EMPTY;
                }

                @Override
                public int getTankCapacity(int i) {
                    return getCapacity != null ? getCapacity.applyAsInt(instance) : 1000;
                }

                @Override
                public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
                    return isFluidGood == null || isFluidGood.test(instance, FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack)));
                }

                @Override
                public int fill(FluidStack fluidStack, FluidAction fluidAction) {
                    return onFill != null ? onFill.transfer(instance, FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack)), fluidAction.simulate()) : 0;
                }

                @Override
                public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
                    int drained = onDrain != null ? onDrain.transfer(instance, FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack)), fluidAction.simulate()) : 0;
                    if (drained == 0)
                        return FluidStack.EMPTY;
                    return new FluidStack(fluidStack, drained);
                }

                @Override
                public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
                    FluidStack inTank = getFluidInTank(0).copy();
                    if (inTank.isEmpty())
                        return inTank;
                    inTank.setAmount(i);
                    return drain(inTank, fluidAction);
                }
            };
        }

        @Override
        public Capability<IFluidHandler> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:custom_tank_be");
        }
    }

}
