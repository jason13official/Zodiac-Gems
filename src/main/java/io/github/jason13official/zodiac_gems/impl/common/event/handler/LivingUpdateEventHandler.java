package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

public class LivingUpdateEventHandler {

  private static final int DURATION_TICKS = 10;

  public static void onLivingUpdate(LivingTickEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;
    if (player.level().getGameTime() % 2 != 0) return; // every other tick

    GemType heldGemType = GemType.getHeldGem(player);
    if (heldGemType == null) return;

    switch (heldGemType) {
      case GARNET -> applyEffect(player, MobEffects.FIRE_RESISTANCE, DURATION_TICKS, 0);
      case AQUAMARINE -> {
        applyEffect(player, MobEffects.WATER_BREATHING, DURATION_TICKS, 0);
        applyEffect(player, MobEffects.DOLPHINS_GRACE, DURATION_TICKS, 0);
      }
      case MOONSTONE -> {
        applyEffect(player, MobEffects.NIGHT_VISION, DURATION_TICKS * 8, 0);
        applyEffect(player, MobEffects.SLOW_FALLING, DURATION_TICKS, 0);
        if (!player.level().isDay()) {
          applyEffect(player, MobEffects.REGENERATION, DURATION_TICKS, 0);
        }
      }
    }
  }

  private static void applyEffect(Player player, Holder<MobEffect> effect, int duration, int amplifier) {
    player.addEffect(new MobEffectInstance(effect, duration, Math.max(amplifier, 0), true, true));
  }
}
