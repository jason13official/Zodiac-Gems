package io.github.jason13official.zodiac_gems.impl.common.ability;

import io.github.jason13official.zodiac_gems.impl.common.util.GemAbility;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class PlayerAbilityTracker {

  private static final Map<UUID, Map<GemType, Integer>> selectedIndex = new HashMap<>();

  public static GemAbility getSelected(ServerPlayer player, GemType type) {
    int index = selectedIndex
        .getOrDefault(player.getUUID(), Map.of())
        .getOrDefault(type, 0);
    return type.getAbility(index);
  }

  public static void cycle(ServerPlayer player, GemType type) {
    if (type.getAbilityCount() <= 1) {
      return;
    }
    selectedIndex
        .computeIfAbsent(player.getUUID(), k -> new HashMap<>())
        .merge(type, 1, (cur, inc) -> (cur + inc) % type.getAbilityCount());
    GemAbility selected = getSelected(player, type);
    if (selected != null) {
      String name = selected.name().replace('_', ' ');
      player.sendSystemMessage(Component.literal("Ability: " + name));
    }
  }

  public static void reset(UUID playerId) {
    selectedIndex.remove(playerId);
  }
}
