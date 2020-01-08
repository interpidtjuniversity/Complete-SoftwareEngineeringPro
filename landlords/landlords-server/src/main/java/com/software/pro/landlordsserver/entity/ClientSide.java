package com.software.pro.landlordsserver.entity;

import com.software.pro.landlordsserver.enums.ClientRole;
import com.software.pro.landlordsserver.enums.ClientType;

import javax.websocket.*;
import java.util.List;

public class ClientSide {
    private String owner_name;           // 用户名
    private int id;                      // id
    private int tableId;                  //房间id
    private List<Integer> pokers;
    private ClientRole role;            //玩家 人机
    private ClientType type;            //地主 农民
    private ClientSide next;
    private ClientSide pre;
    private transient Session session;
    private int level;

    public ClientSide() {}

    public ClientSide(int id, Session session, String owner_name,ClientRole clientRole) {
        this.id = id;
        this.session = session;
        this.owner_name = owner_name;
        this.role = clientRole;
    }

    public ClientSide(int id, String robot_name, ClientRole clientRole){
        this.id = id;
        this.owner_name = robot_name;
        this.role = clientRole;
    }

    public void init() {
        tableId = -1;
        pokers = null;
        type = null;
        next = null;
        pre = null;
    }


    public String getOwner_name() {
        return owner_name;
    }
    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }
    public void setChannel(Session session) {
        this.session = session;
    }

    public ClientRole getRole() {
        return role;
    }
    public void setRole(ClientRole role) {
        this.role = role;
    }

    public ClientSide getNext() {
        return next;
    }
    public void setNext(ClientSide next) {
        this.next = next;
    }

    public ClientSide getPre() {
        return pre;
    }
    public void setPre(ClientSide pre) {
        this.pre = pre;
    }

    public List<Integer> getPokers() {
        return pokers;
    }
    public void setPokers(List<Integer> pokers) {
        this.pokers = pokers;
    }

    public ClientType getType() {
        return type;
    }
    public void setType(ClientType type) {
        this.type = type;
    }

    public int getTableId() {
        return tableId;
    }
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
