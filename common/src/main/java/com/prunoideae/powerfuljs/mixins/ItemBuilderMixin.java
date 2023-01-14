package com.prunoideae.powerfuljs.mixins;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import com.prunoideae.powerfuljs.CapabilityService;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemBuilder.class, remap = false)
public abstract class ItemBuilderMixin {
    public ItemBuilder attachCapability(CapabilityBuilder<ItemStack, ?, ?> builder) {
        ItemBuilder thisBuilder = (ItemBuilder) (Object) this;
        CapabilityService.INSTANCE.addBuilderCapability(thisBuilder, builder);
        return thisBuilder;
    }
}
