package com.prunoideae.fabric;

import com.prunoideae.PowerfulJS;
import net.fabricmc.api.ModInitializer;

public class PowerfulJSFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PowerfulJS.init();
    }
}
