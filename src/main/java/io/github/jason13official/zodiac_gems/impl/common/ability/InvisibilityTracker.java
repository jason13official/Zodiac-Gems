package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class InvisibilityTracker {

  private static final Map<UUID, UUID> hiddenToCanSee = new HashMap<>();

  public static void hide(UUID hiddenPlayer, UUID canSeePlayer) {
    hiddenToCanSee.put(hiddenPlayer, canSeePlayer);
  }

  public static void unhide(UUID hiddenPlayer) {
    hiddenToCanSee.remove(hiddenPlayer);
  }

  public static boolean isHidden(UUID player) {
    return hiddenToCanSee.containsKey(player);
  }

  @Nullable
  public static UUID getCanSee(UUID hiddenPlayer) {
    return hiddenToCanSee.get(hiddenPlayer);
  }

  public static Map<UUID, UUID> getAll() {
    return hiddenToCanSee;
  }
}
