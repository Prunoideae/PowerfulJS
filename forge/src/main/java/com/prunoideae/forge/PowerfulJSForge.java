package com.prunoideae.forge;

import dev.architectury.platform.forge.EventBuses;
import com.prunoideae.PowerfulJS;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PowerfulJS.MOD_ID)
public class PowerfulJSForge {
    public PowerfulJSForge() {
        EventBuses.registerModEventBus(PowerfulJS.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        PowerfulJS.init();
    }
}
