package com.ikun.rpc.server;

/*
* HTTP服务器接口,定义统一的服务器方法，便于后续的扩展
* */
public interface HttpServer {

    /*
    * 启动服务器
    *
    * @param port
    * */
    void doStart(int port);
}
