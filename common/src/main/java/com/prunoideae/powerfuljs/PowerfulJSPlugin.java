package com.prunoideae.powerfuljs;

import com.prunoideae.powerfuljs.events.BlockEntityCapEventJS;
import com.prunoideae.powerfuljs.events.EntityCapEventJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class PowerfulJSPlugin extends KubeJSPlugin {

    //Only this event here so no need to make container classes
    EventGroup GROUP = EventGroup.of("CapabilityEvents");

    EventHandler REGISTER_BE_CAP = GROUP.startup("blockEntity", () -> BlockEntityCapEventJS.class);
    EventHandler REGISTER_ENTITY_CAP = GROUP.startup("entity", () -> EntityCapEventJS.class);

    @Override
    public void registerEvents() {
        GROUP.register();
    }

    @Override
    public void afterInit() {
        CapabilityService.INSTANCE.loadBuilders();
        REGISTER_BE_CAP.post(new BlockEntityCapEventJS());
        REGISTER_ENTITY_CAP.post(new EntityCapEventJS());
    }
}
