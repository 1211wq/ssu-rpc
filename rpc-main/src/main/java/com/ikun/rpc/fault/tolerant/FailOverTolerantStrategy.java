package com.ikun.rpc.fault.tolerant;

import com.ikun.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 故障转移
 */
public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 调用其他服务
        return null;
    }
}
