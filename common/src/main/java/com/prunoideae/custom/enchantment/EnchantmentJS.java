package com.prunoideae.custom.enchantment;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EnchantmentJS extends Enchantment {

    private final EnchantmentProperty property;

    public EnchantmentJS(EnchantmentBuilder builder) {
        super(builder.getRarity(), builder.getCategory(), builder.getEquipmentSlots().toArray(new EquipmentSlot[0]));
        property = builder.getProperties();
    }

    @Override
    public boolean isCurse() {
        return property.isCurse();
    }

    @Override
    public boolean isDiscoverable() {
        return property.isDiscoverable();
    }

    @Override
    public boolean isTradeable() {
        return property.isTradeable();
    }

    @Override
    public boolean isTreasureOnly() {
        return property.isTreasureOnly();
    }

    @Override
    public int getMaxLevel() {
        return property.getMaxLevel();
    }

    @Override
    public int getMinLevel() {
        return property.getMinLevel();
    }

    @Override
    public int getMaxCost(int i) {
        return property.getMaxCost() != null ? property.getMaxCost() : super.getMaxCost(i);
    }

    @Override
    public int getMinCost(int i) {
        return property.getMinCost() != null ? property.getMinCost() : super.getMinCost(i);
    }

    @Override
    public void doPostAttack(@NotNull LivingEntity livingEntity, @NotNull Entity entity, int i) {
        LevelJS level = UtilsJS.getLevel(livingEntity.level);
        property.getPostAttackCallback().accept(new EnchantmentBuilder.AttackInfo(level.getLivingEntity(livingEntity), level.getEntity(entity), i));
    }

    @Override
    public void doPostHurt(@NotNull LivingEntity livingEntity, @NotNull Entity entity, int i) {
        LevelJS level = UtilsJS.getLevel(livingEntity.level);
        property.getPostHurtCallback().accept(new EnchantmentBuilder.AttackInfo(level.getLivingEntity(livingEntity), level.getEntity(entity), i));
    }

    @Override
    public float getDamageBonus(int i, @NotNull MobType mobType) {
        return property.getDamageBonusCallback().apply(i, mobType);
    }

    @Override
    public int getDamageProtection(int i, @NotNull DamageSource damageSource) {
        return property.getDamageProtectionCallback().apply(i, damageSource);
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return property.getIsCompatibleWith().test(enchantment);
    }
}
