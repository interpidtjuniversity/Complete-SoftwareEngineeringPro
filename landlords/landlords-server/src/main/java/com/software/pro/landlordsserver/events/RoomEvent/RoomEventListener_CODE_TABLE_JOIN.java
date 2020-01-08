package com.software.pro.landlordsserver.events.RoomEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.TableEventCode;
import com.software.pro.landlordsserver.events.TableEvent.TableEventListener;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import javax.websocket.Session;
import java.util.LinkedList;
import java.util.Map;

public class RoomEventListener_CODE_TABLE_JOIN implements RoomEventListener {
    public void call(int client_id,int table_id){
        //********************************************写数据**********************************************************//
        //client_id 加入table_id
        //变量
        Table table = RoomsContains.TABLE_MAP.get(table_id);
        ClientSide clientSide = ServerContains.CLIENTSIDE_MAP.get(client_id);
        LinkedList<ClientSide> clientSideLinkedList = table.getClientSideList();
        Map<Integer, ClientSide>integerClientSideMap = table.getClientSideMap();
        //设置从属关系 修改变量
        clientSideLinkedList.getLast().setNext(clientSide);
        clientSide.setPre(clientSideLinkedList.getLast());
        if(table.getPlayerNums() == 2){             //如果没加入前有两人
            clientSide.setNext(clientSideLinkedList.getFirst());
            clientSideLinkedList.getFirst().setPre(clientSide);
        }
        //修改变量
        clientSide.setTableId(table_id);
        ServerContains.CLIENTSIDE_MAP.put(client_id, clientSide);
        clientSideLinkedList.add(clientSide);
        integerClientSideMap.put(client_id, clientSide);
        table.setClientSideList(clientSideLinkedList);
        table.setClientSideMap(integerClientSideMap);
        //table
        RoomsContains.TABLE_MAP.put(table_id,table);
        //session onlinecount
        Map<Integer, Session>sessionMap = RoomsContains.Tables_Sessions.get(table_id);
        sessionMap.put(client_id, ServerContains.SESSION_ID_MAP.get(client_id));
        RoomsContains.Tables_Sessions.put(table_id,sessionMap);
        RoomsContains.Tables_OnLineCount.put(table_id,table.getPlayerNums());
        //index
        Map<Integer,Integer>integerIntegerMap = RoomsContains.Tables_Clients_Index.get(table_id);
        integerIntegerMap.put(client_id,table.getPlayerNums() + 1);
        //返回数据
        Object[]data = RoomTableHelper.joinTable(table_id);
        try {
            server.sendAll(table_id, data);
        }
        catch (Exception e){e.printStackTrace();}
        //如果人数满三个
        if(RoomsContains.TABLE_MAP.get(table_id).getPlayerNums() == 3){
            //游戏开始
            TableEventListener.get(TableEventCode.CODE_GAME_STARTING).call(table_id);
        }


    }
    public void call(int client_id){}
}
