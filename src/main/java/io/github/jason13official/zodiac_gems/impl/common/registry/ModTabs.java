package io.github.jason13official.zodiac_gems.impl.common.registry;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.ZodiacGems;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTabs {

  public static CreativeModeTab ZODIAC_GEMS;

  public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {

    Constants.LOG.info("ZodiacGems CreativeModeTab.");

    ZODIAC_GEMS = CreativeModeTab.builder()
        .icon(() -> new ItemStack(ModItems.EMERALD))
        .title(Component.translatable("itemGroup.zodiacGems"))
        .displayItems((parameters, output) -> {
          output.accept(ModItems.GARNET);
          output.accept(ModItems.AMETHYST);
          output.accept(ModItems.AQUAMARINE);
          output.accept(ModItems.DIAMOND);
          output.accept(ModItems.EMERALD);
          output.accept(ModItems.YELLOW_EMERALD);
          output.accept(ModItems.WHITE_EMERALD);
          output.accept(ModItems.PURPLE_EMERALD);
          output.accept(ModItems.CYAN_EMERALD);
          output.accept(ModItems.RED_EMERALD);
          output.accept(ModItems.BLUE_EMERALD);
          output.accept(ModItems.MOONSTONE);
          output.accept(ModItems.RUBY);
          output.accept(ModItems.PERIDOT);
          output.accept(ModItems.SAPPHIRE);
          output.accept(ModItems.TOURMALINE);
          output.accept(ModItems.TOPAZ);
          output.accept(ModItems.ZIRCON);
        })
        .build();

    consumer.accept(ZODIAC_GEMS, ZodiacGems.identifier(Constants.MOD_ID));
  }
}
