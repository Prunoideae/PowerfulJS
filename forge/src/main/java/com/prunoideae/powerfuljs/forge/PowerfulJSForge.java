package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.PowerfulJS;
import com.prunoideae.powerfuljs.extras.create.CreateEvents;
import com.simibubi.create.api.event.TileEntityBehaviourEvent;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mod(PowerfulJS.MOD_ID)
public class PowerfulJSForge {

    public PowerfulJSForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PowerfulJS.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PowerfulJSEvents::registerDummyBEs);
        PowerfulJS.init();
        if (ModList.get().isLoaded("create")) {
            if (MinecraftForge.EVENT_BUS instanceof EventBus eventBus) {
                try {
                    /*
                     * Unable to use AccessWidener or Mixin here.
                     * Only reflection works, good is that it's not Minecraft code, and should not likely to change fast.
                     */
                    Method addListener = EventBus.class.getDeclaredMethod("addListener", EventPriority.class, Predicate.class, Consumer.class);
                    addListener.setAccessible(true);
                    addListener.invoke(eventBus, EventPriority.NORMAL, ((Predicate<TileEntityBehaviourEvent<?>>) event -> true), ((Consumer<TileEntityBehaviourEvent<?>>) CreateEvents::modifyBehaviour));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    PowerfulJS.LOGGER.error("No target addListener found in EventBus!");
                }
            } else {
                PowerfulJS.LOGGER.error("Forge has changed event bus implementation! Please report to Github!");
            }
        }
    }
}