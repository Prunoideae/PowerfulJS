package com.prunoideae.custom;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class MobEffectProperty {
    private final MobEffectCategory category;
    private final int color;
    private final boolean instantaneous;
    private final Map<Attribute, AttributeModifier> attributeModifiers;

    private final BiConsumer<LivingEntity, Integer> effectTickCallback;
    private final BiPredicate<Integer, Integer> tickEffectPredicate;


    public MobEffectProperty(MobEffectCategory category, int color, boolean instantaneous, Map<Attribute, AttributeModifier> attributeModifiers, BiConsumer<LivingEntity, Integer> effectTickCallback, BiPredicate<Integer, Integer> tickEffectPredicate) {
        this.category = category;
        this.color = color;
        this.instantaneous = instantaneous;
        this.attributeModifiers = attributeModifiers;
        this.effectTickCallback = effectTickCallback;
        this.tickEffectPredicate = tickEffectPredicate;
    }

    public BiConsumer<LivingEntity, Integer> getEffectTickCallback() {
        return effectTickCallback;
    }

    public BiPredicate<Integer, Integer> getTickEffectPredicate() {
        return tickEffectPredicate;
    }

    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public boolean isInstantaneous() {
        return instantaneous;
    }

    public int getColor() {
        return color;
    }

    public MobEffectCategory getCategory() {
        return category;
    }
}
