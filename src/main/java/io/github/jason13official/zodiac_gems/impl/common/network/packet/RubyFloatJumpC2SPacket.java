package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.ability.RubyFloatJumpTracker;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RubyFloatJumpC2SPacket(boolean jumping) implements CustomPacketPayload {

  public static final Type<RubyFloatJumpC2SPacket> TYPE = new Type<>(ZodiacGems.id("ruby_float_jump"));

  public static final StreamCodec<ByteBuf, RubyFloatJumpC2SPacket> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL,
      RubyFloatJumpC2SPacket::jumping,
      RubyFloatJumpC2SPacket::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnServer(IPayloadContext context) {
    // packet = this;
    context.enqueueWork(() -> {
      if (!(context.player() instanceof ServerPlayer player)) {
        return;
      }
      if (player == null || GemType.getHeldGem(player) != GemType.RUBY) {
        return;
      }
      RubyFloatJumpTracker.setJumping(player.getUUID(), this.jumping());
    });
  }
}
