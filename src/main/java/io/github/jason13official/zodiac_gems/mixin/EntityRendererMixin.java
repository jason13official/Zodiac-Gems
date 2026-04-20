package io.github.jason13official.zodiac_gems.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

  @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
  private void zodiac_gems$checkNametag(T pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, float pPartialTick, CallbackInfo ci) {
    if (ZodiacGemsClient.HIDDEN_NAMETAGS.contains(pEntity.getUUID())) {
      ci.cancel();
    }
  }
}
