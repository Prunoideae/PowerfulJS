package com.prunoideae;

import com.prunoideae.custom.enchantment.EnchantmentBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;

public class PowerfulJSPlugin extends KubeJSPlugin {
    public static final RegistryObjectBuilderTypes<Enchantment> ENCHANTMENT = RegistryObjectBuilderTypes.add(Registry.ENCHANTMENT_REGISTRY, Enchantment.class, "basic");

    @Override
    public void init() {
        ENCHANTMENT.addType("basic", EnchantmentBuilder.class, EnchantmentBuilder::new);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("EnchantmentRarity", Enchantment.Rarity.class);
        event.add("MobType", MobType.class);
    }
}
