package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import io.github.jason13official.zodiac_gems.Constants;
import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.ability.PlayerAbilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.util.GemType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ToggleAbilityC2SPacket implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<ToggleAbilityC2SPacket> TYPE = new CustomPacketPayload.Type<>(ZodiacGems.id("toggle_ability"));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public ToggleAbilityC2SPacket() {
  }

  public ToggleAbilityC2SPacket(FriendlyByteBuf data) {
  }

  public static void handle(ToggleAbilityC2SPacket packet, CustomPayloadEvent.Context context) {

    // Constants.LOG.info("Handling ToggleAbilityPacket.");
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null) {
        return;
      }
      GemType type = GemType.getHeldGem(player);
      if (type == null) {
        return;
      }
      PlayerAbilityTracker.cycle(player, type);
    });
    context.setPacketHandled(true);
  }

  public void encode(FriendlyByteBuf data) {
  }
}
