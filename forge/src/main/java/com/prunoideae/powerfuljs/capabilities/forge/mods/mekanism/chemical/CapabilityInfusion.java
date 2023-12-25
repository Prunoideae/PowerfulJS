package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilityInfusion {
    public ItemStackBuilder itemStack() {
        return new ItemStackBuilder();
    }

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static abstract class InfusionBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, IInfusionHandler, InfuseType, InfusionStack> {
        @Override
        public Capability<IInfusionHandler> getCapabilityKey() {
            return Capabilities.INFUSION_HANDLER;
        }

        @Override
        public IInfusionHandler getCapability(I instance) {
            return new IInfusionHandler() {
                @Override
                public int getTanks() {
                    return getTanks == null ? 1 : getTanks.applyAsInt(instance);
                }

                @Override
                public @NotNull InfusionStack getChemicalInTank(int i) {
                    return getChemicalInTank == null ? InfusionStack.EMPTY : getChemicalInTank.apply(instance, i);
                }

                @Override
                public void setChemicalInTank(int i, @NotNull InfusionStack stack) {
                    if (setChemicalInTank != null)
                        setChemicalInTank.accept(instance, i, stack);
                }

                @Override
                public long getTankCapacity(int i) {
                    return getTankCapacity == null ? 0 : getTankCapacity.apply(instance, i);
                }

                @Override
                public boolean isValid(int i, @NotNull InfusionStack stack) {
                    return isValid == null || isValid.test(instance, i, stack);
                }

                @Override
                public @NotNull InfusionStack insertChemical(int i, @NotNull InfusionStack stack, @NotNull Action action) {
                    return insertChemical == null ? stack : insertChemical.apply(instance, i, stack, action.simulate());
                }

                @Override
                public @NotNull InfusionStack extractChemical(int i, long l, @NotNull Action action) {
                    return extractChemical == null ? InfusionStack.EMPTY : extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }

    public static class ItemStackBuilder extends InfusionBuilder<ItemStack> {
        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:infusion_item_custom");
        }
    }

    public static class BlockEntityBuilder extends InfusionBuilder<BlockEntity> {

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:infusion_be_custom");
        }
    }
}
