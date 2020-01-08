package com.software.pro.landlordsserver.events.TableEvent;

import com.software.pro.landlordsserver.enums.TableEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface TableEventListener {
    public void call(int table_id);
    public void call(int client_id,int op);
    public void call(int client_id,Object message,int flag);

    public final static Map<TableEventCode, TableEventListener> LISTENER_MAP = new HashMap<>();

    final static String LISTENER_PREFIX = "com.software.pro.landlordsserver.events.TableEvent.TableEventListener_";

    @SuppressWarnings("unchecked")
    public static TableEventListener get(TableEventCode code){
        TableEventListener listener = null;
        try {
            if(TableEventListener.LISTENER_MAP.containsKey(code)){
                listener = TableEventListener.LISTENER_MAP.get(code);
            }else{
                String eventListener = LISTENER_PREFIX + code.name();
                Class<TableEventListener> listenerClass = (Class<TableEventListener>) Class.forName(eventListener);
                try {
                    listener = listenerClass.getDeclaredConstructor().newInstance();
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                TableEventListener.LISTENER_MAP.put(code, listener);
            }
            return listener;
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return listener;
    }
}
