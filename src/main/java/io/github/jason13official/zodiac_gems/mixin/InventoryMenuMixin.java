package io.github.jason13official.zodiac_gems.mixin;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.accessor.AdditionalInventory;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingInput, CraftingRecipe> {

  public InventoryMenuMixin(MenuType<?> pMenuType, int pContainerId) {
    super(pMenuType, pContainerId);
  }

  @Inject(at = @At("TAIL"), method = "<init>")
  private void zodiac$constructor(Inventory inventory, boolean pActive, Player pOwner, CallbackInfo ci) {

    Constants.LOG.info("Constructing player inventory menu instance with Aegis Diamond additional slots.");

    AdditionalInventory aegis = () -> ((AdditionalInventory) inventory).zodiac$getItems();

    for (int index = 0; index < 9; index++) {
      this.addSlot(new Slot(aegis, index, 8 + index * 18, 142 + 32) {
        @Override
        public boolean mayPickup(Player player) {
          return player.getMainHandItem().is(ModItems.DIAMOND) || player.getOffhandItem().is(ModItems.DIAMOND);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
          boolean holdingDiamond = inventory.player.getMainHandItem().is(ModItems.DIAMOND)
              || inventory.player.getOffhandItem().is(ModItems.DIAMOND);
          if (!holdingDiamond) {
            return false;
          }

          return AdditionalInventory.isAllowed(stack);
        }
      });
    }
  }
}
