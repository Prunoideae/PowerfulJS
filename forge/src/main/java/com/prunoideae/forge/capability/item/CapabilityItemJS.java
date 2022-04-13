package com.prunoideae.forge.capability.item;

import com.prunoideae.forge.capability.CapabilitiesProvider;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CapabilityItemJS extends BasicItemJS {
    private final List<CapabilityItemBuilder.InitCapabilityCallback> onInitCaps;
    private final CapabilityItemBuilder.HoverTextCallback appendHoverText;
    private final CapabilityItemBuilder.TooltipImageCallback getTooltipImage;

    public CapabilityItemJS(Builder p) {
        super(p);
        onInitCaps = p.onInitCapabilities;
        appendHoverText = p.appendHoverText;
        getTooltipImage = p.getTooltipImage;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        List<ICapabilityProvider> inited = onInitCaps.stream()
                .map(callback -> callback.apply(ItemStackJS.of(stack), nbt))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return inited.isEmpty() ? null : new CapabilitiesProvider(inited);
    }

    @Override
    public void appendHoverText(ItemStack arg, @Nullable Level arg2, List<Component> list, TooltipFlag arg3) {
        if (appendHoverText == null)
            super.appendHoverText(arg, arg2, list, arg3);
        else
            appendHoverText.appendHoverText(arg, arg2, list, arg3);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack arg) {
        return getTooltipImage == null ? super.getTooltipImage(arg) : getTooltipImage.getTooltipImage(arg);
    }

    public static class Builder extends CapabilityItemBuilder {

        public Builder(ResourceLocation i) {
            super(i);
        }

        @Override
        public Item createObject() {
            return new CapabilityItemJS(this);
        }
    }
}
