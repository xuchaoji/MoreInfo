package com.xuchaoji.craft.moreinfo.events;

import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private Player player;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.player = event.getPlayer();

        long lastPlay = player.getLastPlayed();
        String msg = "Welcome "
                + player.getDisplayName() + ", last play time: "
                + ChatColor.GOLD + CommonUtil.getReadableTime(lastPlay);
        MsgSender.getInstance(CommonUtil.getPlugin()).sendPlayerMsg(player, msg);

    }
}
