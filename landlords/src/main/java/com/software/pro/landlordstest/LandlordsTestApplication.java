package com.software.pro.landlordstest;

import org.omg.CORBA.INTERNAL;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@SpringBootApplication
public class LandlordsTestApplication {
    public static Comparator<Integer> pokerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            if(o1<14&&o2<14)
                return (o1+10)%13 - (o2+10)%13;
            else
                return o1-o2;
        }
    };

    public static void main(String[] args) {
        SpringApplication.run(LandlordsTestApplication.class, args);
    }
}
