package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.mods.botania.mana.CapabilityMana;

public interface CapabilitiesBotania {
    CapabilityMana MANA = new CapabilityMana();
    CapabilityAvatar AVATAR = new CapabilityAvatar();
    CapabilityBlockProvider BLOCK_PROVIDER = new CapabilityBlockProvider();
    CapabilityRelic RELIC = new CapabilityRelic();
    CapabilityExoflame EXOFLAME = new CapabilityExoflame();
}
