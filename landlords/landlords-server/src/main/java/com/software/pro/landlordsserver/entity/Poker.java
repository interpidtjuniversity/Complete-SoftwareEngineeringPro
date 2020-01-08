package com.software.pro.landlordsserver.entity;

public class Poker {

    private int level;
    private int type;

    public Poker(){
        level = 0;
        type = 0;
    }

    public Poker(int type, int level){
        this.level = level;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
