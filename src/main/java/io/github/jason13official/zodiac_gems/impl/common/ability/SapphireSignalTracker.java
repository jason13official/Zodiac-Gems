package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;

public class SapphireSignalTracker {

  private static final Map<UUID, Long> playerPositions = new HashMap<>();
  private static final Set<Long> activePositions = new HashSet<>();

  public static void update(UUID playerId, BlockPos pos) {
    Long old = playerPositions.put(playerId, pos.asLong());
    if (old != null) activePositions.remove(old);
    activePositions.add(pos.asLong());
  }

  public static void remove(UUID playerId) {
    Long old = playerPositions.remove(playerId);
    if (old != null) activePositions.remove(old);
  }

  public static boolean isActive(BlockPos pos) {
    return activePositions.contains(pos.asLong());
  }
}
