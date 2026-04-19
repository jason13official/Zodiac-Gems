package io.github.jason13official.zodiac_gems;

import com.mojang.blaze3d.platform.InputConstants.Type;
import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleAbilityC2SPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.UseAbilityC2SPacket;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class ZodiacGemsClient {

  public static final Lazy<KeyMapping> USE_ABILITY = Lazy.of(() -> new KeyMapping("key.zodiac_gems.use", KeyConflictContext.IN_GAME, Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.zodiac_gems"));
  public static final Lazy<KeyMapping> TOGGLE_ABILITY = Lazy.of(() -> new KeyMapping("key.zodiac_gems.toggle", KeyConflictContext.IN_GAME, Type.KEYSYM, GLFW.GLFW_KEY_X, "key.categories.zodiac_gems"));

  public static final List<UUID> DARKNESS_TRACKER = new ArrayList<>();
  public static final List<UUID> WATERBEND_TRACKER = new ArrayList<>();

  public ZodiacGemsClient(final IEventBus modEventBus) {

    Constants.LOG.info("ZodiacGems Client.");

    modEventBus.addListener(this::registerKeyMappings);

    MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
  }

  public void registerKeyMappings(RegisterKeyMappingsEvent event) {
    event.register(USE_ABILITY.get());
    event.register(TOGGLE_ABILITY.get());
  }

  public void onClientTick(ClientTickEvent event) {
    if (event.phase != Phase.END) {
      return;
    }

    if (Minecraft.getInstance().level instanceof ClientLevel level && level.getGameTime() % 20 == 0) {
      Constants.LOG.info(DARKNESS_TRACKER.toString());
    }

    if (Minecraft.getInstance().level instanceof ClientLevel level && !WATERBEND_TRACKER.isEmpty()) {
      for (UUID uuid : WATERBEND_TRACKER) {
        Player bender = level.getPlayerByUUID(uuid);
        if (bender == null) continue;
        Vec3 ballPos = bender.getEyePosition().add(bender.getLookAngle().scale(3.0));
        for (int i = 0; i < 6; i++) {
          double ox = (level.random.nextDouble() - 0.5) * 0.4;
          double oy = (level.random.nextDouble() - 0.5) * 0.4;
          double oz = (level.random.nextDouble() - 0.5) * 0.4;
          level.addParticle(ParticleTypes.DRIPPING_WATER, ballPos.x + ox, ballPos.y + oy, ballPos.z + oz, 0, 0, 0);
        }
      }
    }

    while (USE_ABILITY.get().consumeClick()) {
      Constants.LOG.info("Consuming USE_ABILITY keypress.");

      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null) return;
      GemType gemType = GemType.getHeldGem(mc.player);
      if (gemType != null) {
        ZodiacNetwork.INSTANCE.send(new UseAbilityC2SPacket(gemType), PacketDistributor.SERVER.noArg());
      }
    }

    while (TOGGLE_ABILITY.get().consumeClick()) {
      Constants.LOG.info("Consuming TOGGLE_ABILITY keypress.");

      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null) return;
      GemType gemType = GemType.getHeldGem(mc.player);
      if (gemType != null) {
        ZodiacNetwork.INSTANCE.send(new ToggleAbilityC2SPacket(), PacketDistributor.SERVER.noArg());
      }
    }
  }
}
