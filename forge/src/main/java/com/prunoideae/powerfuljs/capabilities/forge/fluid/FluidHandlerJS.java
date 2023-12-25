package com.prunoideae.powerfuljs.capabilities.forge.fluid;


import com.google.common.base.Suppliers;
import com.prunoideae.powerfuljs.PowerfulJS;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@RemapPrefixForJS("pjs$")
@SuppressWarnings("UnstableApiUsage")
public class FluidHandlerJS<T extends CapabilityProvider<T>> implements IFluidHandler, INBTSerializable<CompoundTag> {

    static FluidStackJS fromForge(FluidStack fluidStack) {
        return FluidStackJS.of(FluidStackHooksForge.fromForge(fluidStack));
    }

    static FluidStack toForge(FluidStackJS fluidStackJS) {
        return FluidStackHooksForge.toForge(fluidStackJS.getFluidStack());
    }

    @FunctionalInterface
    public interface GetFluidInTank<T extends CapabilityProvider<T>> {
        FluidStackJS get(T instance, FluidHandlerJS<T> storage);
    }

    @FunctionalInterface
    public interface GetTankCapacity<T extends CapabilityProvider<T>> {
        int get(T instance, FluidHandlerJS<T> storage);
    }

    @FunctionalInterface
    public interface IsFluidValid<T extends CapabilityProvider<T>> {
        boolean test(T instance, FluidHandlerJS<T> storage, FluidStackJS stack);
    }

    @FunctionalInterface
    public interface FluidTransfer<T extends CapabilityProvider<T>> {
        int transfer(T instance, FluidHandlerJS<T> storage, FluidStackJS fluidStack, boolean simulate);
    }

    protected final T instance;
    protected final int capacity;
    private FluidStack fluidStack = FluidStack.EMPTY;
    private final GetFluidInTank<T> getFluidInTank;
    private final GetTankCapacity<T> getTankCapacity;
    private final IsFluidValid<T> isFluidValid;
    private final FluidTransfer<T> fill;
    private final FluidTransfer<T> drain;


    public FluidHandlerJS(T instance, int capacity, GetFluidInTank<T> getFluidInTank, GetTankCapacity<T> getTankCapacity, IsFluidValid<T> isFluidValid, FluidTransfer<T> fill, FluidTransfer<T> drain) {
        this.instance = instance;
        this.capacity = capacity;

        this.getFluidInTank = getFluidInTank;
        this.getTankCapacity = getTankCapacity;
        this.isFluidValid = isFluidValid;
        this.fill = fill;
        this.drain = drain;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        if (getFluidInTank != null) return toForge(getFluidInTank.get(instance, this));
        return fluidStack;
    }

    @Override
    public int getTankCapacity(int i) {
        if (getTankCapacity != null) return getTankCapacity.get(instance, this);
        return capacity;
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        if (isFluidValid != null) return isFluidValid.test(instance, this, fromForge(fluidStack));
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        if (fill != null) return fill.transfer(instance, this, fromForge(fluidStack), fluidAction.simulate());
        return pjs$fillRaw(fluidStack, fluidAction.simulate());
    }

    @Info("Util method to fill fluid into the storage. Note that this manipulates the internal `fluidStack` in the tank.")
    public int pjs$fillRaw(FluidStack stack, boolean simulate) {
        if (stack.isEmpty() || !isFluidValid(0, stack)) return 0;
        if (simulate) {
            if (fluidStack.isEmpty() || fluidStack.getAmount() == 0)
                return Math.min(getTankCapacity(0), stack.getAmount());
            if (!fluidStack.isFluidEqual(stack)) return 0;
            return Math.min(getTankCapacity(0) - fluidStack.getAmount(), stack.getAmount());
        }

        if (fluidStack.isEmpty() || fluidStack.getAmount() == 0) {
            fluidStack = new FluidStack(stack, Math.min(getTankCapacity(0), stack.getAmount()));
            return fluidStack.getAmount();
        }
        if (!fluidStack.isFluidEqual(stack)) {
            return 0;
        }
        int capacity = getTankCapacity(0);
        int limit = capacity - fluidStack.getAmount();
        if (stack.getAmount() < limit) {
            fluidStack.grow(stack.getAmount());
            return stack.getAmount();
        } else {
            fluidStack.setAmount(capacity);
            return limit;
        }
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        if (drain != null) {
            int amount = drain.transfer(instance, this, fromForge(fluidStack), fluidAction.simulate());
            return new FluidStack(fluidStack, amount);
        }
        return new FluidStack(fluidStack, pjs$drainRaw(fluidStack, fluidAction.simulate()));
    }

