package com.xuchaoji.craft.moreinfo.events;

import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.ConfigHelper;
import com.xuchaoji.craft.moreinfo.util.ListUtil;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

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

        if (!CommonUtil.playerInList(player.getDisplayName(), "weather-control.players")) {
            return;
        }
        MsgSender msgSender = MsgSender.getInstance(CommonUtil.getPlugin());
        switch (msg) {
            case "雨天":
                CommonUtil.executeCommand("weather rain");
                notifyWeatherChange(player, msgSender, msg);
                break;
            case "雷雨":
                notifyWeatherChange(player, msgSender, msg);
                CommonUtil.executeCommand("weather thunder");
                break;
            case "晴天":
                notifyWeatherChange(player, msgSender, msg);
                CommonUtil.executeCommand("weather clear");
                break;
        }
    }

    private void notifyWeatherChange(Player player, MsgSender msgSender, String msg) {
        msgSender.sendConsoleMsg(player + "call: " + msg);
        msgSender.sendBroadcast(ChatColor.GOLD + player.getDisplayName() +
                ChatColor.WHITE + "召唤了" + ChatColor.AQUA + msg);
    }

    private void lastLoginNotify(Player player, MsgSender sender) {
        long lastPlay = player.getLastPlayed();
        String msg = "欢迎 "
                + player.getDisplayName() + ", 上次在线时间: "
                + ChatColor.GOLD + CommonUtil.getReadableTime(lastPlay);
        sender.sendPlayerMsg(player, msg);
    }

    private void handleOfflineExp(PlayerJoinEvent event, MsgSender sender) {
        ConfigHelper configHelper = ConfigHelper.getInstance(CommonUtil.plugin);
        saveDefault(configHelper);
        Player player = event.getPlayer();
        if (!CommonUtil.playerInList(player.getDisplayName(), "offline-exp.players")) {
            sender.sendConsoleMsg("player " + player.getDisplayName() + " not in offline exp list");
            return;
        }
        int multi = configHelper.getInt("offline-exp.multi." + player.getDisplayName());
        if (multi == 0) {
            multi = 50;
        }
        long offLineHour = CommonUtil.getOfflineHour(event);
        if (offLineHour > 48) {
            sender.sendPlayerMsg(player, "离线经验最多只能存储两天哦。");
            offLineHour = 48;
        }
        long experience = offLineHour * multi;
        if (experience > Integer.MAX_VALUE) {
            experience = 0;
        }
        String offExpMsg = "获得了 " + ChatColor.YELLOW + experience
                + ChatColor.WHITE + " 离线经验.";
        player.giveExp((int)experience);
        sender.sendPlayerMsg(player, "你" + offExpMsg);
        sender.sendConsoleMsg(player.getDisplayName() + " " + offExpMsg);
    }

    private void saveDefault(ConfigHelper configHelper) {
        configHelper.saveInt("offline-exp.multi.chaoji", 100);
        configHelper.saveInt("offline-exp.multi.Moving_Bricks", 100);
    }
}
