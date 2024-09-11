package com.ikun.rpc.fault.retry;

import com.ikun.rpc.model.RpcResponse;
import java.util.concurrent.Callable;

/**
 * 充实策略
 */
public interface RetryStrategy {

    /**
     * 重试
     *
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
