package com.ikun.rpc.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
* 本地注册中心
* */
public class LocalRegistry {

    /*
    * 用线程安全的map存储注册信息
    * */
    public static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /*
    * 服务注册
    * */
    public static void register(String serviceName, Class<?> implClass) {
        map.put(serviceName, implClass);
    }

    /*
    * 获取服务
    * */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /*
    * 删除服务
    * */
    public static void remove(String serviceName){
        map.remove(serviceName);
    }


}
