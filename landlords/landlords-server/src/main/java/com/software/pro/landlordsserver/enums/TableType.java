package com.software.pro.landlordsserver.enums;

public enum TableType {
    PVP("玩家对战"),

    PVR("人机对战"),

            ;
    private String msg;

    private TableType(String msg) {
        this.msg = msg;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }
}
