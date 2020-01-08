package com.software.pro.landlordsserver.utils;

import com.alibaba.fastjson.JSON;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ServerIntegerArrayDecoder implements Decoder.Text<Integer[]>{
    @Override
    public Integer[] decode(String Data) {
        //将java对象转换为json字符串
        return JSON.parseObject(Data, Integer[].class);
    }
    public boolean willDecode(String s) {
        // 验证json字符串是否合法，合法才会进入decode()方法进行转换，不合法直接抛异常
        return JSON.isValid(s);
    }

    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public void destroy() { }
}
