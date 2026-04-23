package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MoonstoneSlowFallTracker {

  private static final Set<UUID> ACTIVE = new HashSet<>();

  public static void add(UUID uuid) {
    ACTIVE.add(uuid);
  }

  public static boolean contains(UUID uuid) {
    return ACTIVE.contains(uuid);
  }

  public static void remove(UUID uuid) {
    ACTIVE.remove(uuid);
  }
}
