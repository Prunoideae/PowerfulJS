package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.PowerfulJS;
import com.prunoideae.powerfuljs.PowerfulJSPlugin;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilitiesForge;
import com.prunoideae.powerfuljs.capabilities.forge.mods.botania.CapabilitiesBotania;
import com.prunoideae.powerfuljs.capabilities.forge.mods.curios.CapabilitiesCurios;
import com.prunoideae.powerfuljs.capabilities.forge.mods.curios.EventCurios;
import com.prunoideae.powerfuljs.capabilities.forge.mods.curios.RegisterCuriosRendererEventJS;
import com.prunoideae.powerfuljs.capabilities.forge.mods.immersive.CapabilitiesHelperIE;
import com.prunoideae.powerfuljs.capabilities.forge.mods.immersive.CapabilitiesIE;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.CapabilitiesMekanism;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.MekanismHelper;
import com.prunoideae.powerfuljs.capabilities.forge.mods.pnc.CapabilitiesPneumatic;
import com.prunoideae.powerfuljs.custom.BlockDummyEntityJS;
import com.prunoideae.powerfuljs.events.forge.DynamicBEEventJS;
import com.prunoideae.powerfuljs.events.forge.DynamicEntityEventJS;
import com.prunoideae.powerfuljs.events.forge.DynamicItemStackEventJS;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import mekanism.common.capabilities.Capabilities;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import vazkii.botania.api.BotaniaForgeCapabilities;

public class PowerfulJSPluginForge extends PowerfulJSPlugin {

    public static final EventHandler DYNAMIC_ATTACH_ITEMSTACK_CAP = GROUP.startup("dynamicItem", () -> DynamicItemStackEventJS.class);
    public static final EventHandler DYNAMIC_ATTACH_ENTITY_CAP = GROUP.startup("dynamicEntity", () -> DynamicEntityEventJS.class);
    public static final EventHandler DYNAMIC_ATTACH_BLOCK_ENTITY_CAP = GROUP.startup("dynamicBE", () -> DynamicBEEventJS.class);

    @Override
    public void init() {
        RegistryInfo.BLOCK.addType("powerfuljs:dummy_block_entity", BlockDummyEntityJS.Builder.class, BlockDummyEntityJS.Builder::new);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("CapabilityBuilder", CapabilitiesForge.class);
        event.add("ForgeCapabilities", ForgeCapabilities.class);
        if (Platform.isModLoaded("botania")) {
            event.add("BotaniaCapabilityBuilder", CapabilitiesBotania.class);
            event.add("BotaniaCapabilities", BotaniaForgeCapabilities.class);
        }

        if (Platform.isModLoaded("pneumaticcraft")) {
            event.add("PNCCapabilityBuilder", CapabilitiesPneumatic.class);
            event.add("PNCCapabilities", PNCCapabilities.class);
        }

        if (Platform.isModLoaded("mekanism")) {
            event.add("MekanismCapabilityBuilder", CapabilitiesMekanism.class);
            event.add("MekanismCapabilities", Capabilities.class);
            event.add("MekanismHelper", MekanismHelper.class);
        }
        if (Platform.isModLoaded("curios")) {
            event.add("CuriosCapabilities", CuriosCapability.class);
            event.add("CuriosCapabilityBuilder", CapabilitiesCurios.class);
            PowerfulJS.PROXY.get().runOnClient(() -> event.add("CuriosRenderer", ICurioRenderer.class));
        }
        if (Platform.isModLoaded("immersiveengineering")) {
            event.add("IECapabilityBuilder", CapabilitiesIE.class);
            event.add("IECapabilities", CapabilitiesHelperIE.class);
        }
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        if (Platform.isModLoaded("curios")) {
            EventCurios.GROUP.register();
        }
    }

    @Override
    public void afterInit() {
        super.afterInit();
        DYNAMIC_ATTACH_ITEMSTACK_CAP.post(ScriptType.STARTUP, new DynamicItemStackEventJS());
        DYNAMIC_ATTACH_BLOCK_ENTITY_CAP.post(ScriptType.STARTUP, new DynamicBEEventJS());
        DYNAMIC_ATTACH_ENTITY_CAP.post(ScriptType.STARTUP, new DynamicEntityEventJS());

        if (Platform.isModLoaded("curios")) {
            PowerfulJS.PROXY.get().runOnClient(() -> EventCurios.REGISTER_RENDERER.post(ScriptType.STARTUP, new RegisterCuriosRendererEventJS()));
        }
    }
}
