package com.software.pro.landlordsserver.utils.helper;

import com.software.pro.landlordsserver.entity.Poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PokerHelper {
    //发牌函数
    //重载比较
    public static Comparator<Integer> pokerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            if(o1<14&&o2<14)
                return (o1+10)%13 - (o2+10)%13;
            else
                return o1-o2;
        }
    };

    public static void sortPoker(List<Integer> pokers){
        Collections.sort(pokers,pokerComparator);
    }

    public static List<List<Integer>> distributePoker(){
        List<Integer> basePokers = new ArrayList<Integer>(54);
        for(int i=0;i< 54; i++){
            basePokers.add(i);
        }
        Collections.shuffle(basePokers);
        List<List<Integer>> pokersList = new ArrayList<List<Integer>>();
        List<Integer> pokers1 = new ArrayList<>(17);
        pokers1.addAll(basePokers.subList(0, 17));
        List<Integer> pokers2 = new ArrayList<>(17);
        pokers2.addAll(basePokers.subList(17, 34));
        List<Integer> pokers3 = new ArrayList<>(17);
        pokers3.addAll(basePokers.subList(34, 51));
        List<Integer> pokers4 = new ArrayList<>(3);
        pokers4.addAll(basePokers.subList(51, 54));
        pokersList.add(pokers1);
        pokersList.add(pokers2);
        pokersList.add(pokers3);
        pokersList.add(pokers4);

        return pokersList;
    }
}
