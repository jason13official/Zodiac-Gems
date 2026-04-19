package io.github.jason13official.zodiac_gems.impl.common.network;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleAbilityC2SPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleDarknessS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleWaterbendS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.UseAbilityC2SPacket;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ZodiacNetwork {

  public static SimpleChannel INSTANCE;

  /// called during FMLCommonSetupEvent in [ZodiacGems]
  public static void init() {
    INSTANCE = ChannelBuilder.named(ZodiacGems.identifier("network")).networkProtocolVersion(1).clientAcceptedVersions(Channel.VersionTest.exact(1)).simpleChannel();

    INSTANCE.messageBuilder(UseAbilityC2SPacket.class).encoder(UseAbilityC2SPacket::encode).decoder(UseAbilityC2SPacket::new).consumerMainThread(UseAbilityC2SPacket::handle).add();
    INSTANCE.messageBuilder(ToggleAbilityC2SPacket.class).encoder(ToggleAbilityC2SPacket::encode).decoder(ToggleAbilityC2SPacket::new).consumerMainThread(ToggleAbilityC2SPacket::handle).add();
    INSTANCE.messageBuilder(ToggleDarknessS2CPacket.class).encoder(ToggleDarknessS2CPacket::encode).decoder(ToggleDarknessS2CPacket::new).consumerMainThread(ToggleDarknessS2CPacket::handle).add();
    INSTANCE.messageBuilder(ToggleWaterbendS2CPacket.class).encoder(ToggleWaterbendS2CPacket::encode).decoder(ToggleWaterbendS2CPacket::new).consumerMainThread(ToggleWaterbendS2CPacket::handle).add();
  }
}
