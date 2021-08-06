package com.xuchaoji.craft.moreinfo.util;

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
}
