package io.github.jason13official.zodiac_gems.impl.common.entity;

import io.github.jason13official.zodiac_gems.impl.common.registry.ModEntities;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class PlayerBodyDouble extends Mob {

  public PlayerBodyDouble(EntityType<? extends PlayerBodyDouble> playerBodyDoubleEntityType, Level level) {
    super(playerBodyDoubleEntityType, level);
  }

  public PlayerBodyDouble(Level pLevel) {
    super(ModEntities.BODY_DOUBLE, pLevel);
  }

  public AbstractClientPlayer getAttachedClientPlayer() {
    // TODO PlayerBodyDouble entities should only exist while their attached player is in the world, this should be safe when wired up properly. maybe have an instance field for the tracked player UUID, and client finds client player instance from that?
    return null;
  }
}
