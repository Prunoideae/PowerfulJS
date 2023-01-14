package com.prunoideae.powerfuljs.fabric;

import com.prunoideae.powerfuljs.PowerfulJS;
import net.fabricmc.api.ModInitializer;

public class PowerfulJSFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PowerfulJS.init();
    }
}