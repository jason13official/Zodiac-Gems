package io.github.jason13official.zodiac_gems.mixin;

import io.github.jason13official.zodiac_gems.impl.common.ability.SapphireSignalTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class SignalGetterMixin {

  @Inject(method = "getSignal", at = @At("HEAD"), cancellable = true)
  private void sapphireRedstoneSignal(BlockGetter level, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir) {
    if (level instanceof Level lvl && !lvl.isClientSide && SapphireSignalTracker.isActive(pos)) {
      cir.setReturnValue(15);
    }
  }
}
