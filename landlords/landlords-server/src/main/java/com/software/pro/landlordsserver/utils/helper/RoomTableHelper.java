package com.software.pro.landlordsserver.utils.helper;

import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoomTableHelper {
    public static Object[] joinTable(int table_id){
        Object[] dataplayers = new Object[3];
        dataplayers[0] = 20;
        dataplayers[1] = table_id;
        Object[]players = new Object[3];
        int flag = 0;
        for(Integer integer : RoomsContains.TABLE_MAP.get(table_id).getClientSideMap().keySet()){
            Object[]player = new Object[2];
            player[0] = integer;
            player[1] = ServerContains.CLIENT_ID_NAME.get(integer);
            players[flag] = player;
            flag++;
        }
        while(flag<3){
            Object[]player = new Object[2];
            player[0] = -1;
            player[1] = "";
            players[flag] = player;
            flag++;
        }
        dataplayers[2]=players;
        return dataplayers;
    }

    public static Object[] createTable(int table_id){
        Integer[] datatable = new Integer[2];
        datatable[0] = 22;
        datatable[1] = table_id;

        return datatable;
    }

    public static Object[] showAllTables(){
        Object[]data = new Object[2];
        data[0] = 18;
        Object[]tables = new Object[RoomsContains.TABLE_MAP.size()];
        int flag = 0;
        for(Integer integer : RoomsContains.TABLE_MAP.keySet()){
            Integer[] table = new Integer[2];
            table[0] = integer;
            table[1] = RoomsContains.TABLE_MAP.get(integer).getPlayerNums();
            tables[flag] = table;
            flag++;
        }
        data[1]=tables;
        return data;
    }

    public static Object[] getPokers(int client_id, List<Integer>list){      //client_id是第一个决定是否要地主的client
        PokerHelper.sortPoker(list);
        Object[] datapokers = new Object[3];
        datapokers[0] = 32;
        datapokers[1] = client_id;
        Object[]pokers = new Object[list.size()];
        for(int i=0;i<list.size();i++){
            pokers[i] = list.get(i);
        }
        datapokers[2] = pokers;
        return datapokers;
    }


    public static Object[] getCallScoreData(int table_id ,int client_id,int score){
        Calculate_Landlord_ID(table_id);
        //
        Object[] data = new Object[4];
        data[0] = 34;
        data[1] = client_id;
        data[2] = score;
        Table table = RoomsContains.TABLE_MAP.get(table_id);
        if(score == 3)
            data[3] = true;
        else {
            if (table.getLandlordId() == -1)
                data[3] = false;
            else
                data[3] = true;
        }
        return data;
    }

    public static Object[] getLandlordPokers(int table_id){
        Object[] data = new Object[3];
        data[0] = 36;
        data[1] = RoomsContains.TABLE_MAP.get(table_id).getLandlordId();
        data[2] = RoomsContains.Tables_Landlord_Pokers.get(table_id);

        //在这里将底牌发送给flask
        return data;
    }

    private static void Calculate_Landlord_ID(int table_id){
        if(RoomsContains.Tables_Call_Scores.get(table_id).size()<3){
            return;
        }
        else if(RoomsContains.Tables_Call_Scores.get(table_id).size()==3){
            int maxscore = -1;
            int maxscoreid = -1;
            int landlordid = -1;
            for(Integer integer : RoomsContains.Tables_Call_Scores.get(table_id).keySet()){
                if(RoomsContains.Tables_Call_Scores.get(table_id).get(integer) > maxscore){
                    maxscore = RoomsContains.Tables_Call_Scores.get(table_id).get(integer);
                    maxscoreid = integer;
                }
            }
            if(maxscore == 0) {
                int landlordIndex = (int)(Math.random() * 3);
                ClientSide clientSide = RoomsContains.TABLE_MAP.get(table_id).getClientSideList().get(landlordIndex);
                landlordid = clientSide.getId();

                RoomsContains.Tables_Call_Scores.get(table_id).put(landlordid,1);
            }
            else{
                landlordid = maxscoreid;
            }
            Table table = RoomsContains.TABLE_MAP.get(table_id);
            table.setLandlordId(landlordid);
            RoomsContains.TABLE_MAP.put(table_id,table);
        }

    }

    public static Object[] getPokersPlayInfo(int client_id, Object pokers){
        Object[]data = new Object[3];
        data[0] = 38;
        data[1] = client_id;
        data[2] = pokers;
        return data;
    }

    public static void Clear_Client_Pokers(int client_id,List<Integer>list){
        if(list.size()>0){
            for(Integer integer : list){
                RoomsContains.Tables_Clients_Pokers.get(client_id).remove(integer);
            }
        }
        if(RoomsContains.Tables_Clients_Pokers.get(client_id).size() == 0) {
            int table_id = ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId();
            Table table = RoomsContains.TABLE_MAP.get(table_id);
            table.setWinner(client_id);
        }
    }

    public static boolean IsGameOver(int table_id){
        Table table = RoomsContains.TABLE_MAP.get(table_id);
        for(ClientSide clientSide : table.getClientSideList()){
            int client_id = clientSide.getId();
            int pokers_nums = RoomsContains.Tables_Clients_Pokers.get(client_id).size();
            if(pokers_nums == 0)
                return true;
        }
        return false;
    }

    public static Object[] getGameOverInfo(int client_id,int table_id){
        Object[]data = new Object[5];
        Table table = RoomsContains.TABLE_MAP.get(table_id);
        int winner_id = table.getWinner();
        data[0]=42;
        data[1]=winner_id;
        //data[2]=Integer.MAX_VALUE;        //最大分
        int i = 3;
        for(ClientSide clientSide : table.getClientSideList()){     //得到另外两个玩家的牌
            int player_id = clientSide.getId();
            if(player_id != client_id){
                List<Integer>list = RoomsContains.Tables_Clients_Pokers.get(player_id);
                List<Integer>dataplayer = new ArrayList<>();
                dataplayer.add(player_id);          //其它玩家id
                if(list.size() != 0){
                    dataplayer.addAll(list);
                }
                data[i] = dataplayer;
                i++;
            }
        }
        int maxscore = -1;
        for(Integer integer:RoomsContains.Tables_Call_Scores.get(table_id).values()){
            if(integer>maxscore)
                maxscore = integer;
            data[2]=maxscore;
        }
        return data;
    }
}
