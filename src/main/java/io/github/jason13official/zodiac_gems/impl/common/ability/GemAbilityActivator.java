package io.github.jason13official.zodiac_gems.impl.common.ability;

import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleWaterbendS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.util.GemAbility;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import io.github.jason13official.zodiac_gems.impl.common.entity.PlayerBodyDouble;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class GemAbilityActivator {

  public static void activate(ServerPlayer player) {
    GemType type = GemType.getHeldGem(player);
    if (type == null) {
      return;
    }
    GemAbility ability = PlayerAbilityTracker.getSelected(player, type);
    if (ability == null) {
      return;
    }
    activate(ability, player);
  }

  @SuppressWarnings("all")
  public static void activate(GemAbility ability, ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    switch (ability) {

      case GARNET_FIREBALL -> {
        Vec3 look = player.getLookAngle();
        LargeFireball fireball = new LargeFireball(level, player, look, 1);
        fireball.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fireball);
      }

      case AMETHYST_DARKNESS -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.AMETHYST, 1.0f) > 0) {
          return;
        }
        EntityHitResult entityHit = pickLivingEntity(player, 10.0);
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
          target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 400, 0));
          player.getCooldowns().addCooldown(ModItems.AMETHYST, 20 * 8);
        }
      }

      case AMETHYST_BLAST -> {
        Vec3 look = player.getLookAngle();
        EvokerFangs fangs = new EvokerFangs(level, look.x, look.y, look.z, player.getViewYRot(1.0f), 5, player);
        fangs.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fangs);
      }

      case AQUAMARINE_WATERBEND -> {
        if (WaterbendTracker.isActive(player.getUUID())) {
          WaterbendTracker.remove(player.getUUID());
          for (ServerPlayer p : level.players()) {
            ZodiacNetwork.INSTANCE.send(new ToggleWaterbendS2CPacket(player.getUUID(), false), PacketDistributor.PLAYER.with(p));
          }
          EntityHitResult entityHit = pickLivingEntity(player, 10.0);
          if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
            target.hurt(level.damageSources().playerAttack(player), 6.0f);
            Vec3 knockDir = target.position().subtract(player.position()).normalize();
            target.setDeltaMovement(knockDir.x * 1.5, 0.4, knockDir.z * 1.5);
            level.setBlock(target.blockPosition(), Blocks.WATER.defaultBlockState(), 3);
          } else {
            HitResult blockHit = player.pick(10.0, 1.0f, false);
            if (blockHit instanceof BlockHitResult bhr && blockHit.getType() != HitResult.Type.MISS) {
              level.setBlock(bhr.getBlockPos().relative(bhr.getDirection()), Blocks.WATER.defaultBlockState(), 3);
            }
          }
        } else {
          HitResult hit = player.pick(10, 0, true);
          if (hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();
            FluidState fluid = level.getFluidState(pos);
            if (fluid.is(FluidTags.WATER) && fluid.isSource()) {
              level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
              WaterbendTracker.setActive(player.getUUID());
              for (ServerPlayer p : level.players()) {
                ZodiacNetwork.INSTANCE.send(new ToggleWaterbendS2CPacket(player.getUUID(), true), PacketDistributor.PLAYER.with(p));
              }
            }
          }
        }
      }

      case DIAMOND_TURTLE -> {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 3));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, 2));
        player.getCooldowns().addCooldown(ModItems.DIAMOND, 20 * 6);
      }

      case DIAMOND_TUNNEL -> {
        if (player.getFoodData().getFoodLevel() < 4) return;
        player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 4);
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 3));
      }

      case EMERALD_TELEPORT -> {
        HitResult hit = player.pick(20, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          BlockPos pos = blockHit.getBlockPos().above();
          player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        }
      }

      case EMERALD_SPECTRAL_ARROW -> {
        Vec3 look = player.getLookAngle();
        SpectralArrow arrow = new SpectralArrow(level, look.x, look.y, look.z, ItemStack.EMPTY, ItemStack.EMPTY);
        arrow.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(arrow);
      }

      case MOONSTONE_SLOW_FALLING -> {
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
      }

      case RUBY_FLOAT -> {
        Vec3 vel = player.getDeltaMovement();
        player.setDeltaMovement(vel.x, 0.1, vel.z);
        player.fallDistance = 0;
      }

      case RUBY_DRAGON_BREATH -> {
        Vec3 look = player.getLookAngle();
        DragonFireball fireball = new DragonFireball(level, player, look);
        fireball.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fireball);
      }

      case RUBY_FLOAT_OTHERS -> {
        EntityHitResult entityHit = pickLivingEntity(player, 30.0);
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
          Vec3 look = player.getLookAngle();
          ShulkerBullet bullet = new ShulkerBullet(level, player, target, null);
          bullet.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
          level.addFreshEntity(bullet);
        }
      }

      case PERIDOT_POISON_BLAST -> {
        ThrownPotion potion = new ThrownPotion(level, player);
        potion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_POISON));
        potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20, 0.5f, 1.0f);
        level.addFreshEntity(potion);
      }

      case SAPPHIRE_LIGHTNING -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.SAPPHIRE, 1.0f) > 0) {
          return;
        }
        HitResult hit = player.pick(30, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
          bolt.setPos(Vec3.atCenterOf(blockHit.getBlockPos()));
          bolt.setVisualOnly(false);
          level.addFreshEntity(bolt);
        }
        player.getCooldowns().addCooldown(ModItems.SAPPHIRE, 20 * 12);
      }

      case TOURMALINE_BODY_DOUBLE -> {
        PlayerBodyDouble entity = new PlayerBodyDouble(level);
        entity.setOwnerUUID(player.getUUID());
        entity.setAttacking(false);
        entity.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0);
        level.addFreshEntity(entity);
      }

      case TOURMALINE_FIGHTING_MIMIC -> {
        PlayerBodyDouble entity = new PlayerBodyDouble(level);
        entity.setOwnerUUID(player.getUUID());
        entity.setAttacking(true);
        entity.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0);
        level.addFreshEntity(entity);
      }

      case TOPAZ_WARP -> {
        HitResult hit = player.pick(50, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          BlockPos pos = blockHit.getBlockPos().above();
          player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        }
      }

      case ZIRCON_FREEZE -> {
        AABB box = player.getBoundingBox().inflate(10);
        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player).forEach(e -> e.setTicksFrozen(6 * 20));
      }
    }
  }

  private static EntityHitResult pickLivingEntity(ServerPlayer player, double range) {
    Vec3 eyePos = player.getEyePosition(1.0f);
    Vec3 lookVec = player.getViewVector(1.0f);
    Vec3 endPos = eyePos.add(lookVec.scale(range));
    HitResult blockHit = player.pick(range, 1.0f, false);
    double blockDistSq = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation().distanceToSqr(eyePos) : range * range;
    AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0, 1.0, 1.0);
    return ProjectileUtil.getEntityHitResult(player, eyePos, endPos, searchBox, e -> e != player && e instanceof LivingEntity, blockDistSq);
  }
}
