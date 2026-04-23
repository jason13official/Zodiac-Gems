package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.ability.MoonstoneSlowFallTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.RubyFloatJumpTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.SapphireSignalTracker;
import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleDarknessS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.network.PacketDistributor;

public class LivingUpdateEventHandler {

  private static final int DURATION_TICKS = 10;

  public static void onLivingUpdate(LivingTickEvent event) {

    LivingEntity entity = event.getEntity();
    Level level = entity.level();

    if (level.getGameTime() % 2 != 0) {
      return; // every other tick
    }
    if (level.isClientSide()) {
      return; // only on the server/logical side
    }

    // every five seconds, ensure entities are tracked properly via intermediate packets
    // between effect applied and effect removed/expired.
    if (level.getGameTime() % (20 * 5) == 0 && entity.hasEffect(MobEffects.DARKNESS)) {
      for (ServerPlayer player : ((ServerLevel) level).players()) {
        ZodiacNetwork.INSTANCE.send(new ToggleDarknessS2CPacket(event.getEntity().getUUID(), true), PacketDistributor.PLAYER.with(player));
      }
    }

    if (!(entity instanceof ServerPlayer player)) {
      return;
    }

    if (GemType.getHeldGem(player) == GemType.RUBY) {
      if (RubyFloatJumpTracker.isJumping(player.getUUID())) {
        // hold space → levitate up; renewed every 2 ticks so it never expires
        player.removeEffect(MobEffects.SLOW_FALLING);
        applyEffect(player, MobEffects.LEVITATION, 4, 1);
        var gravity = player.getAttribute(Attributes.GRAVITY);
        if (gravity != null) gravity.setBaseValue(0.0);
        player.fallDistance = 0;
        if (level.getGameTime() % 10 == 0) {
          ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD,
              player.getX(), player.getY() + 0.5, player.getZ(), 3, 0.2, 0.2, 0.2, 0.01);
        }
      } else if (!player.onGround()) {
        // airborne without space → passive slow fall (SLOW_FALLING only caps gravity when y≤0)
        player.removeEffect(MobEffects.LEVITATION);
        var gravity = player.getAttribute(Attributes.GRAVITY);
        if (gravity != null) gravity.setBaseValue(0.08);
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 4, 0, true, false));
        player.fallDistance = 0;
      } else {
        // on ground → clean up all hover effects, normal gravity
        player.removeEffect(MobEffects.LEVITATION);
        player.removeEffect(MobEffects.SLOW_FALLING);
        var gravity = player.getAttribute(Attributes.GRAVITY);
        if (gravity != null) gravity.setBaseValue(0.08);
      }
    } else {
      // ruby removed from hand → clean up
      if (RubyFloatJumpTracker.contains(player.getUUID()) && (player.hasEffect(MobEffects.LEVITATION) || player.hasEffect(MobEffects.SLOW_FALLING))) {
        RubyFloatJumpTracker.remove(player.getUUID());
        player.removeEffect(MobEffects.LEVITATION);
        player.removeEffect(MobEffects.SLOW_FALLING);
        var gravity = player.getAttribute(Attributes.GRAVITY);
        if (gravity != null) gravity.setBaseValue(0.08);
      }
    }

    if (MoonstoneSlowFallTracker.contains(player.getUUID())) {
      if (player.hasEffect(MobEffects.SLOW_FALLING)) {
        if (level.getGameTime() % 4 == 0) {
          ((ServerLevel) level).sendParticles(ParticleTypes.END_ROD,
              player.getX(), player.getY() + 1.0, player.getZ(), 5, 0.3, 0.5, 0.3, 0.01);
          ((ServerLevel) level).sendParticles(ParticleTypes.GLOW,
              player.getX(), player.getY() + 1.0, player.getZ(), 3, 0.3, 0.3, 0.3, 0.0);
        }
      } else {
        MoonstoneSlowFallTracker.remove(player.getUUID());
      }
    }

    GemType heldGemType = GemType.getHeldGem(player);

    if (heldGemType != GemType.SAPPHIRE) {
      SapphireSignalTracker.remove(player.getUUID(), player.level());
    }

    if (heldGemType == null) {
      return;
    }

    switch (heldGemType) {
      case GARNET -> applyEffect(player, MobEffects.FIRE_RESISTANCE, DURATION_TICKS, 0);
      case AQUAMARINE -> {
        applyEffect(player, MobEffects.WATER_BREATHING, DURATION_TICKS, 0);
        applyEffect(player, MobEffects.DOLPHINS_GRACE, DURATION_TICKS, 0);
      }
      case MOONSTONE -> {
        applyEffect(player, MobEffects.NIGHT_VISION, 20 * 30, 0); // 30 seconds to avoid flickering
        // applyEffect(player, MobEffects.SLOW_FALLING, DURATION_TICKS, 0); // only as active ability
        if (!player.level().isDay()) {
          applyEffect(player, MobEffects.REGENERATION, DURATION_TICKS, 0);
        }
      }
      case PERIDOT -> player.removeEffect(MobEffects.POISON);
      case SAPPHIRE -> SapphireSignalTracker.update(player.getUUID(), player.blockPosition(), player.level());
      case TOPAZ -> player.getActiveEffects().stream()
          .filter(e -> e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
          .map(e -> e.getEffect())
          .toList()
          .forEach(player::removeEffect);
    }
  }

  private static void applyEffect(Player player, Holder<MobEffect> effect, int duration, int amplifier) {
    player.addEffect(new MobEffectInstance(effect, duration, Math.max(amplifier, 0), true, true));
  }
}
