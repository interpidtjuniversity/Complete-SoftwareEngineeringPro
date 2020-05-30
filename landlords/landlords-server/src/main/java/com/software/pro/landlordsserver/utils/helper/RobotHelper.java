package com.software.pro.landlordsserver.utils.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.software.pro.landlordsserver.data.RoomsContains;
import com.software.pro.landlordsserver.data.ServerContains;
import com.software.pro.landlordsserver.entity.ClientSide;
import com.software.pro.landlordsserver.entity.Table;
import com.software.pro.landlordsserver.enums.ClientRole;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class RobotHelper {
    public final static RestTemplate restTemplate = new RestTemplate();

    public static Object[] showAllTables() {
        Object[] dataTables = new Object[2];
        dataTables[0] = 18;
        Integer[] empty = {};
        dataTables[1] = empty;

        return dataTables;
    }

    public static ClientSide getRobot() {
        int robotId = ServerContains.getClientId() + 1000;
        ClientRole role = ClientRole.ROBOT;
        String robotName = "ROBOT-" + RandomStringUtils.randomAlphabetic(10);

        ClientSide clientSide = new ClientSide(robotId, robotName, role);
        return clientSide;
    }

    public static Object[] getJoinPVETableData(int table_id, int client_id) {
        LinkedList<ClientSide> linkedList = RoomsContains.TABLE_MAP.get(table_id).getClientSideList();
        Object[] Data = new Object[3];
        for (int i = 1; i <= 3; i++) {
            Object[] data = new Object[3];
            data[0] = 20;
            data[1] = table_id;
            Object[] dataPlayers = new Object[3];
            int j = 0;
            for (j = 0; j < i; j++) {
                Object[] dataplayer = new Object[2];
                dataplayer[0] = linkedList.get(j).getId();
                dataplayer[1] = linkedList.get(j).getOwner_name();
                dataPlayers[j] = dataplayer;
            }
            for (int k = j; k < 3 - i + j; k++) {
                Object[] dataplayer = new Object[2];
                dataplayer[0] = -1;
                dataplayer[1] = "";
                dataPlayers[k] = dataplayer;
            }
            data[2] = dataPlayers;
            Data[i-1] = data;
        }
        return Data;
    }

    public static void setRobotPokersMap(int robot_id, List<Integer> pokers) {
        //先排好序再插入map
        Collections.sort(pokers, PokerHelper.pokerComparator);
        MultiValueMap<Integer, Integer> pokersMap = RoomsContains.Robots_Pokers.get(robot_id);
        if (pokersMap == null)
            pokersMap = new LinkedMultiValueMap<>();
        for (Integer integer : pokers) {
            if (integer < 52) {        //0----51 = 13 * 4
                int pokerValue = integer % 13 + 1;
                pokersMap.add(pokerValue, integer);
            } else if (integer == 52) {
                //大王
                pokersMap.add(15, 52);
            } else if (integer == 53) {
                //小王
                pokersMap.add(14, 53);
            }
        }
        RoomsContains.Robots_Pokers.put(robot_id, pokersMap);
    }

    public static void setDeepQRobotPokersMap(int robot_id, List<Integer> pokers) {
        String[]flaskPoker = new String[pokers.size()];
        for(int i=0; i< pokers.size();i++)
            if(pokers.get(i) < 52)
                flaskPoker[i]= String.valueOf(pokers.get(i)%13+1);
            else if(pokers.get(i)==52)
                flaskPoker[i]="15";
            else if(pokers.get(i)==53)
                flaskPoker[i]="14";
        //初始化flask服务器
        JSONObject cards = new JSONObject();
        cards.put("cards" , flaskPoker);
        restTemplate.postForObject("http://localhost:5000/init_match",cards,String.class);
    }

    public static void upDate(int table_id, int robot_id, List<Integer> lastsell_type, MultiValueMap<Integer, Integer> robot_pokers) {
        for(int i=0;i<lastsell_type.size();i++){
            if (lastsell_type.get(i) < 52) {        //0----51 = 13 * 4
                lastsell_type.set(i,lastsell_type.get(i)%13+1);
            } else if (lastsell_type.get(i) == 52) {
                //大王
                lastsell_type.set(i,15);
            } else if (lastsell_type.get(i) == 53) {
                //小王
                lastsell_type.set(i,14);
            }
        }

        RoomsContains.ROBOT_Tables_Last_Sell_Type.put(table_id, lastsell_type);
        RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id, 0);
        RoomsContains.Robots_Pokers.put(table_id, robot_pokers);
        setLestSellType(table_id,lastsell_type);
        return;
    }

    public static Integer[] shotPokers(int table_id, int robot_id) {
        List<Integer> lastsell = RoomsContains.ROBOT_Tables_Last_Sell_Type.get(table_id);
        Integer[] nextshot = {};
        MultiValueMap<Integer, Integer> robotpokers = RoomsContains.Robots_Pokers.get(robot_id);
        List<Integer> shotpokerrs = new LinkedList<>();
        if (lastsell.size() == 0) {

            //自由出牌
            for (Integer integer : robotpokers.keySet()) {
                if (robotpokers.get(integer).size() == 1 || robotpokers.get(integer).size() == 2) {    //出小对子
                    System.out.println("自由的1 2");
                    //有单牌先出单
                    shotpokerrs.addAll(robotpokers.get(integer));
                    robotpokers.remove(integer);
                    nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                    upDate(table_id, robot_id, shotpokerrs, robotpokers);
                    return nextshot;
                } else if (robotpokers.get(integer).size() == 3) {         //三带大对子
                    //判断有无三代二或者三代一
                    for (Integer probable : robotpokers.keySet()) {
                        if (robotpokers.get(probable).size() == 1 || robotpokers.get(probable).size() == 2) {
                            System.out.println("自由的 三代一二");
                            shotpokerrs.addAll(robotpokers.get(integer));
                            shotpokerrs.addAll(robotpokers.get(probable));
                            robotpokers.remove(integer);
                            robotpokers.remove(probable);
                            nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                            upDate(table_id, robot_id, shotpokerrs, robotpokers);
                            return nextshot;
                        } else {
                            //没有三带一和三代二就先出一张
                            System.out.println("自由的 没有三代一二 出一张");
                            List<Integer>list = robotpokers.get(integer);
                            Integer one = list.get(0);
                            list.remove(one);
                            if(list.size()==0)
                                robotpokers.remove(integer);
                            else
                                robotpokers.replace(integer,list);

                            shotpokerrs.add(one);
                            nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                            upDate(table_id, robot_id, shotpokerrs, robotpokers);
                            return nextshot;
                        }
                    }
                } else {
                    //居然只剩炸弹?
                    System.out.println("自由的炸弹");
                    shotpokerrs.addAll(robotpokers.get(integer));
                    robotpokers.remove(integer);
                    nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                    upDate(table_id, robot_id, shotpokerrs, robotpokers);
                    return nextshot;
                }
            }
        } else {
            //打印手牌
            //可能的类型是 单,对子,三带一,三带二                                    //连对, 王炸, 顺子
            Integer[] better_lastsell = new Integer[16];
            for (int i = 0; i < 16; i++) {
                better_lastsell[i] = 0;
            }
            for (int i = 0; i < lastsell.size(); i++) {
                System.out.println(lastsell.get(i));
                better_lastsell[lastsell.get(i)]++;        // 牌i 有 better_lastsell[i]张
            }
            //得到了牌的类型
            int single_nums = 0;
            int single_value = -1;
            int double_nums = 0;
            int double_value = -1;
            int three_nums = 0;
            int three_value = -1;
            for (int i = 0; i < 16; i++) {
                if (better_lastsell[i] == 1) {
                    single_nums++;
                    single_value = i;
                } else if (better_lastsell[i] == 2) {
                    double_nums++;
                    double_value = i;
                } else if (better_lastsell[i] == 3) {
                    three_nums++;
                    three_value = i;
                }
            }
            //单牌
            if (single_nums == 1 && double_nums == 0 & three_nums == 0) {
                for (Integer integer : robotpokers.keySet()) {
                    if (PokerHelper.pokerComparator.compare(integer, single_value) > 0 &&
                            (robotpokers.get(integer).size() == 1 || robotpokers.get(integer).size() == 3)) {

                        System.out.println("跟单");
                        List<Integer>list = robotpokers.get(integer);
                        Integer one = list.get(0);
                        list.remove(one);
                        if(list.size()==0)
                            robotpokers.remove(integer);
                        else
                            robotpokers.replace(integer,list);

                        shotpokerrs.add(one);
                        robotpokers.remove(integer, one);
                        nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                        upDate(table_id, robot_id, shotpokerrs, robotpokers);
                        return nextshot;
                    }

                }
            }
            //对子
            else if (single_nums == 0 && double_nums == 1 && three_nums == 0) {
                for (Integer integer : robotpokers.keySet()) {
                    if (PokerHelper.pokerComparator.compare(integer, double_value) > 0
                            && (robotpokers.get(integer).size() == 2)) {
                        System.out.println("跟双");
                        shotpokerrs.addAll(robotpokers.get(integer));
                        robotpokers.remove(integer);
                        nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                        upDate(table_id, robot_id, shotpokerrs, robotpokers);
                        return nextshot;
                    }
                }
            }
            //三带二
            else if (single_nums == 0 && double_nums == 1 && three_nums == 1) {
                for (Integer integer : robotpokers.keySet()) {
                    if (PokerHelper.pokerComparator.compare(integer, three_value) > 0 && (robotpokers.get(integer).size() == 3)) {
                        for (Integer probable : robotpokers.keySet()) {
                            if (robotpokers.get(probable).size() == 2) {
                                System.out.println("跟三代二");
                                shotpokerrs.addAll(robotpokers.get(integer));
                                shotpokerrs.addAll(robotpokers.get(probable));
                                robotpokers.remove(integer);
                                robotpokers.remove(probable);
                                nextshot = shotpokerrs.toArray(new Integer[shotpokerrs.size()]);
                                upDate(table_id, robot_id, shotpokerrs, robotpokers);
                                return nextshot;
                            }
                        }
                    }
                }
            }
            //不出
            int nums = RoomsContains.Robots_Tables_Nums_Of_NotOut.get(table_id);
            nums++;
            if(nums == 2) {
                RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id,0);//电脑必须出
                //数组清空 //电脑可以自由出牌
                List<Integer>list = new LinkedList<>();
                RoomsContains.ROBOT_Tables_Last_Sell_Type.put(table_id,list);
                //设置flask
                RobotHelper.setLestSellType(table_id,list);
            }
            else
                RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id, nums);
            return nextshot;
        }
        return nextshot;
    }

                                            //重复数   连续数
    public static List<Integer> judgeJunko(int kind, int size, MultiValueMap<Integer, Integer> map) {
        List<Integer> res = new LinkedList<>();
        for (Integer integer : map.keySet()) {
            if ((integer + 11) % 13 > 0 && (integer + 11) % 13 < 11 && integer < 16 - size) {
                int i = 0;
                for (i = 0; i < size; i++) {
                    if (map.get(integer + i) == null || map.get(integer + i).size() < kind)
                        break;
                }
                if (i == size)
                    res.add(integer);
            }
        }
        return res;
    }

    public static List<Integer> removePoker(int robot_id, int poker, int nums) {
        //值为poker的牌移除nums个
        System.out.println("移除"+poker+nums+"张");
        MultiValueMap<Integer,Integer>robot_pokers = RoomsContains.Robots_Pokers.get(robot_id);
        List<Integer>valuelist = robot_pokers.get(poker);
        List<Integer>reslist = new LinkedList<>();
        for(int i=0;i<nums;i++){
            int index = (int)(Math.random()*valuelist.size());
            reslist.add(valuelist.get(index));
            valuelist.remove(index);
        }
        System.out.println(poker+"还有"+valuelist.size()+"张");
        if(valuelist.size()>0)
            robot_pokers.put(poker,valuelist);
        else
            robot_pokers.remove(poker);
        RoomsContains.Robots_Pokers.put(robot_id,robot_pokers);

        return reslist;
    }


    public static boolean IsGameOver(int client_id){
        Table table = RoomsContains.TABLE_MAP.get(ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId());
        if(RoomsContains.ROBOT_Tables_Client_Pokers.get(client_id)==0) {
            table.setWinner(client_id);
            return true;
        }
        else {
            LinkedList<ClientSide> clientSides = table.getClientSideList();
            for (ClientSide clientSide : clientSides) {
                if (clientSide.getRole().equals(ClientRole.ROBOT)) {
                    int robot_pokers_nums = RoomsContains.Robots_Pokers.get(clientSide.getId()).values().size();
                    if(robot_pokers_nums == 0){
                        table.setWinner(clientSide.getId());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Object[] getGameOverInfo(int client_id){
        Object[]data = new Object[5];
        data[0] = 42;
        //只发送机器人的手牌
        Table table = RoomsContains.TABLE_MAP.get(ServerContains.CLIENTSIDE_MAP.get(client_id).getTableId());
        int winner = table.getWinner();
        data[1]=winner;
        data[2]=Integer.MAX_VALUE;
        int i = 3;
        LinkedList<ClientSide>clientSides = table.getClientSideList();
        for(ClientSide clientSide : clientSides){
            if(clientSide.getRole().equals(ClientRole.ROBOT)) {
                List<Integer> list = new ArrayList<>();
                list.add(clientSide.getId());
                for (List<Integer> samevaluelist : RoomsContains.Robots_Pokers.get(clientSide.getId()).values()) {
                    list.addAll(samevaluelist);
                }
                data[i] = list;
                i++;
            }
        }
        return data;
    }

    public static void setLestSellType(int table_id , List<Integer>pokers){
        //dan//dui//san//san_dai_yi//san_dai_er//bomb//shunzi
        String res;
        if(pokers.size()==0)
            res="";
        else if(pokers.size() == 1)
            res = "dan";
        else if(pokers.size() == 2)
            res = "dui";
        else if(pokers.size() == 3 && pokers.get(0).equals(pokers.get(1)) && pokers.get(1).equals(pokers.get(2)))
            res = "san";
        else if(pokers.size() == 4 && pokers.get(0).equals(pokers.get(1))&&pokers.get(1).equals(pokers.get(2))&&pokers.get(2).equals(pokers.get(3)))
            res = "bomb";
        else if(pokers.size() == 4 && pokers.get(0).equals(pokers.get(1))&&pokers.get(1).equals(pokers.get(2))&& !pokers.get(2).equals(pokers.get(3)))
            res = "sandaiyi";
        else if(pokers.size() == 5 && pokers.get(0).equals(pokers.get(1))&&pokers.get(1).equals(pokers.get(2))&& !pokers.get(2).equals(pokers.get(3)))
            res = "sandaier";
        else
            res = "shunzi";
        RoomsContains.ROBOT_Tables_Flask_Last_Sell_Type.put(table_id,res);
    }

    public static Integer[] deepQShotPokers(int table_id, int robot_id){
        JSONObject play = new JSONObject();

        Integer[]lastSellPokers = RoomsContains.ROBOT_Tables_Last_Sell_Type.get(table_id).stream().toArray(Integer[]::new);
        if(lastSellPokers.length==0){
            play.put("last_move","");
            play.put("last_move_type","");
            play.put("player_id","");
            play.put("your_turn","true");
        }
        else {
            String[] strlastSellpokers = new String[lastSellPokers.length];
            for (int i = 0; i < lastSellPokers.length; i++)
                strlastSellpokers[i] = String.valueOf(lastSellPokers[i]);
            play.put("last_move", strlastSellpokers);
            play.put("last_move_type", RoomsContains.ROBOT_Tables_Flask_Last_Sell_Type.get(table_id));
            play.put("player_id", "19991116");
            play.put("your_turn", "true");
        }
        String strPlayRes = restTemplate.postForObject("http://localhost:5000/play",play,String.class);
        Map<String,Object>playRes = JSONObject.parseObject(strPlayRes,Map.class);
        //flask服务器清除了牌
        //本地也清除牌
        Integer[] res = JSONArray.parseObject(playRes.get("move").toString(),Integer[].class);
        List<Integer>realRes = new LinkedList<>();
        if(res.length == 0){
            //不出
            int nums = RoomsContains.Robots_Tables_Nums_Of_NotOut.get(table_id);
            nums++;
            if(nums == 2) {
                RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id,0);//电脑必须出
                //数组清空 //电脑可以自由出牌
                List<Integer>list = new LinkedList<>();
                RoomsContains.ROBOT_Tables_Last_Sell_Type.put(table_id,list);
                //设置flask
                RobotHelper.setLestSellType(table_id,list);
            }
            else
                RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id, nums);
        }
        else {
            for (int i = 0; i < res.length; i++) {
                realRes.addAll(removePoker(robot_id, res[i], 1));
            }
            RoomsContains.ROBOT_Tables_Last_Sell_Type.put(table_id, Arrays.asList(res));
            RoomsContains.Robots_Tables_Nums_Of_NotOut.put(table_id, 0);
            RoomsContains.ROBOT_Tables_Flask_Last_Sell_Type.put(table_id,playRes.get("move_type").toString()); //直接设置
        }
        return realRes.toArray(new Integer[realRes.size()]);
    }
}
