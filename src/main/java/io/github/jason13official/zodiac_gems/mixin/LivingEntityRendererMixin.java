package io.github.jason13official.zodiac_gems.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

  /// dummy
  public LivingEntityRendererMixin(Context pContext) {
    super(pContext);
  }

  @Shadow
  public static int getOverlayCoords(LivingEntity pLivingEntity, float pU) {
    throw new UnsupportedOperationException("Implemented via mixin");
  }

  @Unique
  private static <T extends LivingEntity, M extends EntityModel<T>> void zodiac_gems$tryRender(LivingEntityRenderer<T, M> instance, T pEntity, float pPartialTicks, PoseStack pPoseStack,
      MultiBufferSource pBuffer, int pPackedLight) {

    if (!ZodiacGemsClient.DARKNESS_TRACKER.contains(pEntity.getUUID())) {
      return;
    }

    pPoseStack.pushPose();
    ResourceLocation resourcelocation = instance.getTextureLocation(pEntity);

    // cool effect but doesn't render through walls
    // RenderType rendertype = RenderType.entityTranslucent(resourcelocation, true);

    RenderType rendertype = RenderType.outline(resourcelocation);
    VertexConsumer vertexconsumer = pBuffer.getBuffer(rendertype);
    int i = OverlayTexture.NO_OVERLAY;
    pPoseStack.scale(1.1f, 1.1f, 1.1f); // temporary scaling for testing
    instance.getModel().renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, i, 0x26131313);
    pPoseStack.popPose();
  }

  @SuppressWarnings("unsafe")
  @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
  private void zodiac_gems$render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {

    LivingEntityRenderer<T, M> self = (LivingEntityRenderer<T, M>) (Object) this;

    zodiac_gems$tryRender(self, pEntity, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
  }
}
