package com.prunoideae.powerfuljs.capabilities.forge.fluid;

import com.google.common.collect.Lists;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RemapPrefixForJS("pjs$")
@SuppressWarnings("UnstableApiUsage")
public class MultiHandlerJS<T extends CapabilityProvider<T>> implements IFluidHandler, INBTSerializable<ListTag> {

    @FunctionalInterface
    public interface GetFluidInTank<T extends CapabilityProvider<T>> {
        FluidStackJS get(T instance, MultiHandlerJS<T> storage);
    }

    @FunctionalInterface
    public interface GetTankCapacity<T extends CapabilityProvider<T>> {
        int get(T instance, MultiHandlerJS<T> storage);
    }

    @FunctionalInterface
    public interface IsFluidValid<T extends CapabilityProvider<T>> {
        boolean test(T instance, MultiHandlerJS<T> storage, FluidStackJS stack);
    }

    @FunctionalInterface
    public interface FluidTransfer<T extends CapabilityProvider<T>> {
        int transfer(T instance, MultiHandlerJS<T> storage, FluidStackJS fluidStack, boolean simulate);
    }


    protected final T instance;
    protected final int[] capacities;
    public final NonNullList<FluidStack> fluidStacks;

    private final List<GetFluidInTank<T>> getFluidInTank;
    private final List<GetTankCapacity<T>> getTankCapacity;
    private final List<IsFluidValid<T>> isFluidValid;
    private final List<FluidTransfer<T>> fill;
    private final List<FluidTransfer<T>> drain;


    public MultiHandlerJS(T instance, int[] capacities,
                          List<GetFluidInTank<T>> getFluidInTank, List<GetTankCapacity<T>> getTankCapacity,
                          List<IsFluidValid<T>> isFluidValid,
                          List<FluidTransfer<T>> fill, List<FluidTransfer<T>> drain
    ) {
        this.instance = instance;
        this.capacities = capacities;
        fluidStacks = NonNullList.withSize(capacities.length, FluidStack.EMPTY);


        this.getFluidInTank = getFluidInTank;
        this.getTankCapacity = getTankCapacity;
        this.isFluidValid = isFluidValid;
        this.fill = fill;
        this.drain = drain;
    }

    @Override
    public int getTanks() {
        return capacities.length;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        if (i >= capacities.length) return FluidStack.EMPTY;
        GetFluidInTank<T> handler = getFluidInTank.get(i);
        if (handler != null) return FluidHandlerJS.toForge(handler.get(instance, this));
        return fluidStacks.get(i);
    }

    @Override
    public int getTankCapacity(int i) {
        if (i >= capacities.length) return 0;
        GetTankCapacity<T> handler = getTankCapacity.get(i);
        if (handler != null) return handler.get(instance, this);
        return capacities[i];
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        if (i >= capacities.length) return false;
        IsFluidValid<T> handler = isFluidValid.get(i);
        if (handler != null) return handler.test(instance, this, FluidHandlerJS.fromForge(fluidStack));
        return true;
    }

    @Override
    public int fill(FluidStack stack, FluidAction fluidAction) {
        if (stack.isEmpty()) return 0;
        int totalFilled = 0;
        FluidStack stackCopy = stack.copy();
        for (int i = 0; i < capacities.length; i++) {
            int thisFilled = fillInTank(i, stackCopy, fluidAction.simulate());
            stackCopy.shrink(thisFilled);
            totalFilled += thisFilled;
            if (stackCopy.getAmount() == 0) break;
        }
        return totalFilled;
    }

    public int fillInTank(int tank, FluidStack stack, boolean simulate) {
        if (tank >= capacities.length) return 0;
        FluidTransfer<T> handler = fill.get(tank);
        if (handler != null) return handler.transfer(instance, this, FluidHandlerJS.fromForge(stack), simulate);
        return fillInTankRaw(tank, stack, simulate);
    }

    public int fillInTankRaw(int tank, FluidStack stack, boolean simulate) {
        if (tank >= capacities.length) return 0;
        if (stack.isEmpty() || !isFluidValid(tank, stack)) return 0;
        FluidStack tankStack = fluidStacks.get(tank);
        if (simulate) {
            if (tankStack.isEmpty()) return Math.min(getTankCapacity(tank), stack.getAmount());
            if (!tankStack.isFluidEqual(stack)) return 0;
            return Math.min(getTankCapacity(tank) - tankStack.getAmount(), stack.getAmount());
        }

        if (tankStack.isEmpty() || tankStack.getAmount() == 0) {
            tankStack = new FluidStack(stack, Math.min(getTankCapacity(tank), stack.getAmount()));
            fluidStacks.set(tank, tankStack);
            return tankStack.getAmount();
        }
        if (!tankStack.isFluidEqual(stack)) return 0;
        int capacity = getTankCapacity(tank);
        int limit = capacity - tankStack.getAmount();
        if (stack.getAmount() < limit) {
            tankStack.grow(stack.getAmount());
            return stack.getAmount();
        } else {
            tankStack.setAmount(capacity);
            return limit;
        }
    }

    @Override
    public @NotNull FluidStack drain(FluidStack stack, FluidAction fluidAction) {
        if (stack.isEmpty()) return FluidStack.EMPTY;
        int totalDrained = 0;
        FluidStack stackCopy = stack.copy();
        for (int i = 0; i < capacities.length; i++) {
            int thisDrained = drainInTank(i, stackCopy, fluidAction.simulate());
            stackCopy.shrink(thisDrained);
            totalDrained += thisDrained;
            if (stackCopy.getAmount() == 0) break;
        }
        return new FluidStack(stack, totalDrained);
    }

