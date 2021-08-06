package com.xuchaoji.craft.moreinfo;

import com.xuchaoji.craft.moreinfo.events.PlayerJoinListener;
import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreInfo extends JavaPlugin {
    private Server server;
    private MsgSender msgSender;
    private PluginManager pluginManager;

    @Override
    public void onDisable() {
        msgSender.sendConsoleMsg("MoreInfo disabled.");
    }

    @Override
    public void onEnable() {
        init();
        registerListeners();
        msgSender.sendConsoleMsg("MoreInfo enabled.");
    }

    private void registerListeners() {
        pluginManager.registerEvents(new PlayerJoinListener(), this);
    }

    private void init() {
        msgSender = MsgSender.getInstance(this);
        server = getServer();
        pluginManager = server.getPluginManager();
        CommonUtil.setPlugin(this);
    }
}
