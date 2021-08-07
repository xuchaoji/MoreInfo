package com.xuchaoji.craft.moreinfo.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OnlinePlayers {
    public static List<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static void addPlayers(Collection<Player> players) {

    }

    public static void removePlayer(Player player) {
        players.remove(player);
    }

    public static void clearPlayers() {
        players.clear();
    }
}
