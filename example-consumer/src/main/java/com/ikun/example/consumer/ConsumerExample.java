package com.ikun.example.consumer;

import com.ikun.example.common.model.User;
import com.ikun.example.common.service.UserService;
import com.ikun.rpc.RpcApplication;
import com.ikun.rpc.bootstrap.ConsumerBootstrap;
import com.ikun.rpc.config.RpcConfig;
import com.ikun.rpc.proxy.ServiceProxyFactory;

public class ConsumerExample {
    public static void main(String[] args) {

        // 初始化
//        ConsumerBootstrap.init();

        // 获取UserService的实现对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("ssu");

        //  调用
        for (int i = 0; i < 3; i++) {
            User newUser = userService.getUser(user);
            if (newUser != null) {
                System.out.println("用户名：" + newUser.getName());
            } else {
                System.out.println("user==null");
            }
        }
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);
    }
}