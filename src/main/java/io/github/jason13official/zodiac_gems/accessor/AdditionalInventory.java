package io.github.jason13official.zodiac_gems.accessor;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.imc.TravelersBackpackIMC;
import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface AdditionalInventory extends Container {

  static AdditionalInventory aegis(Inventory inventory) {
    return () -> ((AdditionalInventory) inventory).zodiac$getItems();
  }

  static boolean isAllowed(ItemStack stack) {

    if (stack.getItem() instanceof ZodiacGemItem) {
      return false;
    } else {
      return !ZodiacGems.travelersBackpackInstalled || !TravelersBackpackIMC.isBackpack(stack);
    }
  }

  NonNullList<ItemStack> zodiac$getItems();

  @Override
  default int getContainerSize() {
    return zodiac$getItems().size();
  }

  @Override
  default boolean isEmpty() {
    return zodiac$getItems().isEmpty();
  }

  @Override
  default ItemStack getItem(int pSlot) {
    return zodiac$getItems().get(pSlot);
  }

  @Override
  default ItemStack removeItem(int pSlot, int pAmount) {
    return ContainerHelper.removeItem(zodiac$getItems(), pSlot, pAmount);
  }

  @Override
  default ItemStack removeItemNoUpdate(int pSlot) {
    return ContainerHelper.takeItem(zodiac$getItems(), pSlot);
  }

  @Override
  default void setItem(int pSlot, ItemStack pStack) {
    zodiac$getItems().set(pSlot, pStack);
  }

  @Override
  default int getMaxStackSize() {
    return Container.super.getMaxStackSize();
  }

  @Override
  default int getMaxStackSize(ItemStack pStack) {
    return Container.super.getMaxStackSize(pStack);
  }

  @Override
  default void setChanged() {

  }

  @Override
  default boolean stillValid(Player pPlayer) {
    return !pPlayer.isDeadOrDying();
  }

  @Override
  default void startOpen(Player pPlayer) {
    Container.super.startOpen(pPlayer);
  }

  @Override
  default void stopOpen(Player pPlayer) {
    Container.super.stopOpen(pPlayer);
  }

  @Override
  default void clearContent() {
    zodiac$getItems().clear();
  }
}
