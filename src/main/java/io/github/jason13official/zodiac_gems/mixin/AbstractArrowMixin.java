package io.github.jason13official.zodiac_gems.mixin;

import io.github.jason13official.zodiac_gems.accessor.ArrowLifeAccessor;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin implements ArrowLifeAccessor {

  @Shadow
  private int life;

  @Override
  public int zodiac_gems$getLife() {
    return this.life;
  }

  @Override
  public void zodiac_gems$setLife(int value) {
    this.life = value;
  }
}
