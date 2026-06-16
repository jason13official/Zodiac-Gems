package io.github.jason13official.zodiac_gems.impl.common.registry;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.item.ZodiacGemItem;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class ModItems {

  /// JANUARY
  public static Item GARNET;

  /// FEBRUARY
  public static Item AMETHYST;

  /// MARCH
  public static Item AQUAMARINE;
  ///
  /// APRIL
  public static Item DIAMOND;

  /// MAY
  public static Item EMERALD;

  /// [ModItems#EMERALD] mimic see [ModItems#chaosEmeralds()]
  public static Item YELLOW_EMERALD, WHITE_EMERALD, PURPLE_EMERALD, CYAN_EMERALD, RED_EMERALD, BLUE_EMERALD;

  /// JUNE
  public static Item MOONSTONE;

  /// JULY
  public static Item RUBY;

  /// AUGUST
  public static Item PERIDOT;

  /// SEPTEMBER
  public static Item SAPPHIRE;

  /// OCTOBER
  public static Item TOURMALINE;

  /// NOVEMBER
  public static Item TOPAZ;

  /// DECEMBER
  public static Item ZIRCON;

  public static Item[] chaosEmeralds() {
    return new Item[]{EMERALD, YELLOW_EMERALD, WHITE_EMERALD, PURPLE_EMERALD, CYAN_EMERALD, RED_EMERALD, BLUE_EMERALD};
  }

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    Constants.LOG.info("ZodiacGems Items.");

    GARNET = new ZodiacGemItem(GemType.GARNET, new Properties().stacksTo(1));
    AMETHYST = new ZodiacGemItem(GemType.AMETHYST, new Properties().stacksTo(1));
    AQUAMARINE = new ZodiacGemItem(GemType.AQUAMARINE, new Properties().stacksTo(1));
    DIAMOND = new ZodiacGemItem(GemType.DIAMOND, new Properties().stacksTo(1));

    EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));
    YELLOW_EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));
    WHITE_EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));
    PURPLE_EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));
    CYAN_EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));
    RED_EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));
    BLUE_EMERALD = new ZodiacGemItem(GemType.EMERALD, new Properties().stacksTo(1));

    MOONSTONE = new ZodiacGemItem(GemType.MOONSTONE, new Properties().stacksTo(1));
    RUBY = new ZodiacGemItem(GemType.RUBY, new Properties().stacksTo(1));
    PERIDOT = new ZodiacGemItem(GemType.PERIDOT, new Properties().stacksTo(1));
    SAPPHIRE = new ZodiacGemItem(GemType.SAPPHIRE, new Properties().stacksTo(1));
    TOURMALINE = new ZodiacGemItem(GemType.TOURMALINE, new Properties().stacksTo(1));
    TOPAZ = new ZodiacGemItem(GemType.TOPAZ, new Properties().stacksTo(1));
    ZIRCON = new ZodiacGemItem(GemType.ZIRCON, new Properties().stacksTo(1));

    consumer.accept(GARNET, ZodiacGems.id("garnet"));
    consumer.accept(AMETHYST, ZodiacGems.id("amethyst"));
    consumer.accept(AQUAMARINE, ZodiacGems.id("aquamarine"));
    consumer.accept(DIAMOND, ZodiacGems.id("diamond"));

    consumer.accept(EMERALD, ZodiacGems.id("emerald"));
    consumer.accept(YELLOW_EMERALD, ZodiacGems.id("yellow_emerald"));
    consumer.accept(WHITE_EMERALD, ZodiacGems.id("white_emerald"));
    consumer.accept(PURPLE_EMERALD, ZodiacGems.id("purple_emerald"));
    consumer.accept(CYAN_EMERALD, ZodiacGems.id("cyan_emerald"));
    consumer.accept(RED_EMERALD, ZodiacGems.id("red_emerald"));
    consumer.accept(BLUE_EMERALD, ZodiacGems.id("blue_emerald"));

    consumer.accept(MOONSTONE, ZodiacGems.id("moonstone"));
    consumer.accept(RUBY, ZodiacGems.id("ruby"));
    consumer.accept(PERIDOT, ZodiacGems.id("peridot"));
    consumer.accept(SAPPHIRE, ZodiacGems.id("sapphire"));
    consumer.accept(TOURMALINE, ZodiacGems.id("tourmaline"));
    consumer.accept(TOPAZ, ZodiacGems.id("topaz"));
    consumer.accept(ZIRCON, ZodiacGems.id("zircon"));
  }
}
