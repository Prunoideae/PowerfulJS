package com.prunoideae.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MobEffectJS extends MobEffect {
    private final MobEffectProperty property;

    protected MobEffectJS(MobEffectProperty property) {
        super(property.getCategory(), property.getColor());
        this.property = property;
        this.getAttributeModifiers().putAll(property.getAttributeModifiers());
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int i) {
        property.getEffectTickCallback().accept(livingEntity, i);
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return property.getTickEffectPredicate().test(i, j);
    }

    @Override
    public boolean isInstantenous() {
        return property.isInstantaneous();
    }

}
