package com.prunoideae.powerfuljs.capabilities.forge.mods.botania.mana;

public class CapabilityMana {
    public ManaItemJS.Builder itemStack(int capacity) {
        return new ManaItemJS.Builder(capacity);
    }

    public ManaHandlerJS.Builder blockEntity(int capacity) {
        return new ManaHandlerJS.Builder(capacity);
    }

}
