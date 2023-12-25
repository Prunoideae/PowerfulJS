package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.IAlloyInteraction;
import mekanism.api.tier.AlloyTier;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityAlloyInteractable {

    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    @FunctionalInterface
    public interface AlloyInteraction {
        void onInteraction(BlockEntity instance, Player player, ItemStack itemStack, AlloyTier tier);
    }


    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IAlloyInteraction> {

        private AlloyInteraction interaction;

        public BlockEntityBuilder interaction(AlloyInteraction interaction) {
            this.interaction = interaction;
            return this;
        }

        @Override
        public IAlloyInteraction getCapability(BlockEntity instance) {
            return (player, stack, tier) -> {
                if (interaction != null)
                    interaction.onInteraction(instance, player, stack, tier);
            };
        }

        @Override
        public Capability<IAlloyInteraction> getCapabilityKey() {
            return Capabilities.ALLOY_INTERACTION;
        }

        @Override
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:alloy_be_custom");
        }
    }
}
