package com.ikun.rpc.config;

import com.ikun.rpc.fault.retry.RetryStrategyKeys;
import com.ikun.rpc.fault.tolerant.TolerantStrategyKeys;
import com.ikun.rpc.localBalancer.LoadBalancerKeys;
import com.ikun.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
* rpc框架配置
*/
@Data
public class RpcConfig {

    /**
    * 名称
    */
    private String name = "rpc";

    /**
    * 版本号
    */
    private String version = "1.0.0";

    /**
    * 服务器主机名
    */
    private String serviceHost = "localhost";

    /**
    * 服务器端口号
    */
    private Integer serverPort = 8080;

    /**
    * 模拟调用
    */
    private boolean mock = false;

    /**
    * 序列化器
    */
    private String serializer = SerializerKeys.JDK;

    /**
    * 注册中心配置
    */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
