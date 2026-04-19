package io.github.jason13official.zodiac_gems.impl.common.item;

import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.world.item.Item;

public class ZodiacGemItem extends Item {

  private final GemType gemType;

  public ZodiacGemItem(GemType gemType, Properties pProperties) {
    super(pProperties);
    this.gemType = gemType;
  }

  public GemType getGemType() {
    return this.gemType;
  }
}
