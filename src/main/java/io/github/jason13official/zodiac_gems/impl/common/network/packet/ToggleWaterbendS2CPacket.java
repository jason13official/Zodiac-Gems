package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleWaterbendS2CPacket(UUID uuid, boolean active) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<ToggleWaterbendS2CPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("toggle_waterbend"));

  public static final StreamCodec<ByteBuf, ToggleWaterbendS2CPacket> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC,
      ToggleWaterbendS2CPacket::uuid,
      ByteBufCodecs.BOOL,
      ToggleWaterbendS2CPacket::active,
      ToggleWaterbendS2CPacket::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnClient(IPayloadContext context) {
    context.enqueueWork(() -> {
      if (this.active() && !ZodiacGemsClient.WATERBEND_TRACKER.contains(this.uuid())) {
        ZodiacGemsClient.WATERBEND_TRACKER.add(this.uuid());
      } else if (!this.active()) {
        ZodiacGemsClient.WATERBEND_TRACKER.remove(this.uuid());
      }
    });
  }
}
