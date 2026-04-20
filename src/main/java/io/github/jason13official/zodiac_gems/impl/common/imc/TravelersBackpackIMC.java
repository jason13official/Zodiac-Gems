package io.github.jason13official.zodiac_gems.impl.common.imc;

import com.tiviacz.travelersbackpack.items.TravelersBackpackItem;
import net.minecraft.world.item.ItemStack;

public class TravelersBackpackIMC {

  public static boolean isBackpack(ItemStack stack) {
    return stack.getItem() instanceof TravelersBackpackItem;
  }
}
