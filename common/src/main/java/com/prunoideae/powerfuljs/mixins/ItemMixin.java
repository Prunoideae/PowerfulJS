package com.prunoideae.powerfuljs.mixins;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import com.prunoideae.powerfuljs.CapabilityService;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin {

    public void attachCapability(CapabilityBuilder<ItemStack, ?, ?> capabilityBuilder) {
        CapabilityService.INSTANCE.addItemCapability((Item) (Object) this, capabilityBuilder);
    }

}
