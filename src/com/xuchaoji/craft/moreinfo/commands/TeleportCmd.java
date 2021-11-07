package com.xuchaoji.craft.moreinfo.commands;

import com.xuchaoji.craft.moreinfo.datas.DDLocation;
import com.xuchaoji.craft.moreinfo.util.CommonUtil;
import com.xuchaoji.craft.moreinfo.util.ConfigHelper;
import com.xuchaoji.craft.moreinfo.util.MsgSender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportCmd implements CommandExecutor {
    private static final String ADD = "add";

    private static final String LIST = "list";

    private static final String GOTO = "goto";

    private static final String LOC_PLAYER_PREFIX = "ddtp.list.";

    private static final String LOC_PUBLIC_PREFIX = "public.ddtp.list.";

    private JavaPlugin plugin = CommonUtil.getPlugin();

    private MsgSender msgSender = MsgSender.getInstance(plugin);

    private ConfigHelper configHelper = new ConfigHelper(plugin);

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            msgSender.sendConsoleMsg(msgSender.dyeMsg("player command only", ChatColor.RED));
        }
        Player player = (Player) commandSender;
        Location location = player.getLocation();
        msgSender.sendConsoleMsg("location: " + CommonUtil.getLocStr(location));
        if (args == null || args.length == 0) {
            pluginMsg(player);
            return true;
        }
        switch (args[0]) {
            case ADD:
                if (canCreateTpPoint(player)) {
                    addLocation(args, player, location);
                } else {
                    pluginMsg(player);
                }
                break;
            case LIST:
                String locList = configHelper.getLocList(player);
                msgSender.sendPlayerMsg(player, "你的位置列表：" + locList);
                break;
            case GOTO:
                gotoLock(args, player);
                break;
            default:
                onParamError(player);
        }
        return true;
    }

    private void pluginMsg(Player player) {
        msgSender.sendPlayerMsg(player, "/ddtp add name 添加名为name的传送点。", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "/ddtp goto name 传送到名为name的传送点。", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "/ddtp list 查看传送点列表。", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "传送点需要手持钻石创建，一颗钻石可传送一次。", ChatColor.YELLOW);
    }

    private boolean canCreateTpPoint(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (null == itemStack || itemStack.getType() != Material.DIAMOND) {
            return false;
        }
        return true;
    }

    private void gotoLock(String[] args, Player player) {
        if (args.length < 2) {
            onParamError(player);
        }
        DDLocation targetLocation = configHelper.getDDLocation(player, args[1]);
        if (null == targetLocation) {
            onLocError(player);
            return;
        }
        Location target = targetLocation.getLocation();
        player.teleport(target);
        int remain = targetLocation.getRemainTimes();
        targetLocation.setRemainTimes(remain - 1);
        configHelper.saveDDLocation(targetLocation);
        msgSender.sendPlayerMsg(player, "已传送到：" + targetLocation.getName() + "， 剩余次数：" + targetLocation.getRemainTimes(), ChatColor.GREEN);
    }

    private void onParamError(Player player) {
        msgSender.sendPlayerMsg(player, "参数错误.", ChatColor.RED);
    }

    private void onLocError(Player player) {
        msgSender.sendPlayerMsg(player, "位置不存在.", ChatColor.RED);
    }

    private void addLocation(String[] args, Player player, Location location) {
        if (args.length < 2) {
            msgSender.sendPlayerMsg(player, "参数错误.", ChatColor.RED);
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() != Material.DIAMOND) {
            msgSender.sendPlayerMsg(player, "传送点需要手持钻石创建，一颗钻石可传送一次。", ChatColor.YELLOW);
            return;
        }
        DDLocation ddLocation = new DDLocation(args[1], location, player, false, itemStack.getAmount());
        player.getInventory().setItemInMainHand(null);
        configHelper.saveDDLocation(ddLocation);
        msgSender.sendPlayerMsg(player, "传送点: " + ddLocation.getName() + ", 创建成功，剩余传送次数： " + ddLocation.getRemainTimes());
    }
}
