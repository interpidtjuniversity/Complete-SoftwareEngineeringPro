package com.software.pro.landlordsserver.events.TableEvent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.ClientRole;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RobotHelper;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TableEventListener_CODE_PVE_GAME_PLAY_POKERS implements TableEventListener{
    public void call(int table_id){}
    public void call(int client_id,int op){}
    public void call(int client_id,Object message,int flag){
        //发送自己出的牌
        Object[]myselfpokerinfo = RoomTableHelper.getPokersPlayInfo(client_id,message);
        server.sendMessage(client_id,myselfpokerinfo);
        Table table = RoomsContains.TABLE_MAP.get(ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId());
        int table_id = table.getId();
        Integer[] playersellpokers = JSONArray.parseObject(message.toString(),Integer[].class);
        //更新玩家牌的数量
        int clietpokersnum = RoomsContains.ROBOT_Tables_Client_Pokers.get(client_id);
        clietpokersnum -= playersellpokers.length;
        RoomsContains.ROBOT_Tables_Client_Pokers.put(client_id,clietpokersnum);


        ArrayList<Integer> listlastsellpokers= new ArrayList<Integer>(Arrays.asList(playersellpokers));
        for(int i=0;i<listlastsellpokers.size();i++){
            if (listlastsellpokers.get(i) < 52) {        //0----51 = 13 * 4
                listlastsellpokers.set(i,listlastsellpokers.get(i)%13+1);
            } else if (listlastsellpokers.get(i) == 52) {
                //大王
                listlastsellpokers.set(i,15);
            } else if (listlastsellpokers.get(i) == 53) {
                //小王
                listlastsellpokers.set(i,14);
            }
        }

        if(playersellpokers.length==0){
            int nums_of_notout = RoomsContains.Robots_Tables_Nums_Of_NotOut.get(table_id);
            nums_of_notout++;
            if(nums_of_notout == 2) {
                RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id,0);//电脑必须出
                //数组清空 //电脑可以自由出牌
                List<Integer>list = new LinkedList<>();
                RoomsContains.ROBOT_Tables_Last_Sell_Type.put(table_id,list);
                //设置flask
                RobotHelper.setLestSellType(table_id,list);
                //
            }
            else
                RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id,nums_of_notout);
        }
        else{
            RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id,0);   //归零
            RoomsContains.ROBOT_Tables_Last_Sell_Type.put(table_id,listlastsellpokers);   //刷新
            RobotHelper.setLestSellType(table_id,listlastsellpokers);       //刷新
        }

        if(RobotHelper.IsGameOver(client_id)){
            Object[] data = RobotHelper.getGameOverInfo(client_id);
            server.sendMessage(client_id,data);
        }
        else {
            //在这里调用两个电脑的出牌函数
            for (ClientSide clientSide : table.getClientSideList()) {
                if (clientSide.getRole().equals(ClientRole.ROBOT)) {
                    //两个电脑连续出牌
                    //出牌前sleep 1.5
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    //出牌后sleep 1.5
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (RobotHelper.IsGameOver(client_id)) {
                        Object[] data = RobotHelper.getGameOverInfo(client_id);
                        server.sendMessage(client_id, data);
                        break;
                    }
                }

            }
        }
    }
}
