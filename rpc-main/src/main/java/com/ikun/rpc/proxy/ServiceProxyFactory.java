package com.ikun.rpc.proxy;

import com.ikun.rpc.RpcApplication;
import com.ikun.rpc.config.RpcConfig;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {
    /**
    * 根据服务列获取代理对象
    *
    * @param serviceClass
    * @param <T>
    * @return
    */
    public static <T> T getProxy(Class<T> serviceClass){

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        if (rpcConfig.isMock()) {
            return (T) Proxy.newProxyInstance(
                    serviceClass.getClassLoader(),
                    new Class[]{serviceClass},
                    new MockProxy()
            );
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    /*
    * 根据服务类获取Mock代理对象
    * */
//    public static <T> T getMockProxy(Class<T> serviceClass){
//        return (T) Proxy.newProxyInstance(
//                serviceClass.getClassLoader(),
//                new Class[]{serviceClass},
//                new MockProxy()
//        );
//    }
}
