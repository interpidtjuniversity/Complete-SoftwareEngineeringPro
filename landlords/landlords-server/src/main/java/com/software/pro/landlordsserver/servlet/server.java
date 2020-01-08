package com.software.pro.landlordsserver.servlet;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.HtmlEvent;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.ClientRole;
import com.software.pro.landlordsserver.enums.RoomEventCode;
import com.software.pro.landlordsserver.enums.TableEventCode;
import com.software.pro.landlordsserver.enums.TableType;
import com.software.pro.landlordsserver.events.RoomEvent.RoomEventListener;
import com.software.pro.landlordsserver.events.TableEvent.TableEventListener;
import com.software.pro.landlordsserver.utils.ServerIntegerArrayDecoder;
import com.software.pro.landlordsserver.utils.ServerIntegerArrayEncoder;
import com.software.pro.landlordsserver.utils.ServerObjectArrayDecoder;
import com.software.pro.landlordsserver.utils.ServletMapEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

//只是一个websocket服务的监听入口, 在外部可以使用该类的变量
@Controller
@Component
@ServerEndpoint(value = "/landlords/{user_Name}",encoders = {ServletMapEncoder.class, ServerIntegerArrayEncoder.class},
        decoders = {ServerIntegerArrayDecoder.class, ServerObjectArrayDecoder.class})                             //带一个用户名, 然后创建一个用户id,
@Slf4j
@RequestMapping("/landlords")
public class server {

    public String user_Name;
    public Session session;
    public int client_id;

    //先群发新玩家加入的消息,然后再群发当前房间人数以更新房间
    @OnOpen
    public void onOpen(Session session, @PathParam("user_Name") String user_Name){                          //客户端连接
        this.session = session;
        this.user_Name = user_Name;
        this.client_id = ServerContains.getClientId();                                                      //服务器分配id
        ClientSide newclientSide = new ClientSide(client_id,session,user_Name, ClientRole.PLAYER);
        //ServerContains
        ServerContains.SESSION_ID_MAP.put(client_id,session);         //加入session
        ServerContains.CLIENTSIDE_MAP.put(client_id,newclientSide);         //加入客户端
        ServerContains.CLIENT_ID_NAME.put(client_id,user_Name);
        //玩家还没有进入房间, 所以暂时不需要放入table
        //给玩家发送id
        Integer[] dataid = new Integer[2];
        dataid[0] = 0;
        dataid[1] = client_id;
        sendMessage(session,dataid);
    }

    @OnClose
    public void onClose(Session session) {
        //从大厅移除
        int client_id = -1;
        for(Integer id : ServerContains.SESSION_ID_MAP.keySet()){
            if(ServerContains.SESSION_ID_MAP.get(id).equals(session))
                client_id = id;break;
        }
        //
        ServerContains.CLIENTSIDE_MAP.remove(client_id);
        ServerContains.SESSION_ID_MAP.remove(client_id);

        log.info("玩家: "+ServerContains.CLIENT_ID_NAME.get(client_id) + "退出游戏");
    }
    @OnMessage
    public void onMessage(Object[] message){
        //加入房间
        if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_JOIN_ROOM) && Integer.valueOf(message[1].toString()).equals(1)){          //人机对战房间
            RoomEventListener.get(RoomEventCode.CODE_PVE_ROOM_JOIN).call(client_id);
        }
        else if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_JOIN_ROOM) &&
                Integer.valueOf(message[1].toString()).equals(2)){     //玩家对战房间
            RoomEventListener.get(RoomEventCode.CODE_ROOM_JOIN).call(client_id);
        }
        //
        else if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_NEW_TABLE)){
            RoomEventListener.get(RoomEventCode.CODE_TABLE_CREATE).call(client_id);       //client_id创建了一个桌子
        }
        //加入桌子
        else if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_JOIN_TABLE)&&
                Integer.valueOf(message[1].toString())>=0){                             //client_id 加入join_table_id
            RoomEventListener.get(RoomEventCode.CODE_TABLE_JOIN).call(client_id,Integer.valueOf(message[1].toString()));
        }
        else if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_JOIN_TABLE)&&
                Integer.valueOf(message[1].toString())<0){
            RoomEventListener.get(RoomEventCode.CODE_PVE_TABLE_JOIN).call(client_id);
        }
        //叫分
        else if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_CALL_SCORE)){
            if(RoomsContains.TABLE_MAP.get(ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId()).getType().equals(TableType.PVR)){
                TableEventListener.get(TableEventCode.CODE_PVE_GAME_LANDLORD_ELECT).call(client_id,Integer.valueOf(message[1].toString()));
            }
            else
                TableEventListener.get(TableEventCode.CODE_GAME_LANDLORD_ELECT).call(client_id,Integer.valueOf(message[1].toString()));
        }
        else if(Integer.valueOf(message[0].toString()).equals(HtmlEvent.REQ_SHOT_POKER)){
            if(RoomsContains.TABLE_MAP.get(ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId()).getType().equals(TableType.PVR)) {
                TableEventListener.get(TableEventCode.CODE_PVE_GAME_PLAY_POKERS).call(client_id, message[1], 0);
            }
            else
                TableEventListener.get(TableEventCode.CODE_GAME_PLAY_POKERS).call(client_id,message[1],0);
        }
    }


    public static void sendMessage(Session session,Object data){
        try {
            session.getBasicRemote().sendObject(data);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendMessage(int client_id,Object data){
        try{
            ServerContains.SESSION_ID_MAP.get(client_id).getBasicRemote().sendObject(data);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void sendOthers(Integer table_id, Integer client_id,Object message) throws IOException{
        for (Integer integer : RoomsContains.Tables_Sessions.get(table_id).keySet()) {
            if(!integer.equals(client_id)) {
                try {
                    sendMessage(RoomsContains.Tables_Sessions.get(table_id).get(integer),message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendAll(Integer table_id,Object message) throws IOException{
        for (Integer integer : RoomsContains.Tables_Sessions.get(table_id).keySet()) {
            try {
                sendMessage(RoomsContains.Tables_Sessions.get(table_id).get(integer),message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
