package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.ZodiacGemsClient;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ToggleDarknessS2CPacket {

  private final UUID uuid;
  private final boolean darkness;

  public ToggleDarknessS2CPacket(UUID uuid, boolean darkness) {
    this.uuid = uuid;
    this.darkness = darkness;
  }

  public ToggleDarknessS2CPacket(FriendlyByteBuf data) {
    this.uuid = data.readUUID();
    this.darkness = data.readBoolean();
  }

  public static void handle(ToggleDarknessS2CPacket packet, CustomPayloadEvent.Context context) {
    Constants.LOG.info("Handling ToggleAbilityPacket.");
    context.enqueueWork(() -> {
      if (!ZodiacGemsClient.DARKNESS_TRACKER.contains(packet.getId()) && packet.hasDarkness()) {
        ZodiacGemsClient.DARKNESS_TRACKER.add(packet.getId());
      } else if (ZodiacGemsClient.DARKNESS_TRACKER.contains(packet.getId()) && !packet.hasDarkness()) {
        ZodiacGemsClient.DARKNESS_TRACKER.remove(packet.getId());
      }
    });
    context.setPacketHandled(true);
  }

  public void encode(FriendlyByteBuf data) {
    data.writeUUID(this.uuid);
    data.writeBoolean(this.darkness);
  }

  public UUID getId() {
    return uuid;
  }

  public boolean hasDarkness() {
    return darkness;
  }
}
