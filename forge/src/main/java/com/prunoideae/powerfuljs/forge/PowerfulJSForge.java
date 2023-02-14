package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.PowerfulJS;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PowerfulJS.MOD_ID)
public class PowerfulJSForge {
    public PowerfulJSForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PowerfulJS.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PowerfulJSEvents::registerDummyBEs);
        PowerfulJS.init();
    }
}