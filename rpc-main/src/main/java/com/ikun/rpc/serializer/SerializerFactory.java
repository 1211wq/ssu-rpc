package com.ikun.rpc.serializer;

/*
* 序列化工厂，用于获取序列化器
* */

import com.ikun.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    /*
    * 序列化器映射，用于实现单例
    * */
   /* private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {{
        put(SerializerKeys.JDK, new JdkSerializer());
        put(SerializerKeys.JSON, new JsonSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.HESSIAN, new HessianSerializer());
    }};*/

    static {
        SpiLoader.load(Serializer.class);
        System.out.println("初始化");
    }

    /*
    * 默认序列化器
    * */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /*
    * 获取实例
    * */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
//        return KEY_SERIALIZER_MAP.get(key);
    }
}
