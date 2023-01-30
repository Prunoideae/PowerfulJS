package com.prunoideae.powerfuljs.capabilities.forge.mods.curios;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface EventCurios {
    EventGroup GROUP = EventGroup.of("CuriosEvents");
    EventHandler REGISTER_RENDERER = GROUP.client("registerRenderer", () -> RegisterCuriosRendererEventJS.class);
}
