package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilityPigment {
    public ItemStackBuilder itemStack() {
        return new ItemStackBuilder();
    }

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static abstract class PigmentBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, IPigmentHandler, Pigment, PigmentStack> {
        @Override
        public Capability<IPigmentHandler> getCapabilityKey() {
            return Capabilities.PIGMENT_HANDLER;
        }

        @Override
        public IPigmentHandler getCapability(I instance) {
            return new IPigmentHandler() {
                @Override
                public int getTanks() {
                    return getTanks == null ? 1 : getTanks.applyAsInt(instance);
                }

                @Override
                public @NotNull PigmentStack getChemicalInTank(int i) {
                    return getChemicalInTank == null ? PigmentStack.EMPTY : getChemicalInTank.apply(instance, i);
                }

                @Override
                public void setChemicalInTank(int i, @NotNull PigmentStack stack) {
                    if (setChemicalInTank != null)
                        setChemicalInTank.accept(instance, i, stack);
                }

                @Override
                public long getTankCapacity(int i) {
                    return getTankCapacity == null ? 0 : getTankCapacity.apply(instance, i);
                }

                @Override
                public boolean isValid(int i, @NotNull PigmentStack stack) {
                    return isValid == null || isValid.test(instance, i, stack);
                }

                @Override
                public @NotNull PigmentStack insertChemical(int i, @NotNull PigmentStack stack, @NotNull Action action) {
                    return insertChemical == null ? stack : insertChemical.apply(instance, i, stack, action.simulate());
                }

                @Override
                public @NotNull PigmentStack extractChemical(int i, long l, @NotNull Action action) {
                    return extractChemical == null ? PigmentStack.EMPTY : extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }

    public static class ItemStackBuilder extends PigmentBuilder<ItemStack> {
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:pigment_item_custom");
        }
    }

    public static class BlockEntityBuilder extends PigmentBuilder<BlockEntity> {
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:pigment_be_custom");
        }
    }
}
