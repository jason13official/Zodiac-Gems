package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.impl.common.ability.GemAbilityActivator;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UseAbilityC2SPacket {

  // private final GemType gemType;

  public UseAbilityC2SPacket(GemType gemType) {
    // this.gemType = gemType;
  }

  public UseAbilityC2SPacket(FriendlyByteBuf data) {
    // this.gemType = GemType.byId(data.readByte());
  }

  public static void handle(UseAbilityC2SPacket packet, CustomPayloadEvent.Context context) {

    // Constants.LOG.info("Handling UseAbilityPacket.");
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null) {
        return;
      }

      GemAbilityActivator.activate(player);
    });
    context.setPacketHandled(true);
  }

  public void encode(FriendlyByteBuf data) {
    // data.writeByte(this.gemType.getId());
  }
}
