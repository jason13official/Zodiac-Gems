package io.github.jason13official.zodiac_gems.impl.common.util;

import com.mojang.serialization.Codec;
import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum GemType implements StringRepresentable {

  GARNET(0, "garnet"),
  AMETHYST(1, "amethyst"),
  AQUAMARINE(2, "aquamarine"),
  DIAMOND(3, "diamond"),
  EMERALD(4, "emerald"),
  MOONSTONE(5, "moonstone"),
  RUBY(6, "ruby"),
  PERIDOT(7, "peridot"),
  SAPPHIRE(8, "sapphire"),
  TOURMALINE(9, "tourmaline"),
  TOPAZ(10, "topaz"),
  ZIRCON(11, "zircon");

  public static final Codec<GemType> CODEC = StringRepresentable.fromEnum(GemType::values);
  private static final IntFunction<GemType> BY_ID = ByIdMap.continuous(GemType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  private final int id;

  private final String name;

  GemType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static GemType byId(int pId) {
    return BY_ID.apply(pId);
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static GemType getHeldGem(Player player) {
    for (ItemStack stack : player.getHandSlots()) {
      if (stack.getItem() instanceof ZodiacGemItem gem) {
        return gem.getGemType();
      }
    }
    return null;
  }
}
