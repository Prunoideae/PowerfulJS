package com.prunoideae.custom.enchantment;

import com.prunoideae.PowerfulJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.core.LivingEntityKJS;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.entity.LivingEntityJS;
import dev.latvian.mods.kubejs.util.BuilderBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EnchantmentBuilder extends BuilderBase<Enchantment> {

    public static final class AttackInfo {
        public final LivingEntityJS attacker;
        public final EntityJS victim;
        public final int i;

        public AttackInfo(LivingEntityJS attacker, EntityJS victim, int i) {
            this.attacker = attacker;
            this.victim = victim;
            this.i = i;
        }
    }

    private Enchantment.Rarity rarity;
    private EnchantmentCategory category;
    private List<EquipmentSlot> equipmentSlots;
    private boolean tresureOnly;
    private boolean curse;
    private boolean tradeable;
    private boolean discoverable;
    private int minLevel;
    private int maxLevel;
    private Integer minCost;
    private Integer maxCost;
    private BiFunction<Integer, DamageSource, Integer> damageProtectionCallback;
    private BiFunction<Integer, MobType, Float> damageBonusCallback;
    private Consumer<AttackInfo> postAttackCallback;
    private Consumer<AttackInfo> postHurtCallback;
    private Predicate<Enchantment> isCompatibleWith;

    public EnchantmentBuilder(ResourceLocation i) {
        super(i);
        rarity = Enchantment.Rarity.COMMON;
        category = EnchantmentCategory.BREAKABLE;
        equipmentSlots = new ArrayList<>();
        tresureOnly = false;
        curse = false;
        tradeable = true;
        discoverable = true;
        minCost = null;
        maxCost = null;
        minLevel = 1;
        maxLevel = 1;

        damageProtectionCallback = (l, d) -> 0;
        damageBonusCallback = (l, d) -> 0f;
        postAttackCallback = a -> {
        };
        postHurtCallback = a -> {
        };
        isCompatibleWith = e -> true;
    }

    @Override
    public RegistryObjectBuilderTypes<Enchantment> getRegistryType() {
        return PowerfulJSPlugin.ENCHANTMENT;
    }

    @Override
    public Enchantment createObject() {
        return new EnchantmentJS(this);
    }

    public EnchantmentBuilder category(EnchantmentCategory category) {
        this.category = category;
        return this;
    }

    public EnchantmentBuilder equipmentSlots(List<EquipmentSlot> equipmentSlots) {
        this.equipmentSlots = equipmentSlots;
        return this;
    }

    public EnchantmentBuilder rarity(Enchantment.Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public EnchantmentBuilder curse(boolean curse) {
        this.curse = curse;
        return this;
    }

    public EnchantmentBuilder discoverable(boolean discoverable) {
        this.discoverable = discoverable;
        return this;
    }

    public EnchantmentBuilder tradeable(boolean tradeable) {
        this.tradeable = tradeable;
        return this;
    }

    public EnchantmentBuilder treasureOnly(boolean treasureOnly) {
        this.tresureOnly = treasureOnly;
        return this;
    }

    public EnchantmentBuilder minCost(Integer minCost) {
        this.minCost = minCost;
        return this;
    }

    public EnchantmentBuilder maxCost(Integer maxCost) {
        this.maxCost = maxCost;
        return this;
    }

    public EnchantmentBuilder minLevel(int minLevel) {
        this.minLevel = minLevel;
        return this;
    }

    public EnchantmentBuilder maxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    public EnchantmentBuilder damageBonusOf(BiFunction<Integer, MobType, Float> damageBonusCallback) {
        this.damageBonusCallback = damageBonusCallback;
        return this;
    }

    public EnchantmentBuilder damageProtectionOf(BiFunction<Integer, DamageSource, Integer> damageProtectionCallback) {
        this.damageProtectionCallback = damageProtectionCallback;
        return this;
    }

    public EnchantmentBuilder onPostAttack(Consumer<AttackInfo> postAttackCallback) {
        this.postAttackCallback = postAttackCallback;
        return this;
    }

    public EnchantmentBuilder onPostHurt(Consumer<AttackInfo> postHurtCallback) {
        this.postHurtCallback = postHurtCallback;
        return this;
    }

    public EnchantmentBuilder compatibility(Predicate<Enchantment> isCompatibleWith) {
        this.isCompatibleWith = isCompatibleWith;
        return this;
    }

    public EnchantmentProperty getProperties() {
        return new EnchantmentProperty(rarity, category, equipmentSlots, tresureOnly, curse, tradeable, discoverable, minLevel, maxLevel, minCost, maxCost, damageProtectionCallback, damageBonusCallback, postAttackCallback, postHurtCallback, isCompatibleWith);
    }

    public EnchantmentCategory getCategory() {
        return category;
    }

    public List<EquipmentSlot> getEquipmentSlots() {
        return equipmentSlots;
    }

    public Enchantment.Rarity getRarity() {
        return rarity;
    }
}


