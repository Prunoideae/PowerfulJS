package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.relic.RelicImpl;

import java.util.List;
import java.util.UUID;


public class CapabilityRelic {

    @FunctionalInterface
    public interface RelicTick {
        void tick(ItemStack stack, Relic relic, Player player);
    }

    public NormalRelicBuilder normalRelic() {
        return normalRelic(null);
    }

    public NormalRelicBuilder normalRelic(ResourceLocation advancement) {
        return new NormalRelicBuilder(advancement);
    }

    public CustomRelicBuilder customRelic() {
        return new CustomRelicBuilder();
    }

    public void addRelicTooltipForItem(ItemStack stack, List<Component> tooltip) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    public static class NormalRelicBuilder extends CapabilityBuilderForge<ItemStack, Relic> {

        private final ResourceLocation advancement;

        public NormalRelicBuilder(ResourceLocation advancement) {
            this.advancement = advancement;
        }

        @Override
        public Relic getCapability(ItemStack instance) {
            return new RelicImpl(instance, advancement);
        }

        @Override
        public Capability<Relic> getCapabilityKey() {
            return BotaniaForgeCapabilities.RELIC;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:relic_botania");
        }
    }

    public static class CustomRelicBuilder extends CapabilityBuilderForge<ItemStack, Relic> {
        private static final String TAG_SOULBIND_UUID = "soulbindUUID";

        private RelicTick tick;
        private ResourceLocation advancement;
        private boolean shouldDamageWrongPlayer = true;

        public CustomRelicBuilder setAdvancement(ResourceLocation advancement) {
            this.advancement = advancement;
            return this;
        }

        public CustomRelicBuilder shouldDamageWrongPlayer(boolean shouldDamageWrongPlayer) {
            this.shouldDamageWrongPlayer = shouldDamageWrongPlayer;
            return this;
        }

        public CustomRelicBuilder onTick(RelicTick tick) {
            this.tick = tick;
            return this;
        }

        @Override
        @HideFromJS
        public Relic getCapability(ItemStack instance) {
            return new Relic() {
                @Override
                public void bindToUUID(UUID uuid) {
                    ItemNBTHelper.setString(instance, TAG_SOULBIND_UUID, uuid.toString());
                }

                @Override
                public @Nullable UUID getSoulbindUUID() {
                    if (ItemNBTHelper.verifyExistance(instance, TAG_SOULBIND_UUID)) {
                        try {
                            return UUID.fromString(ItemNBTHelper.getString(instance, TAG_SOULBIND_UUID, ""));
                        } catch (IllegalArgumentException ex) { // Bad UUID in tag
                            ItemNBTHelper.removeEntry(instance, TAG_SOULBIND_UUID);
                        }
                    }
                    return null;
                }

                @Override
                public void tickBinding(Player player) {
                    if (tick != null)
                        tick.tick(instance, this, player);
                }

                @Override
                public boolean isRightPlayer(Player player) {
                    return player.getUUID().equals(getSoulbindUUID());
                }

                @Override
                public @Nullable ResourceLocation getAdvancement() {
                    return advancement;
                }

                @Override
                public boolean shouldDamageWrongPlayer() {
                    return shouldDamageWrongPlayer;
                }
            };
        }

        @Override
        @HideFromJS
        public Capability<Relic> getCapabilityKey() {
            return BotaniaForgeCapabilities.RELIC;
        }

        @Override
        @HideFromJS
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:relic_custom_botania");
        }
    }
}
