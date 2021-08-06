package com.xuchaoji.craft.moreinfo;

import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreInfo extends JavaPlugin {
    private MsgSender msgSender;

    @Override
    public void onDisable() {
        msgSender.sendConsoleMsg("MoreInfo disabled.");
    }

    @Override
    public void onEnable() {
        msgSender = MsgSender.getInstance(this);
        msgSender.sendConsoleMsg("MoreInfo enabled.");
    }
}
