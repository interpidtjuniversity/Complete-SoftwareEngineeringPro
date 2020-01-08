package com.software.pro.landlordsserver.data;

import com.software.pro.landlordsserver.entity.ClientSide;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerContains {
    //大厅预存
    public final static Map<Integer, ClientSide> CLIENTSIDE_MAP = new ConcurrentSkipListMap<>(); //客户端id, 客户端
    //大厅预存
    public final static Map<Integer, Session> SESSION_ID_MAP = new ConcurrentHashMap<>(); //客户端id , session

    public final static Map<Integer,String> CLIENT_ID_NAME = new ConcurrentSkipListMap<>();    //id ,用户名

    //玩家的唯一id由服务器产生
    private final static AtomicInteger CLIENT_ATOMIC_ID = new AtomicInteger(1);

    private final static AtomicInteger SERVER_ATOMIC_ID = new AtomicInteger(1);

    //服务器分配 客户端id
    public final static int getClientId() {
        return CLIENT_ATOMIC_ID.getAndIncrement();
    }
    //服务器分配 房  间id
    public final static int getTableId() {
        return SERVER_ATOMIC_ID.getAndIncrement();
    }

}
