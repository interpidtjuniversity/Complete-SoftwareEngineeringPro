package com.software.pro.landlordsserver.test;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;


import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DeepQAITest {
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        JSONObject cards = new JSONObject();
        cards.put("cards" , new String[]{"3","4","5","6","7","8","9","9","9","9","10","11","12","13","1","2","14"});
        //restTemplate.postForObject("http://localhost:5000/init_match",cards,String.class);
        //String belandlordRes = restTemplate.getForObject("http://localhost:5000/be_landlord",String.class);
        //System.out.println(belandlordRes);
//        JSONObject play = new JSONObject();
//        play.put("last_move",new String[]{"3","3"});
//        play.put("last_move_type","dui");
//        play.put("player_id","2");
//        play.put("your_turn","true");
//        String playRes = restTemplate.postForObject("http://localhost:5000/play",play,String.class);
//        System.out.println(playRes);
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(new Integer(2));
        System.out.println(list);
    }
}
