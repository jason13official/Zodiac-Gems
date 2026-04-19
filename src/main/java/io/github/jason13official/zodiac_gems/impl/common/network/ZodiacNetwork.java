package io.github.jason13official.zodiac_gems.impl.common.network;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.ToggleAbilityPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.UseAbilityPacket;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ZodiacNetwork {

  public static SimpleChannel INSTANCE;

  /// called during FMLCommonSetupEvent in [ZodiacGems]
  public static void init() {
    INSTANCE = ChannelBuilder.named(ZodiacGems.identifier("network")).networkProtocolVersion(1).clientAcceptedVersions(Channel.VersionTest.exact(1)).simpleChannel();

    INSTANCE.messageBuilder(UseAbilityPacket.class).encoder(UseAbilityPacket::encode).decoder(UseAbilityPacket::new).consumerMainThread(UseAbilityPacket::handle).add();
    INSTANCE.messageBuilder(ToggleAbilityPacket.class).encoder(ToggleAbilityPacket::encode).decoder(ToggleAbilityPacket::new).consumerMainThread(ToggleAbilityPacket::handle).add();
  }
}
