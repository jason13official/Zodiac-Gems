package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NametagTracker {

  private static final Set<UUID> hiddenNametags = new HashSet<>();

  public static void hide(UUID player) {
    hiddenNametags.add(player);
  }

  public static void show(UUID player) {
    hiddenNametags.remove(player);
  }

  public static boolean isHidden(UUID player) {
    return hiddenNametags.contains(player);
  }

  public static Set<UUID> getAll() {
    return hiddenNametags;
  }
}
