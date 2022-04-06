package com.prunoideae.custom;

import com.prunoideae.PowerfulJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.util.BuilderBase;
import dev.latvian.mods.rhino.mod.util.color.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class MobEffectBuilder extends BuilderBase<MobEffect> {
    private MobEffectCategory category;
    private int color;
    private boolean instantaneous;
    private final Map<Attribute, AttributeModifier> attributeModifiers;
    private BiConsumer<LivingEntity, Integer> effectTickCallback;
    private BiPredicate<Integer, Integer> tickEffectPredicate;

    public MobEffectBuilder(ResourceLocation i) {
        super(i);
        category = MobEffectCategory.NEUTRAL;
        color = 0x000000;
        instantaneous = false;
        attributeModifiers = new HashMap<>();
        effectTickCallback = (e, l) -> {
        };
        tickEffectPredicate = (l, d) -> true;
    }

    public MobEffectBuilder category(MobEffectCategory category) {
        this.category = category;
        return this;
    }

    public MobEffectBuilder color(Color color) {
        this.color = color.getRgbKJS();
        return this;
    }

    public MobEffectBuilder instantaneous(boolean instantaneous) {
        this.instantaneous = instantaneous;
        return this;
    }

    public MobEffectBuilder onEffectTick(BiConsumer<LivingEntity, Integer> effectTickCallback) {
        this.effectTickCallback = effectTickCallback;
        return this;
    }

    public MobEffectBuilder checkEffectTick(BiPredicate<Integer, Integer> tickEffectPredicate) {
        this.tickEffectPredicate = tickEffectPredicate;
        return this;
    }

    public MobEffectBuilder addModifier(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
        attributeModifiers.put(attribute, new AttributeModifier(uuid, amount, operation));
        return this;
    }

    @Override
    public RegistryObjectBuilderTypes<MobEffect> getRegistryType() {
        return PowerfulJSPlugin.MOB_EFFECT;
    }

    @Override
    public MobEffect createObject() {
        return new MobEffectJS(new MobEffectProperty(category, color, instantaneous, attributeModifiers, effectTickCallback, tickEffectPredicate));
    }
}
