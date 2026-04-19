package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WaterbendTracker {

  private static final Set<UUID> ACTIVE = new HashSet<>();

  public static void setActive(UUID uuid) {
    ACTIVE.add(uuid);
  }

  public static boolean isActive(UUID uuid) {
    return ACTIVE.contains(uuid);
  }

  public static void remove(UUID uuid) {
    ACTIVE.remove(uuid);
  }
}
