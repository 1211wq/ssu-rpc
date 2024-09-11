package com.ikun.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.ikun.rpc.RpcApplication;
import com.ikun.rpc.config.RpcConfig;
import com.ikun.rpc.constant.RpcConstant;
import com.ikun.rpc.fault.retry.RetryStrategy;
import com.ikun.rpc.fault.retry.RetryStrategyFactory;
import com.ikun.rpc.fault.tolerant.TolerantStrategy;
import com.ikun.rpc.fault.tolerant.TolerantStrategyFactory;
import com.ikun.rpc.localBalancer.LoadBalancer;
import com.ikun.rpc.localBalancer.LoadBalancerFactory;
import com.ikun.rpc.model.RpcRequest;
import com.ikun.rpc.model.RpcResponse;
import com.ikun.rpc.model.ServiceMetaInfo;
import com.ikun.rpc.registry.Registry;
import com.ikun.rpc.registry.RegistryFactory;
import com.ikun.rpc.serializer.Serializer;
import com.ikun.rpc.serializer.SerializerFactory;
import com.ikun.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdk动态代理
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取配置信息
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 获取序列化方式
        final Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();


        // 从注册中心获取服务提供者地址
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(rpcRequest.getServiceName());
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());

        // 将调用的方法名（请求路径作为参数）
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        //  发送请求（http）
            /*
            try (HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
                return rpcResponse.getData();
            }
            */

        // 发送TCP请求,使用失败重试策略
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectServiceMetaInfo)
            );
            return rpcResponse.getData();
        } catch (Exception e) {
            // 容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
            throw new RuntimeException("调用失败");
        }
    }
}
