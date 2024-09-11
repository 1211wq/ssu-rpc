package com.ikun.rpc.fault.tolerant;

import com.ikun.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 故障恢复
 */
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 调用降级的服务
        return null;
    }
}
