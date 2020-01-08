package com.software.pro.landlordsserver.utils;

import net.sf.json.JSONObject;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.Map;


public class ServletMapEncoder implements Encoder.Text<Map<String,Object>>{
    @Override
    public String encode(Map<String,Object> Data) {
        //将java对象转换为json字符串
        return JSONObject.fromObject(Data).toString();

    }
    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public void destroy() { }
}
