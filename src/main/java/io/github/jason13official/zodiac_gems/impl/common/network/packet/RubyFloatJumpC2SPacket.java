package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.impl.common.ability.RubyFloatJumpTracker;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RubyFloatJumpC2SPacket {

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

  public static void handle(RubyFloatJumpC2SPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null || GemType.getHeldGem(player) != GemType.RUBY) return;
      RubyFloatJumpTracker.setJumping(player.getUUID(), packet.jumping);
    });
    context.setPacketHandled(true);
  }
}
