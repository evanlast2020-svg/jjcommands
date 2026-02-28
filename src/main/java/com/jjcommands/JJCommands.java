package com.jjcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class JJCommands {

    private static final Random RNG = new Random();

    @SubscribeEvent
    public static void onCommands(RegisterCommandsEvent event) {

        // /roll <tier> <player>
        event.getDispatcher().register(
            Commands.literal("roll")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("tier", StringArgumentType.word())
                    .suggests((ctx, b) -> { b.suggest("S"); b.suggest("A"); b.suggest("B"); b.suggest("C"); return b.buildFuture(); })
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> {
                            String tier = StringArgumentType.getString(ctx, "tier").toUpperCase();
                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                            List<Techniques.Technique> pool = Techniques.getTier(tier);

                            if (pool == null) {
                                ctx.getSource().sendFailure(Component.literal("§cНевірний тір! Використай S, A, B або C."));
                                return 0;
                            }

                            Techniques.Technique t = pool.get(RNG.nextInt(pool.size()));
                            boolean ok = TechApplier.apply(player, t);

                            if (!ok) {
                                ctx.getSource().sendFailure(Component.literal("§cПомилка! JujutsuCraft не знайдено або несумісна версія."));
                                return 0;
                            }

                            String color = Techniques.color(tier);
                            String msg = "§8[§6Technique Roll§8] §f" + player.getName().getString() + " отримав " + color + "[" + tier + "] " + t.name() + "§f!";
                            ctx.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal(msg), false);
                            player.sendSystemMessage(Component.literal("§8[§6Technique Roll§8] §fТвоя техніка: " + color + "[" + tier + "] " + t.name() + "§f!"));
                            return 1;
                        })
                    )
                )
        );

        // /grant <player> <technique>
        event.getDispatcher().register(
            Commands.literal("grant")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("technique", StringArgumentType.greedyString())
                        .suggests((ctx, b) -> { Techniques.ALL.forEach(t -> b.suggest(t.name().replace(" ", "_"))); return b.buildFuture(); })
                        .executes(ctx -> {
                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                            String input = StringArgumentType.getString(ctx, "technique").replace("_", " ");
                            Techniques.Technique t = Techniques.findByName(input);

                            if (t == null) {
                                ctx.getSource().sendFailure(Component.literal("§cТехніка \"" + input + "\" не знайдена!"));
                                return 0;
                            }

                            boolean ok = TechApplier.apply(player, t);
                            if (!ok) {
                                ctx.getSource().sendFailure(Component.literal("§cПомилка застосування техніки."));
                                return 0;
                            }

                            String color = Techniques.color(t.tier());
                            ctx.getSource().sendSuccess(() -> Component.literal("§aВидано " + color + "[" + t.tier() + "] " + t.name() + "§a гравцю §e" + player.getName().getString()), false);
                            player.sendSystemMessage(Component.literal("§8[§6JujutsuCraft§8] §fТи отримав: " + color + "[" + t.tier() + "] " + t.name() + "§f!"));
                            return 1;
                        })
                    )
                )
        );
    }
}
