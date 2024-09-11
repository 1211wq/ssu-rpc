package com.ikun.rpc.registry;

import com.ikun.rpc.config.RegistryConfig;
import com.ikun.rpc.model.ServiceMetaInfo;

import java.util.List;

/*
* 注册中心
* */
public interface Registry {

    /**
    * 初始化
    * */
    void init(RegistryConfig registryConfig);

    /**
    * 注册服务（服务端）
    * */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
    * 销毁服务
    * */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
    * 服务发现（消费者）
    * */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
    * 服务销毁
    * */
    void destroy();

    /**
    * 心跳检测
    * */
    void heartBeat();

    /**
    * 监听（消费端）
    * */
    void watch(String serviceNodeKey);
}
