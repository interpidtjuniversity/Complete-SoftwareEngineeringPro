package com.software.pro.landlordsserver.events.TableEvent;

import com.alibaba.fastjson.JSONArray;
import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TableEventListener_CODE_GAME_PLAY_POKERS implements TableEventListener{
    public void call(int table_id){}
    public void call(int client_id,int op){}
    public void call(int client_id,Object message,int flag){
        Integer[] playersellpokers = JSONArray.parseObject(message.toString(),Integer[].class);
        List<Integer>pokerslist = new ArrayList<Integer>(Arrays.asList(playersellpokers));
        RoomTableHelper.Clear_Client_Pokers(client_id,pokerslist);

        //将出牌信息直接转发
        Object[] data = RoomTableHelper.getPokersPlayInfo(client_id,message);
        ClientSide clientSide = ServerContains.CLIENTSIDE_MAP.get(client_id);
        int table_id = clientSide.getTableId();

        try {
            server.sendAll(table_id, data);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //游戏结束
        if(RoomTableHelper.IsGameOver(table_id)){
            LinkedList<ClientSide>clientSides = RoomsContains.TABLE_MAP.get(table_id).getClientSideList();
            for(ClientSide client : clientSides){
                int dataId = client.getId();
                Object[] dataIddata = RoomTableHelper.getGameOverInfo(dataId,table_id);
                server.sendMessage(dataId,dataIddata);
            }
        }
    }
}
