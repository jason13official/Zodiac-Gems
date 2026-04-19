package io.github.jason13official.zodiac_gems;

import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModTabs;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class ZodiacGems {

  public static IEventBus EVENT_BUS;

  public ZodiacGems(FMLJavaModLoadingContext context) {

    Constants.LOG.info("ZodiacGems Common.");

    EVENT_BUS = context.getModEventBus();

    bind(Registries.ITEM, ModItems::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> {
      ZodiacNetwork.init();
    });

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new ZodiacGemsClient(EVENT_BUS);
    }
  }

  @Deprecated
  @SuppressWarnings("all")
  public ZodiacGems() {
    this(FMLJavaModLoadingContext.get());
  }

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  public <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registry.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registry, rl, () -> t));
      }
    });
  }
}
