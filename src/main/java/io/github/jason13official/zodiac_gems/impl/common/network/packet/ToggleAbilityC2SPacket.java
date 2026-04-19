package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.impl.common.ability.PlayerAbilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ToggleAbilityC2SPacket {

  public ToggleAbilityC2SPacket() {}

  public ToggleAbilityC2SPacket(FriendlyByteBuf data) {}

  public void encode(FriendlyByteBuf data) {}

  public static void handle(ToggleAbilityC2SPacket packet, CustomPayloadEvent.Context context) {
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null) return;
      GemType type = GemType.getHeldGem(player);
      if (type == null) return;
      PlayerAbilityTracker.cycle(player, type);
    });
    context.setPacketHandled(true);
  }
}
