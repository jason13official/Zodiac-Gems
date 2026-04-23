package io.github.jason13official.zodiac_gems.impl.common.item;

import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ZodiacGemItem extends Item {

  private final GemType gemType;

  public ZodiacGemItem(GemType gemType, Properties pProperties) {
    super(pProperties);
    this.gemType = gemType;
  }

  private static MutableComponent passive(String text) {
    return Component.literal("Passive: ").withStyle(ChatFormatting.GRAY)
        .append(Component.literal(text).withStyle(ChatFormatting.WHITE));
  }

  private static MutableComponent active(String text) {
    return Component.literal("Active: ").withStyle(ChatFormatting.YELLOW)
        .append(Component.literal(text).withStyle(ChatFormatting.WHITE));
  }

  public GemType getGemType() {
    return this.gemType;
  }

  @Override
  public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> list, TooltipFlag pTooltipFlag) {
    switch (this.gemType) {

      case GARNET -> {
        list.add(passive("Fire Resistance"));
        list.add(active("Ghast Fireball (6s cooldown)"));
      }

      case AMETHYST -> {
        list.add(active("Targeted Darkness (8s cooldown)"));
        list.add(active("Amethyst Blast (6s cooldown)"));
      }

      case AQUAMARINE -> {
        list.add(passive("Water Breathing + Dolphin's Grace"));
        list.add(active("Water Bending (6s cooldown)"));
      }

      case DIAMOND -> {
        list.add(passive("Aegis Vault"));
        list.add(active("Turtle Master Potion (6s cooldown)"));
        list.add(active("Fast Tunneling (6s cooldown)"));
      }

      case EMERALD -> {
        list.add(active("Ender Pearl (6s cooldown)"));
        list.add(active("Spectral Arrow (6s cooldown)"));
      }

      case MOONSTONE -> {
        list.add(passive("Night Vision"));
        list.add(passive("Nighttime Regeneration"));
        list.add(passive("Mob Cloaking"));
        list.add(active("Slow Falling Burst (6s cooldown)"));
      }

      case RUBY -> {
        list.add(passive("Controllable Levitation"));
        list.add(active("Dragon's Breath (6s cooldown)"));
        list.add(active("Shulker Bullet (6s cooldown)"));
      }

      case PERIDOT -> {
        list.add(passive("Poison Immunity"));
        list.add(active("Poison Blast (6s cooldown)"));
      }

      case SAPPHIRE -> {
        list.add(active("Lightning Strike(12s cooldown)"));
        list.add(passive("Redstone Emittance"));
      }

      case TOURMALINE -> {
        list.add(active("Idle Body Double (6s cooldown)"));
        list.add(active("Fighting Body Double (12s cooldown)"));
      }

      case TOPAZ -> {
        list.add(passive("Potion Immune"));
        list.add(active("Ray Cast (6s cooldown)"));
      }

      case ZIRCON -> {
        list.add(passive("Advanced Knee Replacements"));
        list.add(active("Freeze! (30s cooldown)"));
      }
    }
  }
}
