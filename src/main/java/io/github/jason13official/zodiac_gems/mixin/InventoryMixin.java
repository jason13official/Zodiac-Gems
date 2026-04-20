package io.github.jason13official.zodiac_gems.mixin;

import io.github.jason13official.zodiac_gems.accessor.AdditionalInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Inventory.class)
public class InventoryMixin implements AdditionalInventory {

  @Unique
  private final NonNullList<ItemStack> zodiac$aegis = NonNullList.withSize(9, ItemStack.EMPTY);

  @Override
  public NonNullList<ItemStack> zodiac$getItems() {
    return this.zodiac$aegis;
  }
}
