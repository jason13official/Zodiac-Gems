package io.github.jason13official.zodiac_gems.mixin;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {

  @Unique
  private static final ResourceLocation ZODIAC$INVENTORY_LOCATION = ZodiacGems.id("zodiac/additional_slots");

  /// dummy
  public InventoryScreenMixin(InventoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
    super(pMenu, pPlayerInventory, pTitle);
  }

  private static void render(InventoryScreen screen, GuiGraphics pGuiGraphics) {
    // pGuiGraphics.blit(ZODIAC$INVENTORY_LOCATION, 1, (142 + 39) - 7, 0, 0, 176, 32);
    // pGuiGraphics.blit(ZODIAC$INVENTORY_LOCATION, 5, 5, 0, 0, 176, 32);
    pGuiGraphics.blitSprite(ZODIAC$INVENTORY_LOCATION, screen.getGuiLeft(), screen.getGuiTop() + screen.getYSize(), 176, 32);
  }

  @Inject(at = @At("HEAD"), method = "renderBg")
  private void zodiac$renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, CallbackInfo ci) {

    if (this.minecraft == null || this.minecraft.player == null) {
      return;
    }

    AtomicBoolean hasDiamond = new AtomicBoolean(false);

    this.minecraft.player.getHandSlots().forEach(stack -> {
      if (stack.is(ModItems.DIAMOND)) {
        hasDiamond.set(true);
      }
    });

    if (!hasDiamond.get()) {
      return;
    }

    pGuiGraphics.pose().pushPose();
    render((InventoryScreen) (Object) this, pGuiGraphics);
    pGuiGraphics.pose().popPose();
  }
}
