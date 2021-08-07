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

        MsgSender sender = MsgSender.getInstance(CommonUtil.getPlugin());

        long lastPlay = player.getLastPlayed();
        String msg = "Welcome "
                + player.getDisplayName() + ", last play time: "
                + ChatColor.GOLD + CommonUtil.getReadableTime(lastPlay);
        sender.sendPlayerMsg(player, msg);
        long offLineHour = CommonUtil.getOfflineHour(event);
        long experience = offLineHour * 100;
        if (experience > Integer.MAX_VALUE) {
            experience = 0;
        }
        String offExpMsg = " got " + ChatColor.YELLOW + experience
                + ChatColor.WHITE + " offline experience.";
        player.giveExp((int)experience);
        sender.sendPlayerMsg(player, "You" + offExpMsg);
        sender.sendConsoleMsg(player.getDisplayName() + offExpMsg);
    }
}
