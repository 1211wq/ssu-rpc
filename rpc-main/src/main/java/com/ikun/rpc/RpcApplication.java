package com.ikun.rpc;

import com.ikun.rpc.config.RegistryConfig;
import com.ikun.rpc.config.RpcConfig;
import com.ikun.rpc.constant.RpcConstant;
import com.ikun.rpc.registry.Registry;
import com.ikun.rpc.registry.RegistryFactory;
import com.ikun.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    public static volatile RpcConfig rpcConfig;

    /**
    * 框架初始化，支持传入自定义配置
    */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config={}",newRpcConfig.toString());
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

        //  注册中心初始化
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,config={}",registryConfig);

        //  创建并注册Shutdown Hook， JVM退出是执行
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));

    }

    /**
    * 初始化
    */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            //  加载配置失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }


    /**
    * 获取配置
    * 单例，双重校验锁
    * @return
    */
    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}