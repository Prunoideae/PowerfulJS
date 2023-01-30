package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.IConfigurable;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

import java.util.function.BiFunction;

public class CapabilityConfigurable {
    public BlockEntityBuilder blockEntity() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IConfigurable> {
        private BiFunction<BlockEntity, Player, String> onSneakRightClick;
        private BiFunction<BlockEntity, Player, String> onRightClick;

        public BlockEntityBuilder onSneakRightClick(BiFunction<BlockEntity, Player, String> onSneakRightClick) {
            this.onSneakRightClick = onSneakRightClick;
            return this;
        }

        public BlockEntityBuilder onRightClick(BiFunction<BlockEntity, Player, String> onRightClick) {
            this.onRightClick = onRightClick;
            return this;
        }

        @Override
        public IConfigurable getCapability(BlockEntity instance) {
            return new IConfigurable() {
                @Override
                public InteractionResult onSneakRightClick(Player player) {
                    return onSneakRightClick != null ? InteractionResult.valueOf(onSneakRightClick.apply(instance, player)) : InteractionResult.FAIL;
                }

                @Override
                public InteractionResult onRightClick(Player player) {
                    return onRightClick != null ? InteractionResult.valueOf(onRightClick.apply(instance, player)) : InteractionResult.FAIL;
                }
            };
        }

        @Override
        public Capability<IConfigurable> getCapabilityKey() {
            return Capabilities.CONFIGURABLE;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mek_configurable_be");
        }
    }
}
