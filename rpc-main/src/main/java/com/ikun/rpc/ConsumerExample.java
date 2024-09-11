package com.ikun.rpc;

import cn.hutool.core.io.FileUtil;
import com.ikun.rpc.config.RpcConfig;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);
        String configFileName = "application.yaml";

        if (FileUtil.exist(configFileName)) {
            System.out.println(configFileName + " 文件存在！");
        } else {
            System.out.println(configFileName + " 文件不存在！");
        }

    }
}
