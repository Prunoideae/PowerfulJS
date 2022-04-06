package com.prunoideae;

import com.prunoideae.custom.MobEffectBuilder;
import com.prunoideae.custom.enchantment.EnchantmentBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PowerfulJSPlugin extends KubeJSPlugin {
    public static final RegistryObjectBuilderTypes<Enchantment> ENCHANTMENT = RegistryObjectBuilderTypes.add(Registry.ENCHANTMENT_REGISTRY, Enchantment.class, "basic");
    public static final RegistryObjectBuilderTypes<MobEffect> MOB_EFFECT = RegistryObjectBuilderTypes.add(Registry.MOB_EFFECT_REGISTRY, MobEffect.class, "basic");

    @Override
    public void init() {
        ENCHANTMENT.addType("basic", EnchantmentBuilder.class, EnchantmentBuilder::new);
        MOB_EFFECT.addType("basic", MobEffectBuilder.class, MobEffectBuilder::new);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("EnchantmentRarity", Enchantment.Rarity.class);
        event.add("EnchantmentCategory", EnchantmentCategory.class);
        event.add("MobType", MobType.class);
        event.add("ModifyOperation", AttributeModifier.Operation.class);
        event.add("Attributes", Attributes.class);
        event.add("EffectCategory", MobEffectCategory.class);
    }
}
