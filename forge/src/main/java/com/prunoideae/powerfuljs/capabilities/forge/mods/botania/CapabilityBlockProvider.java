package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.BlockProvider;

public class CapabilityBlockProvider {

    public BuilderItemStack blockProvider() {
        return new BuilderItemStack();
    }

    @FunctionalInterface
    public interface ProvideBlock {
        boolean provideBlock(ItemStack instance, Player player, ItemStack requestor, Block block, boolean doit);
    }

    @FunctionalInterface
    public interface GetBlockCount {
        int getBlockCount(ItemStack instance, Player player, ItemStack requestor, Block block);
    }

    public static class BuilderItemStack extends CapabilityBuilderForge<ItemStack, BlockProvider> {
        private ProvideBlock provideBlock;
        private GetBlockCount getBlockCount;

        public BuilderItemStack provideBlock(ProvideBlock provideBlock) {
            this.provideBlock = provideBlock;
            return this;
        }

        public BuilderItemStack getBlockCount(GetBlockCount getBlockCount) {
            this.getBlockCount = getBlockCount;
            return this;
        }

        @Override
        @HideFromJS
        public BlockProvider getCapability(ItemStack instance) {
            return new BlockProvider() {
                @Override
                public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
                    return provideBlock != null && provideBlock.provideBlock(instance, player, requestor, block, doit);
                }

                @Override
                public int getBlockCount(Player player, ItemStack requestor, Block block) {
                    return getBlockCount == null ? 0 : getBlockCount.getBlockCount(instance, player, requestor, block);
                }
            };
        }

        @Override
        @HideFromJS
        public Capability<BlockProvider> getCapabilityKey() {
            return BotaniaForgeCapabilities.BLOCK_PROVIDER;
        }

        @Override
        @HideFromJS
        public ResourceLocation getDefaultKey() {
            return new ResourceLocation("powerful:block_provider_botania");
        }
    }
}
