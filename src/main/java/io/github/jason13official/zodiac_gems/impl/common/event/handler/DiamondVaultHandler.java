package io.github.jason13official.zodiac_gems.impl.common.event.handler;

import io.github.jason13official.zodiac_gems.accessor.AdditionalInventory;
import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class DiamondVaultHandler {

  private static final int VAULT_SIZE = 9;
  private static final Map<UUID, List<ItemStack>> vault = new HashMap<>();

  public static void onPlayerDeath(LivingDeathEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }

    AdditionalInventory aegis = () -> ((AdditionalInventory) player.getInventory()).zodiac$getItems();
    List<ItemStack> saved = new ArrayList<>();
    for (int i = 0; i < aegis.getContainerSize() && saved.size() < VAULT_SIZE; i++) {
      ItemStack stack = aegis.getItem(i);
      if (stack.isEmpty() || stack.getItem() instanceof ZodiacGemItem) {
        continue;
      }
      saved.add(stack.copy());
      aegis.setItem(i, ItemStack.EMPTY);
    }

    if (!saved.isEmpty()) {
      vault.put(player.getUUID(), saved);
    }
  }

  public static void onPlayerClone(PlayerEvent.Clone event) {
    if (!event.isWasDeath()) {
      return;
    }
    UUID id = event.getOriginal().getUUID();
    List<ItemStack> saved = vault.remove(id);
    if (saved == null) {
      return;
    }
    ServerPlayer newPlayer = (ServerPlayer) event.getEntity();
    AdditionalInventory aegis = () -> ((AdditionalInventory) newPlayer.getInventory()).zodiac$getItems();

    int index = 0;
    for (ItemStack stack : saved) {
//      if (!newPlayer.getInventory().add(stack)) {
//        newPlayer.drop(stack, false);
//      }
      aegis.setItem(index, stack);
      index++;
    }

  }

  public static void onLoadPlayer(PlayerEvent.LoadFromFile event) {

  }

  public static void onSavePlayer(PlayerEvent.SaveToFile event) {
  }
}
