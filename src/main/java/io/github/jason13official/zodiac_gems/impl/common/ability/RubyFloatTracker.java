package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RubyFloatTracker {

  private static final Map<UUID, Long> activeFloats = new HashMap<>();

  public static void start(UUID player, long expiryGameTime) {
    activeFloats.put(player, expiryGameTime);
  }

  public static boolean isActive(UUID player) {
    return activeFloats.containsKey(player);
  }

  public static long getExpiry(UUID player) {
    return activeFloats.getOrDefault(player, 0L);
  }

  public static void remove(UUID player) {
    activeFloats.remove(player);
  }
}
