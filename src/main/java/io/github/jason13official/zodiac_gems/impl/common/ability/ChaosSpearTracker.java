package io.github.jason13official.zodiac_gems.impl.common.ability;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public class ChaosSpearTracker {

  private static final Map<Integer, ServerLevel> ARROWS = new HashMap<>();
  private static final DustParticleOptions DUST = new DustParticleOptions(new Vector3f(0.6f, 0.0f, 1.0f), 1.0f);

  public static void add(int entityId, ServerLevel level) {
    ARROWS.put(entityId, level);
  }

  public static void tick(ServerLevel level) {
    Iterator<Map.Entry<Integer, ServerLevel>> it = ARROWS.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Integer, ServerLevel> entry = it.next();
      if (entry.getValue() != level) continue;
      Entity e = level.getEntity(entry.getKey());
      if (e == null || e.isRemoved()) {
        it.remove();
        continue;
      }
      level.sendParticles(DUST, e.getX(), e.getY(), e.getZ(), 3, 0.05, 0.05, 0.05, 0.0);
    }
  }
}
