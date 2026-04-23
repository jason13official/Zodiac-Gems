package io.github.jason13official.zodiac_gems.impl.common.util;

public enum ChaosEmeraldType {

  WHITE(1.0f, 1.0f, 1.0f),
  GREEN(0.00f, 0.80f, 0.20f),
  PURPLE(0.63f, 0.13f, 0.94f),
  CYAN(0.0f, 0.9f, 0.93f),
  RED(0.63f, 0.14f, 0.13f),
  YELLOW(0.71f, 0.65f, 0.26f),
  BLUE(0.0f, 0.24f, 1.0f);

  private final float r, g, b;

  ChaosEmeraldType(float r, float g, float b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public float[] getColor() {
    return new float[]{r, g, b};
  }
}
