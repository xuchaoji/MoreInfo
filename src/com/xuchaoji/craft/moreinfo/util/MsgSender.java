package com.xuchaoji.craft.moreinfo.util;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class MsgSender {
    private volatile static MsgSender instance;

    private JavaPlugin plugin;

    private Server server;

    private ConsoleCommandSender sender;

    public static MsgSender getInstance(JavaPlugin plugin) {
        if (null == instance) {
            synchronized (MsgSender.class) {
                if (null == instance) {
                    instance = new MsgSender(plugin);
                }
            }
        }
        return instance;
    }

    public MsgSender(JavaPlugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.sender = server.getConsoleSender();
    }

    public void sendConsoleMsg(String msg) {
        sender.sendMessage(msg);
    }
}