    @Info("Util method to drain fluid from the storage. Note that this manipulates the internal `fluidStack` in the tank.")
    public int pjs$drainRaw(FluidStack stack, boolean simulate) {
        int drained = stack.getAmount();
        if (fluidStack.isEmpty() || fluidStack.getAmount() == 0) return 0;
        if (fluidStack.getAmount() < drained) drained = fluidStack.getAmount();
        if (!simulate) fluidStack.shrink(drained);
        return drained;
    }


    @Override
    @HideFromJS
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        if (i == 0) return FluidStack.EMPTY;
        if (getFluidInTank(0).isEmpty()) return FluidStack.EMPTY;
        FluidStack fluid = getFluidInTank(0).copy();
        fluid.setAmount(i);
        return drain(fluid, fluidAction);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        fluidStack.writeToNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        fluidStack = FluidStack.loadFluidStackFromNBT(arg);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    public FluidStackJS getFluid() {
        return fromForge(fluidStack);
    }

    public void setFluid(FluidStackJS fluid) {
        this.fluidStack = toForge(fluid);
    }

    public static class Builder<T extends CapabilityProvider<T>> extends CapabilityBuilderForge<T, IFluidHandler> {

        protected final int capacity;
        protected GetFluidInTank<T> getFluidInTank;
        protected GetTankCapacity<T> getTankCapacity;
        protected IsFluidValid<T> isFluidValid;
        protected FluidTransfer<T> fill;
        protected FluidTransfer<T> drain;

        public Builder<T> getFluidInTank(GetFluidInTank<T> getFluidInTank) {
            this.getFluidInTank = getFluidInTank;
            return this;
        }

        public Builder<T> getTankCapacity(GetTankCapacity<T> getTankCapacity) {
            this.getTankCapacity = getTankCapacity;
            return this;
        }

        public Builder<T> isFluidValid(IsFluidValid<T> isFluidValid) {
            this.isFluidValid = isFluidValid;
            return this;
        }

        public Builder<T> fill(FluidTransfer<T> fill) {
            this.fill = fill;
            return this;
        }

        public Builder<T> drain(FluidTransfer<T> drain) {
            this.drain = drain;
            return this;
        }

        public Builder(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public IFluidHandler getCapability(T instance) {
            return new FluidHandlerJS<>(instance, capacity, getFluidInTank, getTankCapacity, isFluidValid, fill, drain);
        }

        @Override
        public Capability<IFluidHandler> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER;
        }

        @Override
        protected ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerfuljs:fluid");
        }

        public Builder<T> acceptFluid(FluidStackJS fluidStackJS) {
            return acceptFluid(fluidStackJS, false);
        }

        public Builder<T> acceptFluid(FluidStackJS fluidStackJS, boolean matchNbt) {
            isFluidValid((instance1, storage, stack) ->
                    matchNbt ?
                            fluidStackJS.matches(stack) :
                            fluidStackJS.getFluid().equals(stack.getFluid())
            );
            return this;
        }

        public Builder<T> acceptFluidTag(ResourceLocation fluidTag) {
            isFluidValid((instance1, storage, stack) -> stack.hasTag(fluidTag));
            return this;
        }

        public Builder<T> noDrain() {
            drain((instance1, storage, fluidStack1, simulate) -> 0);
            return this;
        }

        public Builder<T> noFill() {
            fill((instance1, storage, fluidStack1, simulate) -> 0);
            return this;
        }
    }
}
