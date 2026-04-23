package io.github.jason13official.zodiac_gems.impl.common.entity;

import io.github.jason13official.zodiac_gems.impl.common.registry.ModEntities;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerBodyDouble extends PathfinderMob {

  private static final EntityDataAccessor<Boolean> DATA_ATTACKING =
      SynchedEntityData.defineId(PlayerBodyDouble.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID =
      SynchedEntityData.defineId(PlayerBodyDouble.class, EntityDataSerializers.OPTIONAL_UUID);

  public PlayerBodyDouble(EntityType<? extends PlayerBodyDouble> playerBodyDoubleEntityType, Level level) {
    super(playerBodyDoubleEntityType, level);
  }

  public PlayerBodyDouble(Level pLevel) {
    super(ModEntities.BODY_DOUBLE, pLevel);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.tickCount >= 200) {
      this.discard();
    }
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(DATA_ATTACKING, false);
    builder.define(DATA_OWNER_UUID, Optional.empty());
  }

  public boolean isAttacking() {
    return this.entityData.get(DATA_ATTACKING);
  }

  public void setAttacking(boolean v) {
    this.entityData.set(DATA_ATTACKING, v);
  }

  public void setOwnerUUID(UUID uuid) {
    this.entityData.set(DATA_OWNER_UUID, Optional.of(uuid));
  }

  public AbstractClientPlayer getAttachedClientPlayer() {
    Optional<UUID> uuid = this.entityData.get(DATA_OWNER_UUID);
    if (uuid.isEmpty() || !(this.level() instanceof ClientLevel cl)) {
      return null;
    }
    Player p = cl.getPlayerByUUID(uuid.get());
    return p instanceof AbstractClientPlayer acp ? acp : null;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false) {
      @Override
      public boolean canUse() {
        return PlayerBodyDouble.this.isAttacking() && super.canUse();
      }
    });
    this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
      @Override
      public boolean canUse() {

        var target = this.target;
        var owner = PlayerBodyDouble.this.getAttachedClientPlayer();

        if ((target != null && owner != null) && target.getUUID().equals(owner.getUUID())) {
          return false;
        }

        return PlayerBodyDouble.this.isAttacking() && super.canUse();
      }
    });
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true) {
      @Override
      public boolean canUse() {
        return PlayerBodyDouble.this.isAttacking() && super.canUse();
      }
    });
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    this.swing(InteractionHand.MAIN_HAND);
    return super.doHurtTarget(target);
  }
}
