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
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RubyFloatJumpC2SPacket implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<RubyFloatJumpC2SPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("ruby_float_jump"));

  public static final StreamCodec<ByteBuf, RubyFloatJumpC2SPacket> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL,
      RubyFloatJumpC2SPacket::jumping,
      RubyFloatJumpC2SPacket::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  private final boolean jumping;

  public RubyFloatJumpC2SPacket(boolean jumping) {
    this.jumping = jumping;
  }

  public RubyFloatJumpC2SPacket(FriendlyByteBuf data) {
    this.jumping = data.readBoolean();
  }

  public void encode(FriendlyByteBuf data) {
    data.writeBoolean(jumping);
  }

  public boolean jumping() {
    return this.jumping;
  }

  public static void handle(RubyFloatJumpC2SPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null || GemType.getHeldGem(player) != GemType.RUBY) return;
      RubyFloatJumpTracker.setJumping(player.getUUID(), packet.jumping);
    });
    context.setPacketHandled(true);
  }

  public void handleOnServer(IPayloadContext iPayloadContext) {
    // packet = this;
  }
}
