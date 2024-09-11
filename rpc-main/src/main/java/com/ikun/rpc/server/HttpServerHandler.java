package com.ikun.rpc.server;

import com.ikun.rpc.RpcApplication;
import com.ikun.rpc.model.RpcRequest;
import com.ikun.rpc.model.RpcResponse;
import com.ikun.rpc.registry.LocalRegistry;
import com.ikun.rpc.serializer.Serializer;
import com.ikun.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        //  指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //  记录日志
        System.out.println("Received request：" + request.method());
        //  异步处理请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserializer(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //构造响应对象
            RpcResponse rpcResponse = new RpcResponse();
            //  如果请求为null，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequese is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                //  获取调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                rpcResponse.setData(result);
                rpcResponse.setMessage("ok");
                rpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //  响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /*
     * 响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     * */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {

        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] serializered = serializer.serializer(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serializered));
        } catch (Exception e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }

    }
}
