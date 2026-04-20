package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.Nullable;

public class SyncInvisibilityS2CPacket {

  private final UUID hiddenPlayer;
  private final boolean hidden;
  @Nullable private final UUID canSeePlayer;

  public SyncInvisibilityS2CPacket(UUID hiddenPlayer, boolean hidden, @Nullable UUID canSeePlayer) {
    this.hiddenPlayer = hiddenPlayer;
    this.hidden = hidden;
    this.canSeePlayer = canSeePlayer;
  }

  public SyncInvisibilityS2CPacket(FriendlyByteBuf data) {
    this.hiddenPlayer = data.readUUID();
    this.hidden = data.readBoolean();
    this.canSeePlayer = this.hidden ? data.readUUID() : null;
  }

  public void encode(FriendlyByteBuf data) {
    data.writeUUID(hiddenPlayer);
    data.writeBoolean(hidden);
    if (hidden) data.writeUUID(canSeePlayer);
  }

  public static void handle(SyncInvisibilityS2CPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      if (packet.hidden) {
        ZodiacGemsClient.HIDDEN_PLAYERS.put(packet.hiddenPlayer, packet.canSeePlayer);
      } else {
        ZodiacGemsClient.HIDDEN_PLAYERS.remove(packet.hiddenPlayer);
      }
    });
    context.setPacketHandled(true);
  }
}
