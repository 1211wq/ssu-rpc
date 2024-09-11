package com.ikun.rpc.config;

import lombok.Data;

/*
* rpc框架注册中心配置
* */
@Data
public class RegistryConfig {
    /*
    * 注册中心类别
    * */
    private String registry = "etcd";

    /*
    * 注册中心地址
    * */
    private String address = "http://192.168.74.136:2379";

    /*
    * 用户名
    * */
    private String userName;

    /*
    * 密码
    * */
    private String password;

    /*
    * 超时时间
    * */
    private Long timeout = 10000L;
}
