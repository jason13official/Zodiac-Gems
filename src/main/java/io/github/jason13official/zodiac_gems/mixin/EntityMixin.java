package io.github.jason13official.zodiac_gems.mixin;

import io.github.jason13official.zodiac_gems.accessor.AdditionalInventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

  @Unique
  private static CompoundTag zodiac$saveAllItems(CompoundTag pTag, NonNullList<ItemStack> pItems, boolean pAlwaysPutTag, HolderLookup.Provider pLevelRegistry) {
    ListTag listtag = new ListTag();

    for (int i = 0; i < pItems.size(); i++) {
      ItemStack itemstack = pItems.get(i);
      if (!itemstack.isEmpty()) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putByte("ZodiacSlot", (byte) i);
        listtag.add(itemstack.save(pLevelRegistry, compoundtag));
      }
    }

    if (!listtag.isEmpty() || pAlwaysPutTag) {
      pTag.put("ZodiacItems", listtag);
    }

    return pTag;
  }

  @Unique
  private static void zodiac$loadAllItems(CompoundTag pTag, NonNullList<ItemStack> pItems, HolderLookup.Provider pLevelRegistry) {
    ListTag listtag = pTag.getList("ZodiacItems", 10);

    for (int i = 0; i < listtag.size(); i++) {
      CompoundTag compoundtag = listtag.getCompound(i);
      int j = compoundtag.getByte("ZodiacSlot") & 255;
      if (j >= 0 && j < pItems.size()) {
        pItems.set(j, ItemStack.parse(pLevelRegistry, compoundtag).orElse(ItemStack.EMPTY));
      }
    }
  }

  @Inject(at = @At("RETURN"), method = "load")
  private void zodiac$load(CompoundTag pCompound, CallbackInfo ci) {
    Entity self = (Entity) (Object) this;

    if (!(self instanceof ServerPlayer player)) {
      return;
    }

    var aegis = AdditionalInventory.aegis(player.getInventory());
    zodiac$loadAllItems(pCompound, aegis.zodiac$getItems(), self.level().registryAccess());
  }

  @Inject(at = @At("RETURN"), method = "saveWithoutId")
  private void zodiac$saveWithoutId(CompoundTag pCompound, CallbackInfoReturnable<CompoundTag> cir) {
    Entity self = (Entity) (Object) this;

    if (!(self instanceof ServerPlayer player)) {
      return;
    }

    var aegis = AdditionalInventory.aegis(player.getInventory());
    zodiac$saveAllItems(pCompound, aegis.zodiac$getItems(), true, self.level().registryAccess());
  }
}
