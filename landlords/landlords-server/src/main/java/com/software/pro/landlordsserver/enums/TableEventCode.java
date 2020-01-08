package com.software.pro.landlordsserver.enums;

import java.io.Serializable;

public enum TableEventCode implements Serializable {

    CODE_GAME_LANDLORD_ELECT("叫地主"),

    CODE_GAME_PLAY_POKERS("出牌"),

    CODE_GAME_STARTING("游戏开始"),

    CODE_PVE_GAME_STARTING("人机对战游戏开始"),

    CODE_PVE_GAME_LANDLORD_ELECT("人机对战叫地主"),

    CODE_PVE_GAME_PLAY_POKERS("人机对战出牌"),
    ;

    private String msg;

    private TableEventCode(String msg) {
        this.msg = msg;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }
}
