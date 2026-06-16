package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

public class LivingFallEventHandler {

  public static void onLivingFall(LivingFallEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }
    if (GemType.getHeldGem(player) == GemType.ZIRCON) {
      event.setCanceled(true);
    }
  }
}
