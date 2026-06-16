package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.ability.PlayerAbilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleAbilityC2SPacket() implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<ToggleAbilityC2SPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("toggle_ability"));

  public static final StreamCodec<ByteBuf, ToggleAbilityC2SPacket> STREAM_CODEC = StreamCodec.unit(new ToggleAbilityC2SPacket());

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnServer(IPayloadContext context) {
    context.enqueueWork(() -> {
      if (!(context.player() instanceof ServerPlayer player)) {
        return;
      }
      GemType type = GemType.getHeldGem(player);
      if (type == null) {
        return;
      }
      PlayerAbilityTracker.cycle(player, type);
    });
  }
}
