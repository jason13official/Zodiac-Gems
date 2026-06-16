package io.github.jason13official.zodiac_gems;

import io.github.jason13official.zodiac_gems.impl.common.ability.ChaosSpearTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.InvisibilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.MoonstoneSlowFallTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.NametagTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.PlayerAbilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.RubyFloatJumpTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.WaterbendTracker;
import io.github.jason13official.zodiac_gems.impl.common.command.ZodiacCommands;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.DiamondVaultHandler;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.ItemPickupEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.LivingChangeTargetEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.LivingFallEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.event.handler.LivingUpdateEventHandler;
import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.RubyFloatJumpC2SPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.SyncInvisibilityS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.SyncNametagS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleDarknessS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleWaterbendS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModEntities;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.Map;
import java.util.UUID;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class ZodiacGems {

  public static IEventBus EVENT_BUS;
  public static boolean travelersBackpackInstalled = false;

  public ZodiacGems(IEventBus modEventBus) {

    if (FMLLoader.getLoadingModList().getModFileById("travelersbackpack") != null) {
      Constants.LOG.info("travelersbackpack mod ID loaded.");
      travelersBackpackInstalled = true;
    }

    Constants.LOG.info("ZodiacGems Common.");

    EVENT_BUS = modEventBus;

    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);

    EVENT_BUS.addListener((Consumer<RegisterPayloadHandlersEvent>) event -> {
      ZodiacNetwork.init();

      final PayloadRegistrar registrar = event.registrar("1");

      registrar.playToServer(RubyFloatJumpC2SPacket.TYPE, RubyFloatJumpC2SPacket.STREAM_CODEC, RubyFloatJumpC2SPacket::handleOnServer);
      registrar.playToClient(SyncInvisibilityS2CPacket.TYPE, SyncInvisibilityS2CPacket.STREAM_CODEC, SyncInvisibilityS2CPacket::handleOnClient);
    });

    NeoForge.EVENT_BUS.addListener(ItemPickupEventHandler::onItemPickup);
    NeoForge.EVENT_BUS.addListener(LivingUpdateEventHandler::onLivingUpdate);
    NeoForge.EVENT_BUS.addListener(LivingFallEventHandler::onLivingFall);
    NeoForge.EVENT_BUS.addListener(LivingChangeTargetEventHandler::onLivingChangeTarget);
    NeoForge.EVENT_BUS.addListener(DiamondVaultHandler::onPlayerDeath);
    NeoForge.EVENT_BUS.addListener(DiamondVaultHandler::onPlayerClone);
    NeoForge.EVENT_BUS.addListener(ZodiacCommands::register);

    NeoForge.EVENT_BUS.addListener((Consumer<LevelTickEvent.Post>) event -> {
      if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel serverLevel)) return;
      ChaosSpearTracker.tick(serverLevel);
    });

    NeoForge.EVENT_BUS.addListener((Consumer<PlayerEvent.PlayerLoggedInEvent>) event -> {
      if (!(event.getEntity() instanceof ServerPlayer player)) return;
      for (Map.Entry<UUID, UUID> entry : InvisibilityTracker.getAll().entrySet()) {
        ZodiacNetwork.INSTANCE.send(new SyncInvisibilityS2CPacket(entry.getKey(), true, entry.getValue()), PacketDistributor.PLAYER.with(player));
      }
      for (UUID uuid : NametagTracker.getAll()) {
        ZodiacNetwork.INSTANCE.send(new SyncNametagS2CPacket(uuid, true), PacketDistributor.PLAYER.with(player));
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<EntityLeaveLevelEvent>) event -> {
      if (event.getEntity() instanceof ServerPlayer player) {
        PlayerAbilityTracker.reset(player.getUUID());
        RubyFloatJumpTracker.remove(player.getUUID());
        MoonstoneSlowFallTracker.remove(player.getUUID());
        if (WaterbendTracker.isActive(player.getUUID())) {
          WaterbendTracker.remove(player.getUUID());
          for (ServerPlayer p : player.serverLevel().players()) {
            ZodiacNetwork.INSTANCE.send(new ToggleWaterbendS2CPacket(player.getUUID(), false), PacketDistributor.PLAYER.with(p));
          }
        }
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<LivingDeathEvent>) event -> {
      if (event.getEntity() instanceof ServerPlayer player) {
        MoonstoneSlowFallTracker.remove(player.getUUID());
        if (WaterbendTracker.isActive(player.getUUID())) {
          WaterbendTracker.remove(player.getUUID());
          for (ServerPlayer p : player.serverLevel().players()) {
            ZodiacNetwork.INSTANCE.send(new ToggleWaterbendS2CPacket(player.getUUID(), false), PacketDistributor.PLAYER.with(p));
          }
        }
      }
    });

    EVENT_BUS.addListener((Consumer<EntityAttributeCreationEvent>) event -> {
      event.put(ModEntities.BODY_DOUBLE,
          LivingEntity.createLivingAttributes()
              .add(Attributes.ATTACK_DAMAGE, 4.0)
              .add(Attributes.FOLLOW_RANGE, 16.0)
              .build());
    });

    NeoForge.EVENT_BUS.addListener((Consumer<MobEffectEvent.Added>) event -> {

      if (event.getEntity().level().isClientSide()) {
        return;
      }

      if (event.getEffectInstance().is(MobEffects.DARKNESS)) {
        for (ServerPlayer player : ((ServerLevel) event.getEntity().level()).players()) {
          ZodiacNetwork.INSTANCE.send(new ToggleDarknessS2CPacket(event.getEntity().getUUID(), true), PacketDistributor.PLAYER.with(player));
        }
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<MobEffectEvent.Remove>) event -> {

      if (event.getEntity().level().isClientSide()) {
        return;
      }

      if (event.getEffectInstance() != null && event.getEffectInstance().is(MobEffects.DARKNESS)) {
        for (ServerPlayer player : ((ServerLevel) event.getEntity().level()).players()) {
          ZodiacNetwork.INSTANCE.send(new ToggleDarknessS2CPacket(event.getEntity().getUUID(), false), PacketDistributor.PLAYER.with(player));
        }
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<MobEffectEvent.Expired>) event -> {

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

  public static ResourceLocation id(String path) {
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
