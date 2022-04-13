package com.prunoideae.forge.capability.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CapabilityItemBuilder extends ItemBuilder {

    @FunctionalInterface
    public interface InitCapabilityCallback {
        ICapabilityProvider apply(ItemStackJS stack, @Nullable CompoundTag nbt);
    }

    @FunctionalInterface
    public interface HoverTextCallback {
        void appendHoverText(ItemStack arg, @Nullable Level arg2, List<Component> list, TooltipFlag arg3);
    }

    @FunctionalInterface
    public interface TooltipImageCallback {
        Optional<TooltipComponent> getTooltipImage(ItemStack arg);
    }

    public transient List<InitCapabilityCallback> onInitCapabilities = new ArrayList<>();
    public transient HoverTextCallback appendHoverText = null;
    public transient TooltipImageCallback getTooltipImage = null;

    public CapabilityItemBuilder(ResourceLocation i) {
        super(i);
    }

    public CapabilityItemBuilder addCapability(InitCapabilityCallback onInitCapabilities) {
        this.onInitCapabilities.add(onInitCapabilities);
        return this;
    }

    public CapabilityItemBuilder appendHoverText(HoverTextCallback appendHoverText) {
        this.appendHoverText = appendHoverText;
        return this;
    }

    public CapabilityItemBuilder getTooltipImage(TooltipImageCallback getTooltipImage) {
        this.getTooltipImage = getTooltipImage;
        return this;
    }
}
