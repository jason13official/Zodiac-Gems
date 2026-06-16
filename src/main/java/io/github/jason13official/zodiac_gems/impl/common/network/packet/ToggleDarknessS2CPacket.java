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

public record ToggleDarknessS2CPacket(UUID uuid, boolean darkness) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<ToggleDarknessS2CPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("toggle_darkness"));

  public static final StreamCodec<ByteBuf, ToggleDarknessS2CPacket> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC,
      ToggleDarknessS2CPacket::uuid,
      ByteBufCodecs.BOOL,
      ToggleDarknessS2CPacket::darkness,
      ToggleDarknessS2CPacket::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnClient(IPayloadContext context) {
    context.enqueueWork(() -> {
      if (!ZodiacGemsClient.DARKNESS_TRACKER.contains(this.uuid()) && this.darkness()) {
        ZodiacGemsClient.DARKNESS_TRACKER.add(this.uuid());
      } else if (ZodiacGemsClient.DARKNESS_TRACKER.contains(this.uuid()) && !this.darkness()) {
        ZodiacGemsClient.DARKNESS_TRACKER.remove(this.uuid());
      }
    });
  }
}
