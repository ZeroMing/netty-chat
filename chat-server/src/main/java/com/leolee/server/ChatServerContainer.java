package com.leolee.server;

import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/6 14:03
 */
public class ChatServerContainer {

    public static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();


    public static  void put(String clientId,Channel channel){
        if(map.containsKey(clientId)){
            return;
        }
        map.put(clientId,channel);
    }


    public static void clear(){
        map.clear();
    }

    public static Channel get(String clientId){
        return map.get(clientId);
    }

    /**
     * 根据ID移除Channel
     * @param clientId
     */
    public static  void remove(String clientId){
        if(map.containsKey(clientId)){
            map.remove(clientId);
        }

    }

    /**
     * 移除指定的Channel
     * @param channel
     */
    public static void remove(Channel channel){
        Set<Map.Entry<String, Channel>> entries = map.entrySet();
        Iterator<Map.Entry<String, Channel>> iterator = entries.iterator();
        String clientId = null;
        while (iterator.hasNext()) {
            Map.Entry<String, Channel> next = iterator.next();
            if(next.getValue() == channel){
                clientId = next.getKey();
            }
        }
        if(clientId != null){
            remove(clientId);
        }
    }

}
