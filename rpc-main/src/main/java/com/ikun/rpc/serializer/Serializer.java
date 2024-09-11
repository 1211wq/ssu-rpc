package com.ikun.rpc.serializer;

import java.io.IOException;

public interface Serializer {
    /*
    * 序列化
    * @param object
    * @param <T>
    * @return
    * @throw IOException
    * */
    <T> byte[] serializer(T object) throws IOException;

    /*
    * 反序列化
    * @param bytes
    * @param type
    * @param <T>
    * @return
    * @throws IOException
    * */

    <T> T deserializer(byte[] bytes, Class<T> type) throws IOException;
}
