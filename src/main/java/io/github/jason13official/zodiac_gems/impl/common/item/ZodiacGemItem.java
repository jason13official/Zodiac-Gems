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

  public GemType getGemType() {
    return this.gemType;
  }

  @Override
  public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> list, TooltipFlag pTooltipFlag) {
    switch (this.gemType) {

      case GARNET -> {
        list.add(passive("Fire Resistance"));
        list.add(active("Launch Ghast Fireball"));
      }

      case AMETHYST -> {
        list.add(active("Darkness on targeted player (8s cooldown)"));
        list.add(active("Amethyst Blast Shotgun"));
      }

      case AQUAMARINE -> {
        list.add(passive("Water Breathing + Dolphin's Grace"));
        list.add(active("Water Bending — pick up and fling water"));
      }

      case DIAMOND -> {
        list.add(passive("9 inventory slots kept on death"));
        list.add(active("Turtle Master — Resistance IV (cooldown)"));
        list.add(active("Fast Tunneling — Haste IV, costs hunger"));
      }

      case EMERALD -> {
        list.add(active("Ender Pearl Teleport (cooldown)"));
        list.add(active("Chaos Spear — Spectral Arrow"));
      }

      case MOONSTONE -> {
        list.add(passive("Night Vision"));
        list.add(passive("Slow Falling"));
        list.add(passive("Regeneration at night"));
        list.add(passive("Mobs not hostile to holder"));
        list.add(active("Trigger Slow Falling burst"));
      }

      case RUBY -> {
        list.add(active("Controllable Floating"));
        list.add(active("Dragon's Breath attack"));
        list.add(active("Apply floating to another player"));
      }

      case PERIDOT -> {
        list.add(passive("Poison Immunity"));
        list.add(active("Poison Blast — splash Strong Poison"));
      }

      case SAPPHIRE -> {
        list.add(passive("Emits Redstone signal (strength 15)"));
        list.add(active("Lightning Strike — weakened (12s cooldown)"));
      }

      case TOURMALINE -> {
        list.add(active("Spawn idle body double"));
        list.add(active("Spawn fighting mimic"));
      }

      case TOPAZ -> {
        list.add(passive("No harmful effects"));
        list.add(active("Warp to cursor — 50-block raycast"));
      }

      case ZIRCON -> {
        list.add(passive("No fall damage"));
        list.add(active("Freeze nearby entities 6s (long cooldown)"));
      }
    }
  }

  private static MutableComponent passive(String text) {
    return Component.literal("Passive: ").withStyle(ChatFormatting.GRAY)
        .append(Component.literal(text).withStyle(ChatFormatting.WHITE));
  }

  private static MutableComponent active(String text) {
    return Component.literal("Active: ").withStyle(ChatFormatting.YELLOW)
        .append(Component.literal(text).withStyle(ChatFormatting.WHITE));
  }
}
