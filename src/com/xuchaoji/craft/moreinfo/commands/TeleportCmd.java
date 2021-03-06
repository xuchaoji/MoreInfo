package com.xuchaoji.craft.moreinfo.commands;

import com.xuchaoji.craft.moreinfo.ShareLocListener;
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

    private static final String SHARE = "share";

    private static final String GO = "go";

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
                handleAdd(args, player, location);
                break;
            case LIST:
                showList(player);
                break;
            case GOTO:
                gotoLoc(args, player);
                break;
            case SHARE:
                shareLocation(args, player);
                break;
            case GO:
                gotoPublicLoc(args, player);
                break;
            default:
                onParamError(player);
        }
        return true;
    }

    private void gotoPublicLoc(String[] args, Player player) {
        if (args == null || args.length < 2) {
            return;
        }
        DDLocation publicLocation = configHelper.getPublicLoc(args[1]);
        if (null == publicLocation) {
            onLocError(player);
            return;
        }

        if (!takeDiamond(player, 1)) {
            msgSender.sendPlayerMsg(player, "???????????????????????????????????????????????????????????????????????????", ChatColor.RED);
            return;
        }

        Location targetLoc = publicLocation.getLocation();
        player.teleport(targetLoc);
        msgSender.sendPlayerMsg(player, "???????????????" + args[1]);
    }

    private void shareLocation(String[] args, Player player) {
        if (args == null || args.length < 3) {
            onParamError(player);
            return;
        }
        DDLocation privateLoc = configHelper.getDDLocation(player, args[1]);
        if (null == privateLoc) {
            onLocError(player);
            return;
        }
        DDLocation tmpLoc = privateLoc;
        configHelper.shareDDLocation(tmpLoc, args[2], new ShareLocListener() {
            @Override
            public void onSuccess() {
                msgSender.sendPlayerMsg(player, "???????????????????????????");
                privateLoc.setRemainTimes(0);
                configHelper.saveDDLocation(privateLoc);
            }

            @Override
            public void onNameRepeated() {
                msgSender.sendPlayerMsg(player, "????????????????????????", ChatColor.RED);
            }
        });
    }

    private void handleAdd(String[] args, Player player, Location location) {
        if (canCreateTpPoint(player)) {
            addLocation(args, player, location);
        } else {
            pluginMsg(player);
        }
    }

    private void showList(Player player) {
        String locList = configHelper.getLocList(player);
        if (null != locList) {
            msgSender.sendPlayerMsg(player, "?????????????????????" + locList);
        }
        String pubList = configHelper.getPublicList();
        if (null != pubList) {
            msgSender.sendPlayerMsg(player, "?????????????????????" + pubList);
        }
    }

    private void pluginMsg(Player player) {
        msgSender.sendPlayerMsg(player, "/ddtp add name ????????????name???????????????", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "/ddtp goto name ???????????????name???????????????", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "/ddtp go name ???????????????name?????????????????????", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "/ddtp list ????????????????????????", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "/ddtp share name publicName ????????????name??????????????????????????????publicName???????????????????????????", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "????????????????????????????????????????????????1?????????", ChatColor.GREEN);
        msgSender.sendPlayerMsg(player, "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????", ChatColor.YELLOW);
    }

    private boolean canCreateTpPoint(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (null == itemStack || itemStack.getType() != Material.DIAMOND) {
            return false;
        }
        return true;
    }

    private void gotoLoc(String[] args, Player player) {
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
        msgSender.sendPlayerMsg(player, "???????????????" + targetLocation.getName() + "??? ???????????????" + targetLocation.getRemainTimes(), ChatColor.GREEN);
    }

    private void onParamError(Player player) {
        msgSender.sendPlayerMsg(player, "???????????????/ddtp ?????????????????????", ChatColor.RED);
    }

    private void onLocError(Player player) {
        msgSender.sendPlayerMsg(player, "???????????????.", ChatColor.RED);
    }

    private void addLocation(String[] args, Player player, Location location) {
        if (args.length < 2) {
            msgSender.sendPlayerMsg(player, "????????????.", ChatColor.RED);
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() != Material.DIAMOND) {
            msgSender.sendPlayerMsg(player, "??????????????????????????????????????????????????????????????????", ChatColor.YELLOW);
            return;
        }
        DDLocation ddLocation = new DDLocation(args[1], location, player, false, itemStack.getAmount());
        player.getInventory().setItemInMainHand(null);
        configHelper.saveDDLocation(ddLocation);
        msgSender.sendPlayerMsg(player, "?????????: " + ddLocation.getName() + ", ???????????????????????????????????? " + ddLocation.getRemainTimes());
    }

    private boolean takeDiamond(Player player, int amount) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (null == itemStack || itemStack.getType() != Material.DIAMOND || itemStack.getAmount() < amount) {
            return false;
        }
        int total = itemStack.getAmount();
        itemStack.setAmount(total - amount);
        player.getInventory().setItemInMainHand(itemStack);
        return true;
    }
}
