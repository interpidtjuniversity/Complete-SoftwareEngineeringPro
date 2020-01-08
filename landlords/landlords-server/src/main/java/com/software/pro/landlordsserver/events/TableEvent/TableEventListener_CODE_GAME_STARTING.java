package com.software.pro.landlordsserver.events.TableEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.PokerHelper;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import java.util.LinkedList;
import java.util.List;

public class TableEventListener_CODE_GAME_STARTING implements TableEventListener{
    public void call(int table_id){
        //这个桌子开始游戏
        //在这里设置系统变量 RoomContains
        Table table = RoomsContains.TABLE_MAP.get(table_id);
        //得到牌
        List<List<Integer>>pokers = PokerHelper.distributePoker();
        //设置底牌
        RoomsContains.Tables_Landlord_Pokers.put(table_id,pokers.get(pokers.size()-1));
        table.setLandlordPokers(pokers.get(pokers.size()-1));
        LinkedList<ClientSide> clientSides = table.getClientSideList();

        int electIndex = (int)(Math.random() * 3);
        int electid = table.getClientSideList().get(electIndex).getId();
        for(int i = 0; i< clientSides.size(); i++){
            ClientSide clientSide = clientSides.get(i);
            Object[]datapokers = RoomTableHelper.getPokers(electid,pokers.get(i));
            server.sendMessage(clientSide.getSession(),datapokers);

            //设置服务器记录
            RoomsContains.Tables_Clients_Pokers.put(clientSide.getId(),pokers.get(i));
        }
    }
    public void call(int client_id, int op){}
    public void call(int client_id,Object message,int flag){}
}
