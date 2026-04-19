package io.github.jason13official.zodiac_gems.impl.common.ability;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.accessor.ArrowLifeAccessor;
import io.github.jason13official.zodiac_gems.impl.common.registry.ModItems;
import io.github.jason13official.zodiac_gems.impl.common.util.GemAbility;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GemAbilityActivator {

  public static void activate(ServerPlayer player) {
    GemType type = GemType.getHeldGem(player);
    if (type == null) return;
    GemAbility ability = PlayerAbilityTracker.getSelected(player, type);
    if (ability == null) return;
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
//        AABB box = player.getBoundingBox().inflate(8);
//        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player)
//            .forEach(e -> e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0)));

        Vec3 look = player.getLookAngle();
        Vec3 deltaMovement = look.normalize().scale(2.0f);
        // Arrow arrow = new Arrow(level, look.x, look.y, look.z, ItemStack.EMPTY, ItemStack.EMPTY);
        ItemStack stack = new ItemStack(Items.TIPPED_ARROW);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.HARMING).withEffectAdded(new MobEffectInstance(MobEffects.DARKNESS, 400, 0)));

        if (stack.getItem() instanceof TippedArrowItem arrowItem) {
          Arrow arrow = (Arrow) arrowItem.asProjectile(level, player.position(), stack, player.getDirection());

          ArrowLifeAccessor accesor = (ArrowLifeAccessor) arrow;
          accesor.zodiac_gems$setLife(1100);

          ArrowLifeAccessor accessorAgain = (ArrowLifeAccessor) arrow;
          Constants.LOG.info("Arrow has actual life value of {}", accessorAgain.zodiac_gems$getLife());

          arrow.setDeltaMovement(deltaMovement.x, deltaMovement.y, deltaMovement.z);
          arrow.pickup = Pickup.DISALLOWED;
          arrow.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
          level.addFreshEntity(arrow);

          player.getCooldowns().addCooldown(ModItems.AMETHYST, 20 * 8);
        }

//        HitResult hit = player.pick(4.0D, 0, false);
//
//        if (hit instanceof EntityHitResult result) {
//          if (result.getEntity() instanceof LivingEntity living) {
//            living.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
//
//            Constants.LOG.info("living has darkness? {}", living.hasEffect(MobEffects.DARKNESS));
//          } else {
//            Constants.LOG.info("not living/null?");
//          }
//        } else {
//          Constants.LOG.info("hit failed?");
//        }
      }

      case AMETHYST_BLAST -> {
        Vec3 look = player.getLookAngle();
        EvokerFangs fireball = new EvokerFangs(level, look.x, look.y, look.z, player.getViewYRot(1.0f), 5, player);
        fireball.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(fireball);
      }

      case AQUAMARINE_WATERBEND -> {
        // TODO
      }

      case DIAMOND_TURTLE -> {

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 3));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, 2));

        player.getCooldowns().addCooldown(ModItems.DIAMOND, 20 * 6);
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

        // maybe use AreaEffectCloud instead?
        Vec3 look = player.getLookAngle();
        DragonFireball arrow = new DragonFireball(level, player, look);
        arrow.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
        level.addFreshEntity(arrow);
      }

      case RUBY_FLOAT_OTHERS -> {

        HitResult hit = player.pick(30, 0, false);

        if (hit instanceof EntityHitResult result) {
          Vec3 look = player.getLookAngle();
          ShulkerBullet arrow = new ShulkerBullet(level, player, result.getEntity(), null);
          arrow.setPos(player.getX() + look.x * 2, player.getEyeY(), player.getZ() + look.z * 2);
          level.addFreshEntity(arrow);
        }
      }

      case PERIDOT_POISON_BLAST -> {
        ThrownPotion potion = new ThrownPotion(level, player);
        potion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_POISON));
        potion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20, 0.5f, 1.0f);
        level.addFreshEntity(potion);
      }

      case SAPPHIRE_LIGHTNING -> {
        HitResult hit = player.pick(30, 0, false);
        if (hit instanceof BlockHitResult blockHit) {
          LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
          bolt.setPos(Vec3.atCenterOf(blockHit.getBlockPos()));
          bolt.setVisualOnly(false);
          level.addFreshEntity(bolt);
        }
        player.getCooldowns().addCooldown(ModItems.SAPPHIRE, 20 * 12);
      }

      case SAPPHIRE_REDSTONE_SIGNAL -> {
        // TODO
      }

      case TOURMALINE_BODY_DOUBLE -> {
        // TODO: needs custom entity
      }

      case TOURMALINE_FIGHTING_MIMIC -> {
        // TODO
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
        level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player)
            .forEach(e -> e.setTicksFrozen(6 * 20));
      }
    }
  }
}
