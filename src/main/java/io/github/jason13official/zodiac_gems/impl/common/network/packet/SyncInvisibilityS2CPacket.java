package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Nullable;

public record SyncInvisibilityS2CPacket(UUID hiddenPlayer, boolean hidden, UUID canSeePlayer) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<SyncInvisibilityS2CPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("sync_invisibility"));

  public static final StreamCodec<ByteBuf, SyncInvisibilityS2CPacket> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC,
      SyncInvisibilityS2CPacket::hiddenPlayer,
      ByteBufCodecs.BOOL,
      SyncInvisibilityS2CPacket::hidden,
      UUIDUtil.STREAM_CODEC,
      SyncInvisibilityS2CPacket::canSeePlayer,
      SyncInvisibilityS2CPacket::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnClient(IPayloadContext context) {
    context.enqueueWork(() -> {
      if (this.hidden()) {
        ZodiacGemsClient.HIDDEN_PLAYERS.put(this.hiddenPlayer(), this.canSeePlayer());
      } else {
        ZodiacGemsClient.HIDDEN_PLAYERS.remove(this.hiddenPlayer());
      }
    });
  }
}
