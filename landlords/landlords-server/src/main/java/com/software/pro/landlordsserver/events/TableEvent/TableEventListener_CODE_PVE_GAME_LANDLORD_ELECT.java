package com.software.pro.landlordsserver.events.TableEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.ClientRole;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RobotHelper;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

public class TableEventListener_CODE_PVE_GAME_LANDLORD_ELECT implements TableEventListener{
    public void call(int table_id){}
    public void call(int client_id,int op){
        int table_id = ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId();
        if(op!=0){
            //玩家选择了叫地主
            Object[]data = new Object[4];
            data[0] =  34;
            data[1] = client_id;
            data[2] = op;
            data[3] = true;
            server.sendMessage(client_id,data);

            Table table = RoomsContains.TABLE_MAP.get(table_id);
            table.setLandlordId(client_id);
            RoomsContains.TABLE_MAP.put(table_id,table);
            Object[]datalandlordpokers = RoomTableHelper.getLandlordPokers(table_id);
            try {
                server.sendMessage(client_id,datalandlordpokers);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            RoomsContains.ROBOT_Tables_Client_Pokers.put(client_id,20);
        }
        else{
            //玩家不叫,在两个机器人中选一个做为地主
            int landlord_id = -1;
            Table table = RoomsContains.TABLE_MAP.get(table_id);
            for(ClientSide clientSide : table.getClientSideList()){
                if(clientSide.getRole().equals(ClientRole.ROBOT)){                                    //robot2 永不为地主
                    landlord_id = clientSide.getId();
                    table.setLandlordId(landlord_id);
                    RoomsContains.TABLE_MAP.put(table_id,table);
                    break;
                }
            }
            //将地主牌添加给这个电脑,然后调用出牌函数
            Object[]datalandlordpokers = RoomTableHelper.getLandlordPokers(table_id);
            try {
                server.sendMessage(client_id,datalandlordpokers);
                Thread.sleep(500);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            //
            RobotHelper.setRobotPokersMap(landlord_id,RoomsContains.Tables_Landlord_Pokers.get(table_id));
            for(ClientSide clientSide: table.getClientSideList()){
                if(clientSide.getRole().equals(ClientRole.ROBOT)){
                    try{
                        Thread.sleep(1500);
                    }
                    catch (Exception e){e.printStackTrace();}

                    if(clientSide.getLevel()==19991116) {     //调用flask
                        Integer[] robot_shot = RobotHelper.deepQShotPokers(table_id, clientSide.getId());
                        Object[] data = RoomTableHelper.getPokersPlayInfo(clientSide.getId(), robot_shot);
                        server.sendMessage(client_id, data);
                    }
                    else {
                        Integer[] robot_shot = RobotHelper.shotPokers(table_id, clientSide.getId());
                        Object[] data = RoomTableHelper.getPokersPlayInfo(clientSide.getId(), robot_shot);
                        server.sendMessage(client_id, data);
                    }

                    try{
                        Thread.sleep(1500);
                    }
                    catch (Exception e){e.printStackTrace();}
                }
            }
        }
    }
    public void call(int client_id,Object message,int flag){}
}
