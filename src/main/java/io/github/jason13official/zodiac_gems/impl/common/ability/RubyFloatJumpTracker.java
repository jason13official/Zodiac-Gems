package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RubyFloatJumpTracker {

  private static final Set<UUID> jumping = new HashSet<>();

  public static void setJumping(UUID player, boolean isJumping) {
    if (isJumping) jumping.add(player);
    else jumping.remove(player);
  }

  public static boolean isJumping(UUID player) {
    return jumping.contains(player);
  }

  public static void remove(UUID player) {
    jumping.remove(player);
  }

  public static boolean contains(UUID player) {
    return jumping.contains(player);
  }
}
