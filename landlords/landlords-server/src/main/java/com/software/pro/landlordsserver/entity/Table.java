package com.software.pro.landlordsserver.entity;

import com.software.pro.landlordsserver.enums.TableType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Table {
    private int id;                            //房间号

    private String roomOwner;                  //房主

    private TableType type;                     //pvp pve

    private Map<Integer, ClientSide> clientSideMap;       //map

    private LinkedList<ClientSide> clientSideList;        //list

    private int landlordId = -1;                          //地主id --> Map<Integer, ClientSide>

    private List<Integer> landlordPokers;                   //三张扑克

    private int lastSellClient = -1;                      //上一个玩家

    private int currentSellClient = -1;                   //当前玩家

    private long lastFlushTime;                           //房间状态改变时间

    private long createTime;                              //房间创建时间

    private int winner;
    public Table() {
    }

    public Table(int id) {
        this.id = id;
        this.clientSideMap = new ConcurrentSkipListMap<Integer, ClientSide>();
        this.clientSideList = new LinkedList<ClientSide>();
    }

    public final long getCreateTime() {
        return createTime;
    }

    public final void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public final TableType getType() {
        return type;
    }

    public final void setType(TableType type) {
        this.type = type;
    }

    public final int getCurrentSellClient() {
        return currentSellClient;
    }

    public final void setCurrentSellClient(int currentSellClient) {
        this.currentSellClient = currentSellClient;
    }

    public long getLastFlushTime() {
        return lastFlushTime;
    }

    public void setLastFlushTime(long lastFlushTime) {
        this.lastFlushTime = lastFlushTime;
    }

    public int getLastSellClient() {
        return lastSellClient;
    }

    public void setLastSellClient(int lastSellClient) {
        this.lastSellClient = lastSellClient;
    }

    public int getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(int landlordId) {
        this.landlordId = landlordId;
    }

    public LinkedList<ClientSide> getClientSideList() {
        return clientSideList;
    }

    public void setClientSideList(LinkedList<ClientSide> clientSideList) {
        this.clientSideList = clientSideList;
    }

    public List<Integer> getLandlordPokers() {
        return landlordPokers;
    }

    public void setLandlordPokers(List<Integer> landlordPokers) {
        this.landlordPokers = landlordPokers;
    }

    public final String getRoomOwner() {
        return roomOwner;
    }

    public final void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final Map<Integer, ClientSide> getClientSideMap() {
        return clientSideMap;
    }

    public final void setClientSideMap(Map<Integer, ClientSide> clientSideMap) {
        this.clientSideMap = clientSideMap;
    }

    public int getPlayerNums() {
        return clientSideList.size();
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
    public int getWinner() {
        return winner;
    }
}