    public int drainInTank(int tank, FluidStack stack, boolean simulate) {
        if (tank >= capacities.length) return 0;
        FluidTransfer<T> handler = drain.get(tank);
        if (handler != null) return handler.transfer(instance, this, FluidHandlerJS.fromForge(stack), simulate);
        return drainInTankRaw(tank, stack, simulate);
    }

    public int drainInTankRaw(int tank, FluidStack stack, boolean simulate) {
        if (tank >= capacities.length) return 0;
        FluidStack tankStack = fluidStacks.get(tank);
        int drained = stack.getAmount();
        if (tankStack.isEmpty() || tankStack.getAmount() == 0) return 0;
        if (tankStack.getAmount() < drained) drained = tankStack.getAmount();
        if (!simulate) tankStack.shrink(drained);
        return drained;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        if (i == 0) return FluidStack.EMPTY;
        FluidStack firstFluid = FluidStack.EMPTY;
        for (int j = 0; j < capacities.length; j++) {
            FluidStack tankFluid = getFluidInTank(i);
            if (!tankFluid.isEmpty()
                    && tankFluid.getAmount() > 0
                    && drainInTank(j, tankFluid, true) > 0
            ) {
                firstFluid = tankFluid;
                break;
            }
        }
        return firstFluid.isEmpty() ? firstFluid : drain(new FluidStack(firstFluid, i), fluidAction);
    }

    @Override
    public ListTag serializeNBT() {
        ListTag tags = new ListTag();
        for (FluidStack fluidStack : fluidStacks) {
            CompoundTag tag = new CompoundTag();
            fluidStack.writeToNBT(tag);
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public void deserializeNBT(ListTag arg) {
        for (int i = 0; i < Math.min(fluidStacks.size(), arg.size()); i++) {
            fluidStacks.set(i, FluidStack.loadFluidStackFromNBT(arg.getCompound(i)));
        }
    }

    public static class Builder<T extends CapabilityProvider<T>> extends CapabilityBuilderForge<T, IFluidHandler> {

        protected final int[] capacities;
        private final List<GetFluidInTank<T>> getFluidInTank;
        private final List<GetTankCapacity<T>> getTankCapacity;
        private final List<IsFluidValid<T>> isFluidValid;
        private final List<FluidTransfer<T>> fill;
        private final List<FluidTransfer<T>> drain;

        public Builder(int... capacities) {
            this.capacities = capacities;
            this.getFluidInTank = new ArrayList<>(Collections.nCopies(capacities.length, null));
            this.getTankCapacity = new ArrayList<>(Collections.nCopies(capacities.length, null));
            this.isFluidValid = new ArrayList<>(Collections.nCopies(capacities.length, null));
            this.fill = new ArrayList<>(Collections.nCopies(capacities.length, null));
            this.drain = new ArrayList<>(Collections.nCopies(capacities.length, null));
        }

        public Builder<T> getFluidInTank(int i, GetFluidInTank<T> getFluidInTank) {
            this.getFluidInTank.set(i, getFluidInTank);
            return this;
        }

        public Builder<T> getTankCapacity(int i, GetTankCapacity<T> getTankCapacity) {
            this.getTankCapacity.set(i, getTankCapacity);
            return this;
        }

        public Builder<T> isFluidValid(int i, IsFluidValid<T> isFluidValid) {
            this.isFluidValid.set(i, isFluidValid);
            return this;
        }

        public Builder<T> fill(int i, FluidTransfer<T> fill) {
            this.fill.set(i, fill);
            return this;
        }

        public Builder<T> drain(int i, FluidTransfer<T> drain) {
            this.drain.set(i, drain);
            return this;
        }

        @Override
        public IFluidHandler getCapability(T instance) {
            return new MultiHandlerJS<>(
                    instance, capacities,
                    getFluidInTank, getTankCapacity,
                    isFluidValid,
                    fill, drain
            );
        }

        @Override
        public Capability<IFluidHandler> getCapabilityKey() {
            return ForgeCapabilities.FLUID_HANDLER;
        }

        @Override
        protected ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerfuljs:fluid");
        }

        public Builder<T> acceptFluid(int tank, FluidStackJS fluidStackJS) {
            return acceptFluid(tank, fluidStackJS, false);
        }

        public Builder<T> acceptFluid(int tank, FluidStackJS fluidStackJS, boolean matchNbt) {
            isFluidValid(tank, (instance1, storage, stack) ->
                    matchNbt ?
                            fluidStackJS.matches(stack) :
                            fluidStackJS.getFluid().equals(stack.getFluid())
            );
            return this;
        }

        public Builder<T> acceptFluidTag(int tank, ResourceLocation fluidTag) {
            isFluidValid(tank, (instance1, storage, stack) -> stack.hasTag(fluidTag));
            return this;
        }

        public Builder<T> noDrain(int tank) {
            drain(tank, (instance1, storage, fluidStack1, simulate) -> 0);
            return this;
        }

        public Builder<T> noFill(int tank) {
            fill(tank, (instance1, storage, fluidStack1, simulate) -> 0);
            return this;
        }
    }
}
