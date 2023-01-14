package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import com.prunoideae.powerfuljs.CapabilityService;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PowerfulJSEvents {

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack object = event.getObject();
        CapabilityService.INSTANCE.getCapabilitiesFor(object).ifPresent(builders -> {
            for (CapabilityBuilder<ItemStack, ?, ?> builder : builders) {
                event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object)));
            }
        });
    }

    @SubscribeEvent
    public static void attachBECapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity object = event.getObject();
        CapabilityService.INSTANCE.getCapabilitiesFor(object).ifPresent(builders -> {
            for (CapabilityBuilder<BlockEntity, ?, ?> builder : builders) {
                event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object)));
            }
        });
    }
}
