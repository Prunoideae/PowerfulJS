package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilitySlurry {

    public ItemStackBuilder itemStack() {
        return new ItemStackBuilder();
    }

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static abstract class SlurryBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, ISlurryHandler, Slurry, SlurryStack> {
        @Override
        public Capability<ISlurryHandler> getCapabilityKey() {
            return Capabilities.SLURRY_HANDLER;
        }

        @Override
        public ISlurryHandler getCapability(I instance) {
            return new ISlurryHandler() {
                @Override
                public int getTanks() {
                    return getTanks == null ? 1 : getTanks.applyAsInt(instance);
                }

                @Override
                public @NotNull SlurryStack getChemicalInTank(int i) {
                    return getChemicalInTank == null ? SlurryStack.EMPTY : getChemicalInTank.apply(instance, i);
                }

                @Override
                public void setChemicalInTank(int i, @NotNull SlurryStack stack) {
                    if (setChemicalInTank != null)
                        setChemicalInTank.accept(instance, i, stack);
                }

                @Override
                public long getTankCapacity(int i) {
                    return getTankCapacity == null ? 0 : getTankCapacity.apply(instance, i);
                }

                @Override
                public boolean isValid(int i, @NotNull SlurryStack stack) {
                    return isValid == null || isValid.test(instance, i, stack);
                }

                @Override
                public @NotNull SlurryStack insertChemical(int i, @NotNull SlurryStack stack, @NotNull Action action) {
                    return insertChemical == null ? stack : insertChemical.apply(instance, i, stack, action.simulate());
                }

                @Override
                public @NotNull SlurryStack extractChemical(int i, long l, @NotNull Action action) {
                    return extractChemical == null ? SlurryStack.EMPTY : extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }

    public static class ItemStackBuilder extends SlurryBuilder<ItemStack> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:slurry_item_custom");
        }
    }

    public static class BlockEntityBuilder extends SlurryBuilder<BlockEntity> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:slurry_be_custom");
        }
    }
}
