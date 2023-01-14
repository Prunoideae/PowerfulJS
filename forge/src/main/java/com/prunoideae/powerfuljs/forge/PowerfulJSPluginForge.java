package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.PowerfulJSPlugin;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilitiesForge;
import com.prunoideae.powerfuljs.capabilities.forge.mods.botania.CapabilitiesBotania;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import vazkii.botania.api.BotaniaForgeCapabilities;

public class PowerfulJSPluginForge extends PowerfulJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("CapabilityBuilder", CapabilitiesForge.class);
        event.add("ForgeCapabilities", ForgeCapabilities.class);
        if (Platform.isModLoaded("botania")) {
            event.add("BotaniaCapabilityBuilder", CapabilitiesBotania.class);
            event.add("BotaniaCapabilities", BotaniaForgeCapabilities.class);
        }
    }
}
