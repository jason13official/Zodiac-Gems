package io.github.jason13official.zodiac_gems.impl.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UseAbilityPacket {

  public UseAbilityPacket() {}

  public UseAbilityPacket(FriendlyByteBuf data) {}

  public void encode(FriendlyByteBuf data) {}

  public static void handle(UseAbilityPacket packet, CustomPayloadEvent.Context context) {}

}
