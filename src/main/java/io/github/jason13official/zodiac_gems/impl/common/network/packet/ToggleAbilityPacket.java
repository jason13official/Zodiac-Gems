package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ToggleAbilityPacket {

  public ToggleAbilityPacket() {}

  public ToggleAbilityPacket(FriendlyByteBuf data) {}

  public void encode(FriendlyByteBuf data) {}

  public static void handle(ToggleAbilityPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {});
    context.setPacketHandled(true);
  }
}
