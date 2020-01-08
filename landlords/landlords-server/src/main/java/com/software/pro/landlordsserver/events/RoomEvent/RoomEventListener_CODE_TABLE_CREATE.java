package com.software.pro.landlordsserver.events.RoomEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.TableType;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

public class RoomEventListener_CODE_TABLE_CREATE implements RoomEventListener{

    public void call(int client_id,int table_id){}
    public void call(int client_id){
        //client_id的玩家要创建桌子
        int newtableId = ServerContains.getTableId();
        Table newTable = new Table(newtableId);
        ClientSide clientSide = ServerContains.CLIENTSIDE_MAP.get(client_id);
        //玩家加入房间
        clientSide.setTableId(newtableId);      //修改客户端信息
        //
        //设置房间
        newTable.setRoomOwner(ServerContains.CLIENT_ID_NAME.get(client_id));
        newTable.getClientSideList().add(clientSide);
        newTable.getClientSideMap().put(client_id, clientSide);
        newTable.setType(TableType.PVP);
        //修改变量
        ServerContains.CLIENTSIDE_MAP.put(client_id, clientSide);
        //table onlinecount
        RoomsContains.TABLE_MAP.put(newtableId,newTable);
        RoomsContains.Tables_OnLineCount.put(newtableId,1);    //房间一个人
        //session
        Map<Integer, Session>sessionHashMap = new HashMap<>();
        sessionHashMap.put(client_id,ServerContains.SESSION_ID_MAP.get(client_id));
        RoomsContains.Tables_Sessions.put(newtableId,sessionHashMap);
        //index
        Map<Integer,Integer>integerIntegerMap = new HashMap<>();
        integerIntegerMap.put(client_id,0);
        RoomsContains.Tables_Clients_Index.put(newtableId,integerIntegerMap);
        //发送数据
        //发送玩家信息 创建桌子的人只需要发送给自己
        Object[] dataplayers = RoomTableHelper.joinTable(newtableId);
        server.sendMessage(ServerContains.SESSION_ID_MAP.get(client_id),dataplayers);
        //发送tableid
        Object[] datatable = RoomTableHelper.createTable(newtableId);
        server.sendMessage(ServerContains.SESSION_ID_MAP.get(client_id),datatable);
    }
}
