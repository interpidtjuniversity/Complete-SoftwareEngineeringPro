package com.software.pro.landlordsserver.events.TableEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import java.util.HashMap;
import java.util.Map;

public class TableEventListener_CODE_GAME_LANDLORD_ELECT implements TableEventListener{
    public void call(int client_id,int op){
        ClientSide clientSide = ServerContains.CLIENTSIDE_MAP.get(client_id);
        Table table = RoomsContains.TABLE_MAP.get(clientSide.getTableId());
        int table_id = table.getId();
        //table_id 的 client_id 选择叫地主 score分
        Map<Integer,Integer>integerIntegerMap = RoomsContains.Tables_Call_Scores.get(table_id);

        if(integerIntegerMap==null){
            Map<Integer,Integer>map = new HashMap<>();
            map.put(client_id,op);
            RoomsContains.Tables_Call_Scores.put(table_id,map);
        }
        else{
            Map<Integer,Integer>map = RoomsContains.Tables_Call_Scores.get(table_id);
            map.put(client_id,op);
            RoomsContains.Tables_Call_Scores.put(table_id, map);
        }

        //返回数据
        Object[] datacallscore = RoomTableHelper.getCallScoreData(table_id,client_id,op);
        try{
            server.sendAll(table_id,datacallscore);
        }
        catch (Exception e){e.printStackTrace();}
        //检测
        if(RoomsContains.Tables_Call_Scores.get(table_id).size() == 3||op==3){
            if(op == 3) {
                table.setLandlordId(client_id);
                RoomsContains.TABLE_MAP.put(table_id, table);
            }
            Object[] datalandlordpokers = RoomTableHelper.getLandlordPokers(table_id);
            try{
                server.sendAll(table_id,datalandlordpokers);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            //地主已经产生,将地主牌加入记录
            RoomsContains.Tables_Clients_Pokers.get(table.getLandlordId()).addAll(RoomsContains.Tables_Landlord_Pokers.get(table_id));
        }
    }
    public void call(int table_id){}
    public void call(int client_id,Object message,int flag){
    }
}
