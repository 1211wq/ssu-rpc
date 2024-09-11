package com.ikun.rpc.server.tcp;

import com.ikun.rpc.model.RpcResponse;
import com.ikun.rpc.protocol.ProtocolMessage;
import com.ikun.rpc.protocol.ProtocolMessageDecoder;
import com.ikun.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

import java.io.IOException;

public class VertxTcpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        //  创建vertx实例
        Vertx vertx = Vertx.vertx();

        //  创建tcp服务器
        NetServer server = vertx.createNetServer();

        //  处理请求
        server.connectHandler(new TcpServerHandler());

        // 启动tcp服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }
}
