package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;

public class LivingChangeTargetEventHandler {

  public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
    if (!(event.getNewTarget() instanceof ServerPlayer player)) return;
    if (GemType.getHeldGem(player) == GemType.MOONSTONE) event.setCanceled(true);
  }
}
