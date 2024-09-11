package com.ikun.example.provider;

import com.ikun.example.common.service.UserService;
import com.ikun.rpc.bootstrap.ProviderBootstrap;
import com.ikun.rpc.model.ServiceRegisterInfo;
import java.util.ArrayList;
import java.util.List;

public class ProviderExample {
    public static void main(String[] args) {

        // 要提供的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}