package io.github.jason13official.zodiac_gems;

import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import org.lwjgl.glfw.GLFW;

public class ZodiacGemsClient {

  public static final Lazy<KeyMapping> USE_ABILITY = Lazy.of(() -> new KeyMapping("key.zodiac_gems.use", KeyConflictContext.IN_GAME, Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.zodiac_gems"));
  public static final Lazy<KeyMapping> TOGGLE_ABILITY = Lazy.of(() -> new KeyMapping("key.zodiac_gems.toggle", KeyConflictContext.IN_GAME, Type.KEYSYM, GLFW.GLFW_KEY_X, "key.categories.zodiac_gems"));

  public ZodiacGemsClient(final IEventBus modEventBus) {

    Constants.LOG.info("ZodiacGems Client.");

    modEventBus.addListener((Consumer<RegisterKeyMappingsEvent>) event -> {
      event.register(USE_ABILITY.get());
      event.register(TOGGLE_ABILITY.get());
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<ClientTickEvent>) event -> {
      if (event.phase != Phase.END) {
        return;
      }

      while (USE_ABILITY.get().consumeClick()) {
        Constants.LOG.info("Consuming USE_ABILITY keypress.");
      }

      while (TOGGLE_ABILITY.get().consumeClick()) {
        Constants.LOG.info("Consuming TOGGLE_ABILITY keypress.");
      }
    });
  }
}
