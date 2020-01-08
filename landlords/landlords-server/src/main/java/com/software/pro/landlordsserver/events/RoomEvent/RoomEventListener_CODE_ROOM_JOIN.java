package com.software.pro.landlordsserver.events.RoomEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

public class RoomEventListener_CODE_ROOM_JOIN implements RoomEventListener{

    public void call(int client_id,int table_id){}
    public void call(int client_id){
        //这个玩家查看桌子列表
        Object[]data = RoomTableHelper.showAllTables();
        server.sendMessage(ServerContains.SESSION_ID_MAP.get(client_id),data);
    };
}
