package io.github.jason13official.zodiac_gems.impl.common.item;

import io.github.jason13official.zodiac_gems.impl.common.util.ChaosEmeraldType;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;

public class ChaosEmeraldItem extends ZodiacGemItem {

  private final ChaosEmeraldType emeraldType;

  public ChaosEmeraldItem(ChaosEmeraldType emeraldType, Properties pProperties) {
    super(GemType.EMERALD, pProperties);

    this.emeraldType = emeraldType;
  }

  public ChaosEmeraldType getEmeraldType() {
    return emeraldType;
  }
}
