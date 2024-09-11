package com.ikun.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import com.ikun.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceCache {
    /**
    * 服务缓存
    * */
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
    * 写缓存
    * */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        serviceCache.put(serviceKey, newServiceCache);
    }

    /**
    * 读缓存
    * */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        return serviceCache.get(serviceKey);
    }

    void remove(ServiceMetaInfo serviceMetaInfo) {
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = serviceCache.get(serviceKey);
        serviceMetaInfoList.remove(serviceMetaInfo);
        if (CollUtil.isEmpty(serviceMetaInfoList)){
            serviceCache.remove(serviceKey);
        }
    }

    /**
    * 清空缓存
    * */
    void clearCache() {
        this.serviceCache = null;
    }
}
