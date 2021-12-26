package com.xuchaoji.craft.moreinfo.util;

import com.xuchaoji.craft.moreinfo.constants.Constant;
import com.xuchaoji.craft.moreinfo.datas.DDLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
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

    public static String getLocStr(Location location) {
        return getLocStr(location, ",");
    }

    public static Location getLocationByStr(String locationStr) {
        return getLocationByStr(locationStr, ",");
    }

    public static Location getLocationByStr(String locationStr, String separator) {
        if (locationStr == null) {
            return null;
        }
        String[] params = locationStr.split(separator);
        if (null == params || params.length < 6) {
            return null;
        }
        try {
            World world = Bukkit.getWorld(params[0]);
            double x = Double.valueOf(params[1]);
            double y = Double.valueOf(params[2]);
            double z = Double.valueOf(params[3]);
            float yaw = Float.valueOf(params[4]);
            float pitch = Float.valueOf(params[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Throwable e) {
            return null;
        }
    }

    public static String getLocStr(Location location, String separator) {
        if (null == location) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(location.getWorld().getName()).append(separator)
                .append(location.getX()).append(separator)
                .append(location.getY()).append(separator)
                .append(location.getZ()).append(separator)
                .append(location.getYaw()).append(separator)
                .append(location.getPitch());
        return sb.toString();
    }

    public static DDLocation copyNewLoc(DDLocation source) {
        if (null == source) {
            return null;
        }
        DDLocation copy = new DDLocation(source.getName(), source.getLocation(), source.getCreater(), source.isPublic(), source.getRemainTimes());
        copy.setPermanent(source.isPermanent());
        return copy;
    }
}
