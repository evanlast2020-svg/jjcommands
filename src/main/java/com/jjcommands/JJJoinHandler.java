package com.jjcommands;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class JJJoinHandler {

    private static final Random RNG = new Random();

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        CompoundTag data = player.getPersistentData();
        if (data.getBoolean("jj_assigned")) return;
        data.putBoolean("jj_assigned", true);

        String tier = Techniques.randomTier();
        List<Techniques.Technique> pool = Techniques.getTier(tier);
        Techniques.Technique t = pool.get(RNG.nextInt(pool.size()));

        boolean ok = TechApplier.apply(player, t);
        if (!ok) return;

        String color = Techniques.color(tier);
        player.sendSystemMessage(Component.literal("§8§l[§6§lJujutsuCraft§8§l] §r§fЛаскаво просимо, §e" + player.getName().getString() + "§f!"));
        player.sendSystemMessage(Component.literal("§fТвоя прокльота техніка визначена..."));
        player.sendSystemMessage(Component.literal("§8[§6Technique§8] " + color + "[" + tier + "] " + t.name() + "§f!"));
    }
}
