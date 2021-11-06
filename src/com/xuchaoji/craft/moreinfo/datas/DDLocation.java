package com.xuchaoji.craft.moreinfo.datas;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DDLocation {
    private String name;

    private Location location;

    private Player creater;

    private boolean isPublic;

    private boolean isPermanent;

    private int remainTimes;

    public DDLocation(String name, Location location, Player creater, boolean isPublic, boolean isPermanent) {
        this.name = name;
        this.location = location;
        this.creater = creater;
        this.isPublic = isPublic;
        this.isPermanent = isPermanent;
    }

    public DDLocation(String name, Location location, Player creater, boolean isPublic, int remainTimes) {
        this.name = name;
        this.location = location;
        this.creater = creater;
        this.isPublic = isPublic;
        this.remainTimes = remainTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Player getCreater() {
        return creater;
    }

    public void setCreater(Player creater) {
        this.creater = creater;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public int getRemainTimes() {
        return isPermanent ? Integer.MAX_VALUE : remainTimes;
    }

    public void setRemainTimes(int remainTimes) {
        if (isPermanent) {
            return;
        }
        this.remainTimes = remainTimes;
    }
}
