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

public record SyncNametagS2CPacket(UUID player, boolean hidden) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SyncNametagS2CPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("sync_nametag"));

  public static final StreamCodec<ByteBuf, SyncNametagS2CPacket> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC,
      SyncNametagS2CPacket::player,
      ByteBufCodecs.BOOL,
      SyncNametagS2CPacket::hidden,
      SyncNametagS2CPacket::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnClient(IPayloadContext context) {
    context.enqueueWork(() -> {
      if (this.hidden()) {
        ZodiacGemsClient.HIDDEN_NAMETAGS.add(this.player());
      } else {
        ZodiacGemsClient.HIDDEN_NAMETAGS.remove(this.player());
      }
    });
  }
}
