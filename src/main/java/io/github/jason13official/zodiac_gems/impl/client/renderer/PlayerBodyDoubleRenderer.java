package io.github.jason13official.zodiac_gems.impl.client.renderer;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.client.model.PlayerBodyDoubleModel;
import io.github.jason13official.zodiac_gems.impl.common.entity.PlayerBodyDouble;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlayerBodyDoubleRenderer extends HumanoidMobRenderer<PlayerBodyDouble, PlayerBodyDoubleModel> {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ZodiacGems.identifier("body_double"), "main");

  public PlayerBodyDoubleRenderer(Context pContext) {
    super(pContext, new PlayerBodyDoubleModel(pContext.bakeLayer(LAYER_LOCATION)), 0.7f);
  }

  @Override
  public ResourceLocation getTextureLocation(PlayerBodyDouble pEntity) {
    return pEntity.getAttachedClientPlayer().getSkin().texture();
  }
}
