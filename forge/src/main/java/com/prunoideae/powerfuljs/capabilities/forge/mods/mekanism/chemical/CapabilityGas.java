package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.Action;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

public class CapabilityGas {
    public static ItemStackBuilder itemStack() {
        return new ItemStackBuilder();
    }

    public static BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static abstract class GasBuilder<I> extends CapabilityChemical.ChemicalBuilder<I, IGasHandler, Gas, GasStack> {
        @Override
        @HideFromJS
        public Capability<IGasHandler> getCapabilityKey() {
            return Capabilities.GAS_HANDLER;
        }

        @Override
        @HideFromJS
        public IGasHandler getCapability(I instance) {
            return new IGasHandler() {
                @Override
                public int getTanks() {
                    return getTanks == null ? 1 : getTanks.applyAsInt(instance);
                }

                @Override
                public @NotNull GasStack getChemicalInTank(int i) {
                    return getChemicalInTank == null ? GasStack.EMPTY : getChemicalInTank.apply(instance, i);
                }

                @Override
                public void setChemicalInTank(int i, @NotNull GasStack stack) {
                    if (setChemicalInTank != null)
                        setChemicalInTank.accept(instance, i, stack);
                }

                @Override
                public long getTankCapacity(int i) {
                    return getTankCapacity == null ? 0 : getTankCapacity.apply(instance, i);
                }

                @Override
                public boolean isValid(int i, @NotNull GasStack stack) {
                    return isValid == null || isValid.test(instance, i, stack);
                }

                @Override
                public @NotNull GasStack insertChemical(int i, @NotNull GasStack stack, @NotNull Action action) {
                    return insertChemical == null ? GasStack.EMPTY : insertChemical.apply(instance, i, stack, action.simulate());
                }

                @Override
                public @NotNull GasStack extractChemical(int i, long l, @NotNull Action action) {
                    return extractChemical == null ? GasStack.EMPTY : extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }

    public static class ItemStackBuilder extends GasBuilder<ItemStack> {
        @Override
        @HideFromJS
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:gas_item_custom");
        }
    }

    public static class BlockEntityBuilder extends GasBuilder<BlockEntity> {
        @Override
        @HideFromJS
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:gas_be_custom");
        }
    }
}
