package com.software.pro.landlordsserver.events.RoomEvent;

import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RobotHelper;

import java.awt.*;

public class RoomEventListener_CODE_PVE_ROOM_JOIN implements RoomEventListener{
    public void call(int client_id){                    //client_id 加入pvefa房间
        Object[] data = RobotHelper.showAllTables();
        server.sendMessage(ServerContains.SESSION_ID_MAP.get(client_id),data);
    }
    public void call(int client_id,int table_id){}
}
