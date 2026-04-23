package io.github.jason13official.zodiac_gems.impl.common.util;

import com.mojang.serialization.Codec;
import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import java.util.List;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum GemType implements StringRepresentable {

  GARNET(0, "garnet", 0.85f, 0.10f, 0.10f, GemAbility.GARNET_FIREBALL),
  AMETHYST(1, "amethyst", 0.60f, 0.00f, 1.00f, GemAbility.AMETHYST_DARKNESS, GemAbility.AMETHYST_BLAST),
  AQUAMARINE(2, "aquamarine", 0.00f, 0.85f, 0.90f, GemAbility.AQUAMARINE_WATERBEND),
  DIAMOND(3, "diamond", 0.70f, 0.90f, 1.00f, GemAbility.DIAMOND_TURTLE, GemAbility.DIAMOND_TUNNEL),
  EMERALD(4, "emerald", 0.00f, 0.80f, 0.20f, GemAbility.EMERALD_TELEPORT, GemAbility.EMERALD_SPECTRAL_ARROW),
  MOONSTONE(5, "moonstone", 0.95f, 0.95f, 0.70f, GemAbility.MOONSTONE_SLOW_FALLING),
  RUBY(6, "ruby", 0.90f, 0.05f, 0.20f, GemAbility.RUBY_DRAGON_BREATH, GemAbility.RUBY_FLOAT_OTHERS),
  PERIDOT(7, "peridot", 0.50f, 0.85f, 0.10f, GemAbility.PERIDOT_POISON_BLAST),
  SAPPHIRE(8, "sapphire", 0.05f, 0.30f, 0.95f, GemAbility.SAPPHIRE_LIGHTNING),
  TOURMALINE(9, "tourmaline", 0.95f, 0.30f, 0.55f, GemAbility.TOURMALINE_BODY_DOUBLE, GemAbility.TOURMALINE_FIGHTING_MIMIC),
  TOPAZ(10, "topaz", 1.00f, 0.75f, 0.05f, GemAbility.TOPAZ_WARP, GemAbility.TOPAZ_MIND_BEND),
  ZIRCON(11, "zircon", 0.85f, 0.95f, 1.00f, GemAbility.ZIRCON_FREEZE);

  public static final Codec<GemType> CODEC = StringRepresentable.fromEnum(GemType::values);
  private static final IntFunction<GemType> BY_ID = ByIdMap.continuous(GemType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
  private final int id;
  private final String name;
  private final float r, g, b;
  private final List<GemAbility> abilities;

  GemType(int id, String name, float r, float g, float b, GemAbility... abilities) {
    this.id = id;
    this.name = name;
    this.r = r;
    this.g = g;
    this.b = b;
    this.abilities = List.of(abilities);
  }

  public static GemType byId(int pId) {
    return BY_ID.apply(pId);
  }

  public static GemType getHeldGem(Player player) {
    for (ItemStack stack : player.getHandSlots()) {
      if (stack.getItem() instanceof ZodiacGemItem gem) {
        return gem.getGemType();
      }
    }
    return null;
  }

  public float[] getColor() {
    return new float[]{r, g, b};
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

  public List<GemAbility> getAbilities() {
    return abilities;
  }

  public int getAbilityCount() {
    return this.abilities.size();
  }

  public boolean hasAbilities() {
    return !abilities.isEmpty();
  }

  /// wraps values with modules `%` so we can just increment using `++` to get next valid ability index
  public GemAbility getAbility(int index) {
    if (abilities.isEmpty()) {
      return null;
    }
    return abilities.get(index % abilities.size());
  }
}
