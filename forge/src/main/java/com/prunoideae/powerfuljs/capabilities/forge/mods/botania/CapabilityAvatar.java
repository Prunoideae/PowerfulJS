package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;

import java.util.function.BiConsumer;

public class CapabilityAvatar {

    public AvatarBehaviorBuilder wieldable() {
        return new AvatarBehaviorBuilder();
    }

    public static class AvatarBehaviorBuilder extends CapabilityBuilderForge<ItemStack, AvatarWieldable> {
        private BiConsumer<ItemStack, Avatar> onUpdate;
        private ResourceLocation overlay = new ResourceLocation("botania:textures/model/avatar.png");

        public AvatarBehaviorBuilder onUpdate(BiConsumer<ItemStack, Avatar> onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public AvatarBehaviorBuilder setOverlay(ResourceLocation overlay) {
            this.overlay = overlay;
            return this;
        }

        @Override
        @HideFromJS
        public AvatarWieldable getCapability(ItemStack instance) {
            return new AvatarWieldable() {
                @Override
                public void onAvatarUpdate(Avatar tile) {
                    BlockEntity t = (BlockEntity) tile;
                    if (!t.hasLevel())
                        return;
                    if (onUpdate != null && t.getLevel().getGameTime() % 10 == 0)
                        onUpdate.accept(instance, tile);
                }

                @Override
                public ResourceLocation getOverlayResource(Avatar tile) {
                    return overlay;
                }
            };
        }

        @Override
        @HideFromJS
        public Capability<AvatarWieldable> getCapabilityKey() {
            return BotaniaForgeCapabilities.AVATAR_WIELDABLE;
        }

        @Override
        @HideFromJS
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:botania_avatar_wieldable");
        }
    }
}
