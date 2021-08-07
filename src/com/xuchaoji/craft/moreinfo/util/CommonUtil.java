package com.xuchaoji.craft.moreinfo.util;

import com.xuchaoji.craft.moreinfo.constants.Constant;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommonUtil {
    public static JavaPlugin plugin;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(JavaPlugin plugin) {
        CommonUtil.plugin = plugin;
    }

    public static String getFormatTime(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static String getReadableTime(long time) {
        String sdf = "YYYY-MM-dd hh:mm:ss";
        return getFormatTime(sdf, time);
    }

    public static long getOfflineTime(PlayerJoinEvent event) {
        if (null == event) {
            return 0;
        }
        Player player = event.getPlayer();
        long time = System.currentTimeMillis() - player.getLastPlayed();
        if (time <= 0) {
            return 0;
        }
        return time;
    }

    public static long getOfflineHour(PlayerJoinEvent event) {
        return getOfflineTime(event) / Constant.ONE_HOUR;
    }

    public static long getOfflineMinutes(PlayerJoinEvent event) {
        return getOfflineTime(event) / Constant.ONE_MIN;
    }

    public static void executeCommand(String command) {
        if (null == command) {
            return;
        }
        Server server = getPlugin().getServer();
        CommandSender sender = server.getConsoleSender();
        try {
            boolean success = Bukkit.getScheduler().callSyncMethod(getPlugin(), () -> Bukkit.dispatchCommand(sender, command)).get();
            if (success) {
                MsgSender.getInstance(getPlugin()).sendConsoleMsg("execute command: " + command);
            }
        } catch (Exception e) {
            MsgSender.getInstance(getPlugin()).sendConsoleMsg("execute command error: " + e.getClass().getSimpleName());
        }
    }

    public static boolean playerInList(String playerName, String configPath) {
        ConfigHelper configHelper = ConfigHelper.getInstance(CommonUtil.plugin);
        List<String> players = configHelper.getStringList(configPath);
        return !ListUtil.isEmpty(players) && players.contains(playerName);
    }

}
