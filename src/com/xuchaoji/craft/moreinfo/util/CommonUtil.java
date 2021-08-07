package com.xuchaoji.craft.moreinfo.util;

import com.xuchaoji.craft.moreinfo.constants.Constant;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;

public class CommonUtil {
    private static JavaPlugin plugin;

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

}
