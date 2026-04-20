package io.github.jason13official.zodiac_gems.impl.common.ability;

import io.github.jason13official.zodiac_gems.impl.common.entity.PlayerBodyDouble;
import io.github.jason13official.zodiac_gems.impl.common.network.ZodiacNetwork;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleWaterbendS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.util.GemAbility;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpectralArrowItem;
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
import org.joml.Vector3f;

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
        if (player.getCooldowns().getCooldownPercent(ModItems.GARNET, 1.0f) > 0) {
          return;
        }
        Vec3 look = player.getLookAngle();
        LargeFireball fireball = new LargeFireball(level, player, look, 1);
        fireball.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fireball);
        player.getCooldowns().addCooldown(ModItems.GARNET, 20 * 6);
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
        if (player.getCooldowns().getCooldownPercent(ModItems.AMETHYST, 1.0f) > 0) {
          return;
        }
        Vec3 look = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition(1.0f);
        DustParticleOptions dust = new DustParticleOptions(new Vector3f(0.6f, 0.0f, 1.0f), 1.2f);
        level.sendParticles(dust, eyePos.x, eyePos.y, eyePos.z, 15, 0.2, 0.2, 0.2, 0.0);
        for (int step = 1; step <= 6; step++) {
          Vec3 trailPos = eyePos.add(look.scale(step * 1.5));
          level.sendParticles(dust, trailPos.x, trailPos.y, trailPos.z, 3, 0.15, 0.15, 0.15, 0.0);
        }
        for (int i = 0; i < 4; i++) {
          Vec3 spread = look.add(
              (level.random.nextDouble() - 0.5) * 0.35,
              (level.random.nextDouble() - 0.5) * 0.35,
              (level.random.nextDouble() - 0.5) * 0.35
          ).normalize();
          Vec3 endPos = eyePos.add(spread.scale(10.0));
          HitResult blockHit = player.pick(10.0, 1.0f, false);
          double blockDistSq = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation().distanceToSqr(eyePos) : 100.0;
          AABB searchBox = player.getBoundingBox().expandTowards(spread.scale(10.0)).inflate(1.0, 1.0, 1.0);
          EntityHitResult pelletHit = ProjectileUtil.getEntityHitResult(player, eyePos, endPos, searchBox,
              e -> e != player && e instanceof LivingEntity, blockDistSq);
          if (pelletHit != null && pelletHit.getEntity() instanceof LivingEntity target) {
            target.hurt(level.damageSources().playerAttack(player), 3.0f);
          }
        }
        player.getCooldowns().addCooldown(ModItems.AMETHYST, 20 * 6);
      }

      case AQUAMARINE_WATERBEND -> {
        // cooldown only blocks pickup — always allow release so held water is never stuck
        if (!WaterbendTracker.isActive(player.getUUID()) && player.getCooldowns().getCooldownPercent(ModItems.AQUAMARINE, 1.0f) > 0) {
          return;
        }
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
              player.getCooldowns().addCooldown(ModItems.AQUAMARINE, 20 * 6);
            }
          }
        }
      }

      case DIAMOND_TURTLE -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.DIAMOND, 1.0f) > 0) {
          return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 4));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
        player.getCooldowns().addCooldown(ModItems.DIAMOND, 20 * 6);
      }

      case DIAMOND_TUNNEL -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.DIAMOND, 1.0f) > 0) {
          return;
        }
        if (player.getFoodData().getFoodLevel() < 4) {
          return;
        }
        player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 4);
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 3));
        player.getCooldowns().addCooldown(ModItems.DIAMOND, 20 * 6);
      }

      case EMERALD_TELEPORT -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.EMERALD, 1.0f) > 0) {
          return;
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL,
            0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
        ThrownEnderpearl pearl = new ThrownEnderpearl(level, player);
        pearl.setItem(new ItemStack(Items.ENDER_PEARL));
        pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity(pearl);
        player.getCooldowns().addCooldown(ModItems.EMERALD, 20 * 6);
      }

      case EMERALD_SPECTRAL_ARROW -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.EMERALD, 1.0f) > 0) {
          return;
        }
        ItemStack stack = new ItemStack(Items.SPECTRAL_ARROW);
        if (stack.getItem() instanceof SpectralArrowItem arrowItem) {
          AbstractArrow arrow = arrowItem.createArrow(level, stack, player, null);
          arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 3.0f, 0.0f);
          arrow.setCritArrow(true);
          level.addFreshEntity(arrow);
        }
        player.getCooldowns().addCooldown(ModItems.EMERALD, 20 * 6);
      }

      case MOONSTONE_SLOW_FALLING -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.MOONSTONE, 1.0f) > 0) {
          return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
        player.getCooldowns().addCooldown(ModItems.MOONSTONE, 20 * 6);
      }

      case RUBY_DRAGON_BREATH -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.RUBY, 1.0f) > 0) {
          return;
        }
        Vec3 look = player.getLookAngle();
        DragonFireball fireball = new DragonFireball(level, player, look);
        fireball.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fireball);
        player.getCooldowns().addCooldown(ModItems.RUBY, 20 * 6);
      }

      case RUBY_FLOAT_OTHERS -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.RUBY, 1.0f) > 0) {
          return;
        }
        EntityHitResult entityHit = pickLivingEntity(player, 30.0);
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
          Vec3 look = player.getLookAngle();
          ShulkerBullet bullet = new ShulkerBullet(level, player, target, null);
          bullet.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
          level.addFreshEntity(bullet);
        }
        player.getCooldowns().addCooldown(ModItems.RUBY, 20 * 6);
      }

      case PERIDOT_POISON_BLAST -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.PERIDOT, 1.0f) > 0) {
          return;
        }
        ThrownPotion potion = new ThrownPotion(level, player);
        potion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_POISON));
        potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20, 0.5f, 1.0f);
        level.addFreshEntity(potion);
        player.getCooldowns().addCooldown(ModItems.PERIDOT, 20 * 6);
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
        if (player.getCooldowns().getCooldownPercent(ModItems.TOURMALINE, 1.0f) > 0) {
          return;
        }
        PlayerBodyDouble entity = new PlayerBodyDouble(level);
        entity.setOwnerUUID(player.getUUID());
        entity.setAttacking(false);
        entity.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0);
        level.addFreshEntity(entity);
        player.getCooldowns().addCooldown(ModItems.TOURMALINE, 20 * 6);
      }

      case TOURMALINE_FIGHTING_MIMIC -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.TOURMALINE, 1.0f) > 0) {
          return;
        }
        PlayerBodyDouble entity = new PlayerBodyDouble(level);
        entity.setOwnerUUID(player.getUUID());
        entity.setAttacking(true);
        entity.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0);
        level.addFreshEntity(entity);
        player.getCooldowns().addCooldown(ModItems.TOURMALINE, 20 * 6);
      }

      case TOPAZ_WARP -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.TOPAZ, 1.0f) > 0) {
          return;
        }
        HitResult hit = player.pick(50, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          BlockPos pos = blockHit.getBlockPos().above();
          player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        }
        player.getCooldowns().addCooldown(ModItems.TOPAZ, 20 * 6);
      }

      case ZIRCON_FREEZE -> {
        if (player.getCooldowns().getCooldownPercent(ModItems.ZIRCON, 1.0f) > 0) {
          return;
        }
        AABB box = player.getBoundingBox().inflate(10);
        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player).forEach(e -> {
          e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 6, 10, true, true));
          e.setTicksFrozen(20 * 6);
        });
        player.getCooldowns().addCooldown(ModItems.ZIRCON, 20 * 30);
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
