package com.prunoideae.powerfuljs.capabilities.forge.mods.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Consumer;

public class RendererCurios implements ICurioRenderer {
    public RendererCurios(Consumer<RenderContext> renderer) {
        this.renderer = renderer;
    }

    public static class RenderContext {
        public final ItemStack stack;
        public final SlotContext slotContext;
        public final PoseStack matrixStack;
        public final RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent;
        public final MultiBufferSource renderTypeBuffer;
        public final int light;
        public final float limbSwing;
        public final float limbSwingAmount;
        public final float partialTicks;
        public final float ageInTicks;
        public final float netHeadYaw;
        public final float netHeadPitch;

        public RenderContext(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float netHeadPitch) {
            this.stack = stack;
            this.slotContext = slotContext;
            this.matrixStack = matrixStack;
            this.renderLayerParent = renderLayerParent;
            this.renderTypeBuffer = renderTypeBuffer;
            this.light = light;
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.partialTicks = partialTicks;
            this.ageInTicks = ageInTicks;
            this.netHeadYaw = netHeadYaw;
            this.netHeadPitch = netHeadPitch;
        }
    }

    private final Consumer<RenderContext> renderer;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        renderer.accept(new RenderContext(stack, slotContext, matrixStack, (RenderLayerParent<LivingEntity, EntityModel<LivingEntity>>) renderLayerParent, renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch));
    }
}
