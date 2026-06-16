package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.ability.GemAbilityActivator;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UseAbilityC2SPacket() implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<UseAbilityC2SPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("use_ability"));

  public static final StreamCodec<ByteBuf, UseAbilityC2SPacket> STREAM_CODEC = StreamCodec.unit(new UseAbilityC2SPacket());

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnServer(IPayloadContext context) {
    context.enqueueWork(() -> {
      if (!(context.player() instanceof ServerPlayer player)) {
        return;
      }
      GemAbilityActivator.activate(player);
    });
  }
}
