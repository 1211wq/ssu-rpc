package com.ikun.rpc.bootstrap;

import com.ikun.rpc.RpcApplication;

/**
 * 服务消费者启动类
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        // RPC框架初始化（配置和注册中心）
        RpcApplication.init();
    }
}
