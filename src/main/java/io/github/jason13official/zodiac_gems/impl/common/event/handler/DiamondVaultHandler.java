package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class DiamondVaultHandler {

  private static final int VAULT_SIZE = 9;
  private static final Map<UUID, List<ItemStack>> vault = new HashMap<>();

  public static void onPlayerDeath(LivingDeathEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;
    if (GemType.getHeldGem(player) != GemType.DIAMOND) return;

    List<ItemStack> saved = new ArrayList<>();
    for (int i = 0; i < player.getInventory().getContainerSize() && saved.size() < VAULT_SIZE; i++) {
      ItemStack stack = player.getInventory().getItem(i);
      if (stack.isEmpty() || stack.getItem() instanceof ZodiacGemItem) continue;
      saved.add(stack.copy());
      player.getInventory().setItem(i, ItemStack.EMPTY);
    }

    if (!saved.isEmpty()) {
      vault.put(player.getUUID(), saved);
    }
  }

  public static void onPlayerClone(PlayerEvent.Clone event) {
    if (!event.isWasDeath()) return;
    UUID id = event.getOriginal().getUUID();
    List<ItemStack> saved = vault.remove(id);
    if (saved == null) return;
    ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
    for (ItemStack stack : saved) {
      if (!newPlayer.getInventory().add(stack)) {
        newPlayer.drop(stack, false);
      }
    }
  }
}
