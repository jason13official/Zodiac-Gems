package io.github.jason13official.zodiac_gems.impl.common.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jason13official.zodiac_gems.impl.common.ability.InvisibilityTracker;
import io.github.jason13official.zodiac_gems.impl.common.ability.NametagTracker;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.SyncInvisibilityS2CPacket;
import io.github.jason13official.zodiac_gems.impl.common.network.packet.SyncNametagS2CPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ZodiacCommands {

  public static void register(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

    dispatcher.register(Commands.literal("hide")
        .requires(src -> src.hasPermission(2))
        .then(Commands.argument("hidden", EntityArgument.player())
            .then(Commands.argument("canSee", EntityArgument.player())
                .executes(ctx -> {
                  ServerPlayer hidden = EntityArgument.getPlayer(ctx, "hidden");
                  ServerPlayer canSee = EntityArgument.getPlayer(ctx, "canSee");
                  InvisibilityTracker.hide(hidden.getUUID(), canSee.getUUID());
                  SyncInvisibilityS2CPacket packet = new SyncInvisibilityS2CPacket(hidden.getUUID(), true, canSee.getUUID());
                  for (ServerPlayer p : ctx.getSource().getServer().getPlayerList().getPlayers()) {
                    // ZodiacNetwork.INSTANCE.send(packet, PacketDistributor.PLAYER.with(p));
                    PacketDistributor.sendToPlayer(p, packet);
                  }
                  ctx.getSource().sendSuccess(() -> Component.literal(
                      hidden.getName().getString() + " is now hidden from everyone except " + canSee.getName().getString()), true);
                  return 1;
                }))));

    dispatcher.register(Commands.literal("unhide")
        .requires(src -> src.hasPermission(2))
        .then(Commands.argument("player", EntityArgument.player())
            .executes(ctx -> {
              ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
              InvisibilityTracker.unhide(player.getUUID());
              SyncInvisibilityS2CPacket packet = new SyncInvisibilityS2CPacket(player.getUUID(), false, null);
              for (ServerPlayer p : ctx.getSource().getServer().getPlayerList().getPlayers()) {
                // ZodiacNetwork.INSTANCE.send(packet, PacketDistributor.PLAYER.with(p));
                PacketDistributor.sendToPlayer(p, packet);
              }
              ctx.getSource().sendSuccess(() -> Component.literal(
                  player.getName().getString() + " is now visible to everyone."), true);
              return 1;
            })));

    dispatcher.register(Commands.literal("hidenametag")
        .requires(src -> src.hasPermission(2))
        .then(Commands.argument("player", EntityArgument.player())
            .executes(ctx -> {
              ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
              NametagTracker.hide(player.getUUID());
              SyncNametagS2CPacket packet = new SyncNametagS2CPacket(player.getUUID(), true);
              for (ServerPlayer p : ctx.getSource().getServer().getPlayerList().getPlayers()) {
                // ZodiacNetwork.INSTANCE.send(packet, PacketDistributor.PLAYER.with(p));
                PacketDistributor.sendToPlayer(p, packet);
              }
              ctx.getSource().sendSuccess(() -> Component.literal(
                  player.getName().getString() + "'s nametag is now hidden."), true);
              return 1;
            })));

    dispatcher.register(Commands.literal("shownametag")
        .requires(src -> src.hasPermission(2))
        .then(Commands.argument("player", EntityArgument.player())
            .executes(ctx -> {
              ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
              NametagTracker.show(player.getUUID());
              SyncNametagS2CPacket packet = new SyncNametagS2CPacket(player.getUUID(), false);
              for (ServerPlayer p : ctx.getSource().getServer().getPlayerList().getPlayers()) {
                // ZodiacNetwork.INSTANCE.send(packet, PacketDistributor.PLAYER.with(p));
                PacketDistributor.sendToPlayer(p, packet);
              }
              ctx.getSource().sendSuccess(() -> Component.literal(
                  player.getName().getString() + "'s nametag is now visible."), true);
              return 1;
            })));
  }
}
