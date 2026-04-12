package com.BTCK.qltv.dashboard;

public class Module {
    private String name;
    private int iconId;

    public Module(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }
    public int getIconId() {
        return iconId;
    }
}