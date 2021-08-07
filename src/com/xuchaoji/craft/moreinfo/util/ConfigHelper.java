package com.xuchaoji.craft.moreinfo.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigHelper {
    private static ConfigHelper instance;

    private static JavaPlugin plugin;

    private static FileConfiguration config;

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

    private void saveConfig() {
        plugin.saveConfig();
    }
}
