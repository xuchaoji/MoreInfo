package com.xuchaoji.craft.moreinfo.events;

import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MsgSender sender = MsgSender.getInstance(CommonUtil.getPlugin());

        lastLoginNotify(player, sender);
        handleOfflineExp(event, sender);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        if (null == msg) {
            return;
        }
        MsgSender msgSender = MsgSender.getInstance(CommonUtil.getPlugin());
        if ("雨天".equals(msg)) {
            msgSender.sendConsoleMsg( player + "call rain.");
            CommonUtil.executeCommand("weather rain");
        } else if ("雷雨".equals(msg)) {
            msgSender.sendConsoleMsg( player + "call thunder.");
            CommonUtil.executeCommand("weather thunder");
        } else if ("晴天".equals(msg)) {
            msgSender.sendConsoleMsg( player + "call sun.");
            CommonUtil.executeCommand("weather clear");
        }
    }

    private void lastLoginNotify(Player player, MsgSender sender) {
        long lastPlay = player.getLastPlayed();
        String msg = "Welcome "
                + player.getDisplayName() + ", last play time: "
                + ChatColor.GOLD + CommonUtil.getReadableTime(lastPlay);
        sender.sendPlayerMsg(player, msg);
    }

    private void handleOfflineExp(PlayerJoinEvent event, MsgSender sender) {
        long offLineHour = CommonUtil.getOfflineHour(event);
        long experience = offLineHour * 100;
        if (experience > Integer.MAX_VALUE) {
            experience = 0;
        }
        Player player = event.getPlayer();
        String offExpMsg = " got " + ChatColor.YELLOW + experience
                + ChatColor.WHITE + " offline experience.";
        player.giveExp((int)experience);
        sender.sendPlayerMsg(player, "You" + offExpMsg);
        sender.sendConsoleMsg(player.getDisplayName() + offExpMsg);
    }
}
