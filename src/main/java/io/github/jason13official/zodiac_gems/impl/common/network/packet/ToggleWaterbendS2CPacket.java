package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ToggleWaterbendS2CPacket {

  private final UUID uuid;
  private final boolean active;

  public ToggleWaterbendS2CPacket(UUID uuid, boolean active) {
    this.uuid = uuid;
    this.active = active;
  }

  public ToggleWaterbendS2CPacket(FriendlyByteBuf data) {
    this.uuid = data.readUUID();
    this.active = data.readBoolean();
  }

  public static void handle(ToggleWaterbendS2CPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      if (packet.active && !ZodiacGemsClient.WATERBEND_TRACKER.contains(packet.uuid)) {
        ZodiacGemsClient.WATERBEND_TRACKER.add(packet.uuid);
      } else if (!packet.active) {
        ZodiacGemsClient.WATERBEND_TRACKER.remove(packet.uuid);
      }
    });
    context.setPacketHandled(true);
  }

  public void encode(FriendlyByteBuf data) {
    data.writeUUID(this.uuid);
    data.writeBoolean(this.active);
  }
}
