package com.prunoideae.powerfuljs.forge;

import com.prunoideae.powerfuljs.PowerfulJS;
import com.prunoideae.powerfuljs.PowerfulJSPlugin;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilitiesForge;
import com.prunoideae.powerfuljs.capabilities.forge.mods.botania.CapabilitiesBotania;
import com.prunoideae.powerfuljs.capabilities.forge.mods.curios.CapabilitiesCurios;
import com.prunoideae.powerfuljs.capabilities.forge.mods.curios.EventCurios;
import com.prunoideae.powerfuljs.capabilities.forge.mods.curios.RegisterCuriosRendererEventJS;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.CapabilitiesMekanism;
import com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.MekanismHelper;
import com.prunoideae.powerfuljs.capabilities.forge.mods.pnc.CapabilitiesPneumatic;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import mekanism.common.capabilities.Capabilities;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.client.ICurioRenderer;
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
            PowerfulJS.PROXY.runOnClient(() -> event.add("CuriosRenderer", ICurioRenderer.class));
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(FluidStack.class, o -> FluidStackHooksForge.toForge(FluidStackJS.of(o).getFluidStack()));
        if (Platform.isModLoaded("mekanism")) {

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
        if (Platform.isModLoaded("curios")) {
            PowerfulJS.PROXY.runOnClient(() -> EventCurios.REGISTER_RENDERER.post(new RegisterCuriosRendererEventJS()));
        }
    }
}
