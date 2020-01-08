package com.software.pro.landlordsserver.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.Serializable;
import java.util.Map;

public class ServerIntegerArrayEncoder implements Encoder.Text<Object[]> {
    @Override
    public String encode(Object[] Data) {
        //将java对象转换为json字符串
        return JSONArray.fromObject(Data).toString();
    }
    @Override
    public void init(EndpointConfig endpointConfig) { }

    @Override
    public void destroy() { }
}
