package com.xuchaoji.craft.moreinfo.commands;

import com.xuchaoji.craft.moreinfo.constants.Constant;
import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public class GetBlokInfoCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        MsgSender msgSender = MsgSender.getInstance(CommonUtil.plugin);
        msgSender.sendConsoleMsg("onCommand.");
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Block block = player.getTargetBlockExact(Constant.MAX_VIEW_DISTANCE);
            if (null == block) {
                msgSender.sendPlayerMsg(player, "no block within " + Constant.MAX_VIEW_DISTANCE + " blocks.");
                return false;
            }

            BlockData blockData = block.getBlockData();

            String blockStr = blockData == null ? "empty info" : blockData.getAsString();

            msgSender.sendPlayerMsg(player, "blockInfo: " + blockStr);

            return true;
        } else {
            msgSender.sendConsoleMsg(ChatColor.RED + "this command can only send by player");
            return false;
        }
    }
}
