package com.ikun.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ikun.example.common.model.User;
import com.ikun.example.common.service.UserService;
import com.ikun.rpc.model.RpcRequest;
import com.ikun.rpc.model.RpcResponse;
import com.ikun.rpc.serializer.JdkSerializer;
import com.ikun.rpc.serializer.Serializer;

/*
* 静态代理
* */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //  指定序列化器
        Serializer serializer = new JdkSerializer();
       //   发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
