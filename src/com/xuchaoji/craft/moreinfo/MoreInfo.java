package com.xuchaoji.craft.moreinfo;

import com.xuchaoji.craft.moreinfo.commands.GetBlokInfoCmd;
import com.xuchaoji.craft.moreinfo.commands.TeleportCmd;
import com.xuchaoji.craft.moreinfo.constants.CommandStrs;
import com.xuchaoji.craft.moreinfo.events.PlayerJoinListener;
import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.ConfigHelper;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreInfo extends JavaPlugin {
    private Server server;
    private MsgSender msgSender;
    private PluginManager pluginManager;
    private ConfigHelper configHelper;

    @Override
    public void onDisable() {
        msgSender.sendConsoleMsg("MoreInfo disabled.");
    }

    @Override
    public void onEnable() {
        init();
        registerListeners();
        registerCommands();
        msgSender.sendConsoleMsg("MoreInfo enabled.");
    }

    private void registerListeners() {
        pluginManager.registerEvents(new PlayerJoinListener(), this);
    }

    private void registerCommands() {
        this.getCommand(CommandStrs.GET_BLOCK_INFO).setExecutor(new GetBlokInfoCmd());
        this.getCommand(CommandStrs.TEL_CMD).setExecutor(new TeleportCmd());
    }


    private void init() {
        msgSender = MsgSender.getInstance(this);
        server = getServer();
        pluginManager = server.getPluginManager();
        configHelper = ConfigHelper.getInstance(this);
        CommonUtil.setPlugin(this);
    }
}
