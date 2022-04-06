package com.prunoideae.custom.enchantment;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EnchantmentProperty {
    private final Enchantment.Rarity rarity;
    private final EnchantmentCategory category;
    private final List<EquipmentSlot> equipmentSlots;
    private final boolean treasureOnly;
    private final boolean curse;
    private final boolean tradeable;
    private final boolean discoverable;
    private final int minLevel;
    private final int maxLevel;
    private final Integer minCost;
    private final Integer maxCost;
    private final BiFunction<Integer, DamageSource, Integer> damageProtectionCallback;
    private final BiFunction<Integer, MobType, Float> damageBonusCallback;
    private final Consumer<EnchantmentBuilder.AttackInfo> postAttackCallback;
    private final Consumer<EnchantmentBuilder.AttackInfo> postHurtCallback;
    private final Predicate<Enchantment> isCompatibleWith;

    public EnchantmentProperty(Enchantment.Rarity rarity, EnchantmentCategory category, List<EquipmentSlot> equipmentSlots, boolean treasureOnly, boolean curse, boolean tradeable, boolean discoverable, int minLevel, int maxLevel, Integer minCost, Integer maxCost, BiFunction<Integer, DamageSource, Integer> damageProtectionCallback, BiFunction<Integer, MobType, Float> damageBonusCallback, Consumer<EnchantmentBuilder.AttackInfo> postAttackCallback, Consumer<EnchantmentBuilder.AttackInfo> postHurtCallback, Predicate<Enchantment> isCompatibleWith) {
        this.rarity = rarity;
        this.category = category;
        this.equipmentSlots = equipmentSlots;
        this.treasureOnly = treasureOnly;
        this.curse = curse;
        this.tradeable = tradeable;
        this.discoverable = discoverable;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.damageProtectionCallback = damageProtectionCallback;
        this.damageBonusCallback = damageBonusCallback;
        this.postAttackCallback = postAttackCallback;
        this.postHurtCallback = postHurtCallback;
        this.isCompatibleWith = isCompatibleWith;
    }

    public boolean isCurse() {
        return curse;
    }

    public boolean isDiscoverable() {
        return discoverable;
    }

    public boolean isTradeable() {
        return tradeable;
    }

    public boolean isTreasureOnly() {
        return treasureOnly;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    public Integer getMinCost() {
        return minCost;
    }

    public Enchantment.Rarity getRarity() {
        return rarity;
    }

    public List<EquipmentSlot> getEquipmentSlots() {
        return equipmentSlots;
    }

    public EnchantmentCategory getCategory() {
        return category;
    }

    public BiFunction<Integer, DamageSource, Integer> getDamageProtectionCallback() {
        return damageProtectionCallback;
    }

    public BiFunction<Integer, MobType, Float> getDamageBonusCallback() {
        return damageBonusCallback;
    }

    public Consumer<EnchantmentBuilder.AttackInfo> getPostAttackCallback() {
        return postAttackCallback;
    }

    public Consumer<EnchantmentBuilder.AttackInfo> getPostHurtCallback() {
        return postHurtCallback;
    }

    public Predicate<Enchantment> getIsCompatibleWith() {
        return isCompatibleWith;
    }
}
