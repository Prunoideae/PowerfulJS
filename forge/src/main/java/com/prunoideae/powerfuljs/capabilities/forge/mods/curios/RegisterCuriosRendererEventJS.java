package com.prunoideae.powerfuljs.capabilities.forge.mods.curios;

import com.prunoideae.powerfuljs.PowerfulJS;
import dev.latvian.mods.kubejs.client.ClientEventJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.function.Consumer;

public class RegisterCuriosRendererEventJS extends EventJS {
    public void register(Item item, Consumer<RendererCurios.RenderContext> renderer) {
        PowerfulJS.PROXY.get().runOnClient(() -> CuriosRendererRegistry.register(item, () -> new RendererCurios(renderer)));
    }
}
