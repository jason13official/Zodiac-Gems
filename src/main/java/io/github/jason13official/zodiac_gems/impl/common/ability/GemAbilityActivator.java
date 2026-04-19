package io.github.jason13official.zodiac_gems.impl.common.ability;

import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GemAbilityActivator {

  public static void activate(GemType type, ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    switch (type) {

      case GARNET -> {
        // Shoot a ghast fireball in look direction
        Vec3 look = player.getLookAngle();
        // LargeFireball fireball = new LargeFireball(level, player, look.x, look.y, look.z, 1);
        LargeFireball fireball = new LargeFireball(level, player, look, 1);
        fireball.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fireball);
      }

      case AMETHYST -> {
        // Amethyst blast: AOE darkness on nearby entities
        AABB box = player.getBoundingBox().inflate(8);
        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player).forEach(e -> e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0)));
      }

      case EMERALD -> {
        // Enderpearl-style teleport to look target (with cooldown TODO)
        HitResult hit = player.pick(20, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          BlockPos pos = blockHit.getBlockPos().above();
          player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        }
      }

      case RUBY -> {
        // Controllable float: negate fall velocity
        Vec3 vel = player.getDeltaMovement();
        player.setDeltaMovement(vel.x, 0.1, vel.z);
        player.fallDistance = 0;
      }

      case SAPPHIRE -> {
        // Lightning strike at look target
        HitResult hit = player.pick(30, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
          bolt.setPos(Vec3.atCenterOf(blockHit.getBlockPos()));
          bolt.setVisualOnly(false); // true = cosmetic only, no damage
          level.addFreshEntity(bolt);
        }

        player.getCooldowns().addCooldown(ModItems.SAPPHIRE, 20 * 12);
      }

      case TOPAZ -> {
        // Teleport to cursor
        HitResult hit = player.pick(50, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          BlockPos pos = blockHit.getBlockPos().above();
          player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        }
      }

      case ZIRCON -> {
        // Freeze all nearby entities for 6 seconds
        AABB box = player.getBoundingBox().inflate(10);
        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player).forEach(e -> e.setTicksFrozen(6 * 20));
        // TODO: long cooldown
      }

      case TOURMALINE -> {
        // Body double — stub, needs custom entity
        // SpawnBodyDouble(player, level);
      }

      case DIAMOND -> {
        // Turtle shell potion on cooldown
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 3));
        // Fast tunneling: dig speed burst
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 4));
      }

      case PERIDOT -> {
        // Poison blast
        Vec3 look = player.getLookAngle();
        ThrownPotion potion = new ThrownPotion(level, player);
        potion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_POISON));
        potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20, 0.5f, 1.0f);
        level.addFreshEntity(potion);
      }
    }
  }
}
