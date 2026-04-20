package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SyncNametagS2CPacket {

  private final UUID player;
  private final boolean hidden;

  public SyncNametagS2CPacket(UUID player, boolean hidden) {
    this.player = player;
    this.hidden = hidden;
  }

  public SyncNametagS2CPacket(FriendlyByteBuf data) {
    this.player = data.readUUID();
    this.hidden = data.readBoolean();
  }

  public void encode(FriendlyByteBuf data) {
    data.writeUUID(player);
    data.writeBoolean(hidden);
  }

  public static void handle(SyncNametagS2CPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      if (packet.hidden) {
        ZodiacGemsClient.HIDDEN_NAMETAGS.add(packet.player);
      } else {
        ZodiacGemsClient.HIDDEN_NAMETAGS.remove(packet.player);
      }
    });
    context.setPacketHandled(true);
  }
}
