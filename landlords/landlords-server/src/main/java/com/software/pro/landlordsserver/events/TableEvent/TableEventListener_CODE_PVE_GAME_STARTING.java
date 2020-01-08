package com.software.pro.landlordsserver.events.TableEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.ClientRole;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.PokerHelper;
import com.software.pro.landlordsserver.utils.helper.RobotHelper;
import com.software.pro.landlordsserver.utils.helper.RoomTableHelper;

import java.util.LinkedList;
import java.util.List;

public class TableEventListener_CODE_PVE_GAME_STARTING implements TableEventListener{
    public void call(int table_id){
        //这个人机桌子开始游戏
        Table table = RoomsContains.TABLE_MAP.get(table_id);
        List<List<Integer>>pokers = PokerHelper.distributePoker();
        //设置底牌
        RoomsContains.Tables_Landlord_Pokers.put(table_id,pokers.get(pokers.size()-1));
        //先设置Robot的牌 询问玩家是否选择地主
        LinkedList<ClientSide> clientSides = table.getClientSideList();
        for(int i=0;i<clientSides.size();i++){
            if(clientSides.get(i).getRole().equals(ClientRole.ROBOT)){
                RobotHelper.setRobotPokersMap(clientSides.get(i).getId(),pokers.get(i));
                //额外设置flask服务器
                if(clientSides.get(i).getLevel()==19991116)
                    RobotHelper.setDeepQRobotPokersMap(clientSides.get(i).getId(),pokers.get(i));
            }
            else if(clientSides.get(i).getRole().equals(ClientRole.PLAYER)){
                int client_id = clientSides.get(i).getId();
                Object[]data = RoomTableHelper.getPokers(client_id,pokers.get(i));
                server.sendMessage(client_id,data);

                //记录玩家的手牌数
                RoomsContains.ROBOT_Tables_Client_Pokers.put(client_id,pokers.get(i).size());
            }
        }
    }
    public void call(int client_id,int op){}
    public void call(int client_id,Object message,int flag){}
}
