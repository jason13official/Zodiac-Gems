package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import org.joml.Vector3f;

public class ItemPickupEventHandler {

  public static void onItemPickup(EntityItemPickupEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }
    if (!(player.level() instanceof ServerLevel level)) {
      return;
    }
    if (!(event.getItem().getItem().getItem() instanceof ZodiacGemItem gem)) {
      return;
    }
    float[] c = gem.getGemType().getColor();
    level.sendParticles(
        new DustParticleOptions(new Vector3f(c[0], c[1], c[2]), 1.5f),
        player.getX(), player.getY() + 0.5, player.getZ(),
        20, 0.3, 0.4, 0.3, 0.0
    );
  }
}
