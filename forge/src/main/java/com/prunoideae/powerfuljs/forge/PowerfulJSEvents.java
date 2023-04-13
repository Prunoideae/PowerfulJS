package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.CapabilityBuilder;
import com.prunoideae.powerfuljs.CapabilityService;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityProvider;
import com.prunoideae.powerfuljs.custom.BlockDummyEntityJS;
import com.prunoideae.powerfuljs.custom.BlockEntityDummy;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber
public class PowerfulJSEvents {

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack object = event.getObject();
        CapabilityService.INSTANCE.getCapabilitiesFor(object).ifPresent(builders -> {
            for (CapabilityBuilder<ItemStack, ?, ?> builder : builders) {
                event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object), side -> builder.getDirection().test(object, side)));
            }
        });
        CapabilityServiceForge.INSTANCE.getCapabilitiesFor(object).forEach(builder -> {
            event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object), side -> builder.getDirection().test(object, side)));
        });
    }

    @SubscribeEvent
    public static void attachBECapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity object = event.getObject();
        CapabilityService.INSTANCE.getCapabilitiesFor(object).ifPresent(builders -> {
            for (CapabilityBuilder<BlockEntity, ?, ?> builder : builders) {
                event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object), side -> builder.getDirection().test(object, side)));
            }
        });
        CapabilityServiceForge.INSTANCE.getCapabilitiesFor(object).forEach(builder -> {
            event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object), side -> builder.getDirection().test(object, side)));
        });
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity object = event.getObject();
        CapabilityService.INSTANCE.getCapabilitiesFor(object).ifPresent(builders -> {
            for (CapabilityBuilder<Entity, ?, ?> builder : builders) {
                event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object), side -> builder.getDirection().test(object, side)));
            }
        });
        CapabilityServiceForge.INSTANCE.getCapabilitiesFor(object).forEach(builder -> {
            event.addCapability(builder.getResourceLocation(), CapabilityProvider.of(builder.getCapabilityKey(), builder.getCapability(object), side -> builder.getDirection().test(object, side)));
        });
    }

    public static void registerDummyBEs(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
            for (BlockDummyEntityJS.Builder block : BlockEntityDummy.BLOCKS) {
                var blockEntityType = BlockEntityType.Builder.of(BlockEntityDummy::new, block.get()).build(null);
                BlockEntityDummy.BLOCK_ENTITY_TYPES.put(block.id, blockEntityType);
                helper.register(block.id, blockEntityType);
                for (CapabilityBuilder<BlockEntity, ?, ?> capabilityBuilder : block.capabilityBuilders) {
                    CapabilityService.INSTANCE.addBECapability(blockEntityType, capabilityBuilder);
                }
            }
        });
    }
}
