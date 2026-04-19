package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.impl.common.ability.GemAbilityActivator;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UseAbilityPacket {

  private final GemType gemType;

  public UseAbilityPacket(GemType gemType) {
    this.gemType = gemType;
  }

  public UseAbilityPacket(FriendlyByteBuf data) {
    this.gemType = GemType.byId(data.readByte());
  }

  public void encode(FriendlyByteBuf data) {
    data.writeByte(this.gemType.getId());
  }

  public static void handle(UseAbilityPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null) return;
      GemAbilityActivator.activate(packet.gemType, player);
    });
    context.setPacketHandled(true);
  }
}
