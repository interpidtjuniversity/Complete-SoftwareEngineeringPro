package com.software.pro.landlordsserver.events.RoomEvent;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.ClientRole;
import com.software.pro.landlordsserver.enums.TableEventCode;
import com.software.pro.landlordsserver.enums.TableType;
import com.software.pro.landlordsserver.events.TableEvent.TableEventListener;
import com.software.pro.landlordsserver.servlet.server;
import com.software.pro.landlordsserver.utils.helper.RobotHelper;

import java.util.*;

public class RoomEventListener_CODE_PVE_TABLE_JOIN implements RoomEventListener{
    public void call(int client_id){
        //创建pve桌子 ,依次加入机器人
        int newpvetableid = ServerContains.getTableId();
        Table newpvetable = new Table(newpvetableid);
        newpvetable.setType(TableType.PVR);

        ClientSide robot1 = RobotHelper.getRobot();
        ClientSide robot2 = RobotHelper.getRobot();
        robot2.setLevel(19991116);                                                       //这个robot2不使用服务器的出牌函数而使用flask服务器的DeepQAI
        ClientSide player = ServerContains.CLIENTSIDE_MAP.get(client_id);
        player.setTableId(newpvetableid);
        robot1.setTableId(newpvetableid);
        robot2.setTableId(newpvetableid);
        ServerContains.CLIENTSIDE_MAP.put(client_id,player);

        LinkedList<ClientSide>clientSideLinkedList = new LinkedList<>();
        Map<Integer,ClientSide>clientSideMap = new HashMap<>();
        clientSideLinkedList.add(player);
        clientSideMap.put(robot1.getId(),robot1);
        clientSideLinkedList.add(robot1);
        clientSideMap.put(robot2.getId(),robot2);
        clientSideLinkedList.add(robot2);
        clientSideMap.put(player.getId(),player);
        newpvetable.setClientSideList(clientSideLinkedList);
        newpvetable.setClientSideMap(clientSideMap);

        RoomsContains.TABLE_MAP.put(newpvetableid,newpvetable);
        RoomsContains.Tables_OnLineCount.put(newpvetableid,3);

        //发送数据
        Object[] Data = RobotHelper.getJoinPVETableData(newpvetableid,client_id);
        server.sendMessage(client_id,Data[0]);
        try {
            Thread.sleep(100);
        }
        catch (Exception e){e.printStackTrace();}
        server.sendMessage(client_id,Data[1]);
        try {
            Thread.sleep(100);
        }
        catch (Exception e){e.printStackTrace();}
        server.sendMessage(client_id,Data[2]);
        //开始游戏
        //设置不出为0
        RoomsContains.Robots_Tables_Nums_Of_NotOut.put(newpvetableid,0);
        //设置lastsell为空数组
        List<Integer>list = new LinkedList<>();
        RoomsContains.ROBOT_Tables_Last_Sell_Type.put(newpvetableid,list);
        TableEventListener.get(TableEventCode.CODE_PVE_GAME_STARTING).call(newpvetableid);
    }
    public void call(int table_id,int client_id){}
}
