package io.github.jason13official.zodiac_gems;

import io.github.jason13official.zodiac_gems.impl.common.ability.PlayerAbilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.WaterbendTracker;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.LivingChangeTargetEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.LivingFallEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.LivingUpdateEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleDarknessS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleWaterbendS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModTabs;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
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

    MinecraftForge.EVENT_BUS.addListener(LivingUpdateEventHandler::onLivingUpdate);
    MinecraftForge.EVENT_BUS.addListener(LivingFallEventHandler::onLivingFall);
    MinecraftForge.EVENT_BUS.addListener(LivingChangeTargetEventHandler::onLivingChangeTarget);

    MinecraftForge.EVENT_BUS.addListener((Consumer<EntityLeaveLevelEvent>) event -> {
      if (event.getEntity() instanceof ServerPlayer player) {
        PlayerAbilityTracker.reset(player.getUUID());
        if (WaterbendTracker.isActive(player.getUUID())) {
          WaterbendTracker.remove(player.getUUID());
          for (ServerPlayer p : player.serverLevel().players()) {
            ZodiacNetwork.INSTANCE.send(new ToggleWaterbendS2CPacket(player.getUUID(), false), PacketDistributor.PLAYER.with(p));
          }
        }
      }
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<MobEffectEvent.Added>) event -> {

      if (event.getEntity().level().isClientSide()) {
        return;
      }

      if (event.getEffectInstance().is(MobEffects.DARKNESS)) {
        for (ServerPlayer player : ((ServerLevel) event.getEntity().level()).players()) {
          ZodiacNetwork.INSTANCE.send(new ToggleDarknessS2CPacket(event.getEntity().getUUID(), true), PacketDistributor.PLAYER.with(player));
        }
      }
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<MobEffectEvent.Remove>) event -> {

      if (event.getEntity().level().isClientSide()) {
        return;
      }

      if (event.getEffectInstance() != null && event.getEffectInstance().is(MobEffects.DARKNESS)) {
        for (ServerPlayer player : ((ServerLevel) event.getEntity().level()).players()) {
          ZodiacNetwork.INSTANCE.send(new ToggleDarknessS2CPacket(event.getEntity().getUUID(), false), PacketDistributor.PLAYER.with(player));
        }
      }
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<MobEffectEvent.Expired>) event -> {

      if (event.getEntity().level().isClientSide()) {
        return;
      }

      if (event.getEffectInstance() != null && event.getEffectInstance().is(MobEffects.DARKNESS)) {
        for (ServerPlayer player : ((ServerLevel) event.getEntity().level()).players()) {
          ZodiacNetwork.INSTANCE.send(new ToggleDarknessS2CPacket(event.getEntity().getUUID(), false), PacketDistributor.PLAYER.with(player));
        }
      }
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
