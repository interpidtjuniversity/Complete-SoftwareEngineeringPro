package com.software.pro.landlordsserver.data;

import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

//存放房间服务器发给全局服务器的信息
public class RoomsContains {
    //PVP DATA
    public final static Map<Integer, Table> TABLE_MAP = new ConcurrentSkipListMap<>();
    public final static Map<Integer, Map<Integer, Session>> Tables_Sessions = new HashMap<>();      //所有房间的连接信息 id session
    public final static Map<Integer,Integer>Tables_OnLineCount = new HashMap<>();                   //玩家加入后的人数
    public final static Map<Integer, List<Integer>>Tables_Landlord_Pokers = new HashMap<>();        //底牌
    public final static Map<Integer,Map<Integer,Integer>>Tables_Clients_Index=new HashMap<>();      //客户端连接在房间中的序号    id index
    public final static Map<Integer, Map<Integer,Integer>>Tables_Call_Scores = new HashMap<>();     //桌子的叫分情况, true还是false根据table.landlord的值而定 -1 为false
    public final static Map<Integer, List<Integer>>Tables_Clients_Pokers = new HashMap<>();         //玩家的手牌
    //PVE DATA
    public final static Map<Integer,Integer>Robots_Tables_Nums_Of_NotOut = new HashMap<>();               //出牌归零
    public final static Map<Integer,List<Integer>>ROBOT_Tables_Last_Sell_Type = new HashMap<>();             //两次不出清空
    public final static Map<Integer, MultiValueMap<Integer,Integer>>Robots_Pokers = new HashMap<>();      //机器人不出保持不变
    public final static Map<Integer, Integer>ROBOT_Tables_Client_Pokers = new HashMap<>();
                                                     // 1----13  0----51      3 4 5 6 7 8 9 10 J Q K A 2(1,14,27,40)
                                                     // 14         53
                                                     // 15         52
    //flask DATA
    public final static Map<Integer,String>ROBOT_Tables_Flask_Last_Sell_Type = new HashMap<>();       //flask服务器需要的类型    dan, dui ,san, sandaiyi, sandaier, bomb ,shunzi

    public final static void removeTableMessage(int id){
    }

    public final static void addTable(Table table){
        //房间里所有的交互信息
    }
}
