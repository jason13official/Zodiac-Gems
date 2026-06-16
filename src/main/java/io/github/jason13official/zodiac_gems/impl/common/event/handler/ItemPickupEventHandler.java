package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.item.ChaosEmeraldItem;
import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.joml.Vector3f;

public class ItemPickupEventHandler {

  public static void onItemPickup(ItemEntityPickupEvent.Post event) {

    if (!(event.getPlayer() instanceof ServerPlayer player)) {
      return;
    }
    if (!(player.level() instanceof ServerLevel level)) {
      return;
    }

    ItemStack stack = event.getItemEntity().getItem();

    if (!(stack.getItem() instanceof ZodiacGemItem gem)) {
      return;
    }

    GemType type = gem.getGemType();
    float[] c = type != GemType.EMERALD ? gem.getGemType().getColor() : getChaosEmeraldColors(stack);
    level.sendParticles(
        new DustParticleOptions(new Vector3f(c[0], c[1], c[2]), 1.5f),
        player.getX(), player.getY() + 0.5, player.getZ(),
        20, 0.3, 0.4, 0.3, 0.0
    );
  }

  private static float[] getChaosEmeraldColors(ItemStack stack) {

    if (stack.getItem() instanceof ChaosEmeraldItem chaosEmerald) {
      return chaosEmerald.getEmeraldType().getColor();
    }

    return new float[]{0,0,0}; // black
  }
}
