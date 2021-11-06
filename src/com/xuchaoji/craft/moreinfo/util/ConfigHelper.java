package com.xuchaoji.craft.moreinfo.util;

import com.xuchaoji.craft.moreinfo.datas.DDLocation;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class ConfigHelper {
    private static ConfigHelper instance;

    private static JavaPlugin plugin;

    private static FileConfiguration config;

    private static final String LOC = ".locStr";

    private static final String IS_PERMANENT = ".isPermanent";

    private static final String REMAIN = ".remainTimes";

    private static final String LOC_PLAYER_PREFIX = "ddtp.list.";

    public static ConfigHelper getInstance(JavaPlugin plugin) {
        if (null == instance) {
            synchronized (ConfigHelper.class) {
                if (null == instance) {
                    instance = new ConfigHelper(plugin);
                }
            }
        }
        return instance;
    }

    public ConfigHelper(JavaPlugin plugin) {
        init(plugin);
    }

    private void init(JavaPlugin plugin) {
        ConfigHelper.plugin = plugin;
        config = plugin.getConfig();
        config.addDefault("MoreInfoPluginEnabled", true);
        plugin.saveConfig();
    }

    public void saveString(String path, String value) {
        if (null == path) {
            return;
        }
        synchronized (this) {
            config.set(path, value);
            saveConfig();
        }
    }

    public String getString(String path) {
        if (null == path) {
            return null;
        }
        synchronized (this) {
            return config.getString(path);
        }
    }

    public void saveBoolean(String path, boolean value) {
        if (null == path) {
            return;
        }
        synchronized (this) {
            config.set(path, value);
            saveConfig();
        }
    }

    public boolean getBoolean(String path) {
        if (null == path) {
            return false;
        }
        synchronized (this) {
            return config.getBoolean(path);
        }
    }

    public void saveInt(String path, int value) {
        if (null == path) {
            return;
        }
        synchronized (this) {
            config.set(path, value);
            saveConfig();
        }
    }

    public int getInt(String path) {
        if (null == path) {
            return 0;
        }
        synchronized (this) {
            return config.getInt(path);
        }
    }

    public void saveStringList(String path, List<String> list) {
        if (null == path) {
            return;
        }
        synchronized (this) {
            config.set(path, list);
            saveConfig();
        }
    }

    public List<String> getStringList(String path) {
        if (null == path) {
            return null;
        }
        synchronized (this) {
            return config.getStringList(path);
        }
    }

    public void saveDDLocation(DDLocation ddLocation) {
        synchronized (this) {
            Player player = ddLocation.getCreater();
            String nameAndPath = LOC_PLAYER_PREFIX + player.getDisplayName() + "." + ddLocation.getName();
            String locStr = CommonUtil.getLocStr(ddLocation.getLocation());
            boolean isPermanent = false;
            int remainTimes = ddLocation.getRemainTimes();
            if (remainTimes <= 0) {
                saveString(nameAndPath, null);
                return;
            }
            saveString(nameAndPath + LOC, locStr);
            saveBoolean(nameAndPath + IS_PERMANENT, isPermanent);
            saveInt(nameAndPath + REMAIN, remainTimes);
        }
    }

    public String getLocList(Player player) {
        synchronized (this) {
            String path = LOC_PLAYER_PREFIX + player.getDisplayName();
            Set<String> list = getSections(path);
            StringBuilder sb = new StringBuilder();
            for (String name : list) {
                sb.append(name).append(", ");
            }
            return sb.toString().trim();
        }
    }

    public Set<String> getSections(String path) {
        synchronized (this) {
            return config.getConfigurationSection(path).getKeys(false);
        }
    }

    public DDLocation getDDLocation(Player player, String locName) {
        if (null == player || null == locName || "".equalsIgnoreCase(locName)) {
            return null;
        }
        synchronized (this) {
            try {
                String path = LOC_PLAYER_PREFIX + player.getDisplayName() + "." + locName;
                Location location = CommonUtil.getLocationByStr(getString(path + LOC));
                boolean isPermanent = getBoolean(path + IS_PERMANENT);
                int remain = getInt(path + REMAIN);
                DDLocation ddLocation = new DDLocation(locName, location, player, isPermanent, remain);
                return ddLocation;
            } catch (Throwable throwable) {
                return null;
            }
        }
    }

    private void saveConfig() {
        plugin.saveConfig();
    }
}
