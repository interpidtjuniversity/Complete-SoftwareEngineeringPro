package com.software.pro.landlordsserver.enums;

import java.io.Serializable;

public enum RoomEventCode implements Serializable {

    CODE_ROOM_JOIN("加入房间,显示所有桌子"),

    CODE_TABLE_JOIN("加入桌子"),

    CODE_TABLE_CREATE("创建桌子"),

    CODE_PVE_ROOM_JOIN("加入房间,显示所有人机桌子"),

    CODE_PVE_TABLE_JOIN("加入人机桌子"),
    ;


    private String msg;

    private RoomEventCode(String msg) {
        this.msg = msg;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }
}
