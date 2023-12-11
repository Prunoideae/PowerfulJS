package com.prunoideae.powerfuljs.capabilities.forge.mods.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class CapabilityCurios {
    @FunctionalInterface
    public interface EquipCallback {
        void changed(ItemStack instance, SlotContext slotContext, ItemStack stack);
    }

    @FunctionalInterface
    public interface ShouldDrop {
        boolean test(ItemStack instance, SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit);
    }

    public static class AttributeModificationContext {
        private final ItemStack stack;
        private final SlotContext context;
        private final UUID uuid;

        private final Multimap<Attribute, AttributeModifier> modifiers;

        public AttributeModificationContext(ItemStack stack, SlotContext context, UUID uuid, Multimap<Attribute, AttributeModifier> modifiers) {
            this.stack = stack;
            this.context = context;
            this.uuid = uuid;
            this.modifiers = modifiers;
        }

        public ItemStack getStack() {
            return stack;
        }

        public SlotContext getContext() {
            return context;
        }

        public UUID getUUID() {
            return uuid;
        }

        public AttributeModificationContext modify(Attribute attribute, String identifier, double amount, AttributeModifier.Operation operation) {
            modifiers.put(attribute, new AttributeModifier(new UUID(identifier.hashCode(), identifier.hashCode()), identifier, amount, operation));
            return this;
        }

        public AttributeModificationContext remove(Attribute attribute, String identifier) {
            modifiers.get(attribute).removeIf(modifier -> modifier.getName().equals(identifier));
            return this;
        }

        @HideFromJS
        public Multimap<Attribute, AttributeModifier> getModifiers() {
            return modifiers;
        }
    }

    public ItemStackBuilder itemStack() {
        return new ItemStackBuilder();
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, ICurio> {
        private BiConsumer<ItemStack, SlotContext> curioTick;
        private EquipCallback onEquip;
        private EquipCallback onUnequip;
        private BiPredicate<ItemStack, SlotContext> canEquip;
        private BiPredicate<ItemStack, SlotContext> canUnequip;
        private ShouldDrop shouldDrop;

        public ItemStackBuilder curioTick(BiConsumer<ItemStack, SlotContext> curioTick) {
            this.curioTick = curioTick;
            return this;
        }

        public ItemStackBuilder onEquip(EquipCallback onEquip) {
            this.onEquip = onEquip;
            return this;
        }

        public ItemStackBuilder onUnequip(EquipCallback onUnequip) {
            this.onUnequip = onUnequip;
            return this;
        }

        public ItemStackBuilder canEquip(BiPredicate<ItemStack, SlotContext> canEquip) {
            this.canEquip = canEquip;
            return this;
        }

        public ItemStackBuilder canUnequip(BiPredicate<ItemStack, SlotContext> canUnequip) {
            this.canUnequip = canUnequip;
            return this;
        }

        public ItemStackBuilder getDropRule(ShouldDrop getDropRule) {
            this.shouldDrop = getDropRule;
            return this;
        }

        private final Multimap<ResourceLocation, AttributeModifier> modifiers = HashMultimap.create();
        private final Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        private Consumer<AttributeModificationContext> dynamicAttribute = null;
        private boolean attributeInitialized = false;

        public ItemStackBuilder modifyAttribute(ResourceLocation attribute, String identifier, double d, AttributeModifier.Operation operation) {
            modifiers.put(attribute, new AttributeModifier(new UUID(identifier.hashCode(), identifier.hashCode()), identifier, d, operation));
            return this;
        }

        public ItemStackBuilder dynamicAttribute(Consumer<AttributeModificationContext> context) {
            dynamicAttribute = context;
            return this;
        }

        @Override
        public ICurio getCapability(ItemStack instance) {
            return new ICurio() {
                @Override
                public ItemStack getStack() {
                    return instance;
                }

                @Override
                public void curioTick(SlotContext slotContext) {
                    if (curioTick != null)
                        curioTick.accept(instance, slotContext);
                    else
                        ICurio.super.curioTick(slotContext);
                }

                @Override
                public void onEquip(SlotContext slotContext, ItemStack prevStack) {
                    if (onEquip != null)
                        onEquip.changed(instance, slotContext, prevStack);
                    else
                        ICurio.super.onEquip(slotContext, prevStack);
                }

                @Override
                public void onUnequip(SlotContext slotContext, ItemStack newStack) {
                    if (onUnequip != null)
                        onUnequip.changed(instance, slotContext, newStack);
                    else
                        ICurio.super.onUnequip(slotContext, newStack);
                }

                @Override
                public boolean canEquip(SlotContext slotContext) {
                    if (canEquip != null)
                        return canEquip.test(instance, slotContext);
                    else
                        return ICurio.super.canEquip(slotContext);
                }

                @Override
                public boolean canUnequip(SlotContext slotContext) {
                    if (canUnequip != null)
                        return canUnequip.test(instance, slotContext);
                    return ICurio.super.canUnequip(slotContext);
                }

                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
                    if (!attributeInitialized) {
                        for (Map.Entry<ResourceLocation, AttributeModifier> entry : modifiers.entries()) {
                            ResourceLocation key = entry.getKey();
                            AttributeModifier modifier = entry.getValue();
                            attributes.put(RegistryInfo.ATTRIBUTE.getValue(key), modifier);
                        }
                        attributeInitialized = true;
                    }

                    if (dynamicAttribute != null) {
                        Multimap<Attribute, AttributeModifier> attributeCopy = HashMultimap.create(attributes);
                        dynamicAttribute.accept(new AttributeModificationContext(instance, slotContext, uuid, attributeCopy));
                        return attributeCopy;
                    }

                    return attributes;
                }

                @NotNull
                @Override
                public DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
                    if (shouldDrop != null)
                        return shouldDrop.test(instance, slotContext, source, lootingLevel, recentlyHit) ? DropRule.ALWAYS_DROP : DropRule.ALWAYS_KEEP;
                    return DropRule.DEFAULT;
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }
            };
        }

        @Override
        public Capability<ICurio> getCapabilityKey() {
            return CuriosCapability.ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return CuriosCapability.ID_ITEM;
        }
    }
}
