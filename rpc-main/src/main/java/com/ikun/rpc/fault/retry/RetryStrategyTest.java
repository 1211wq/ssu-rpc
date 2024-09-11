package com.ikun.rpc.fault.retry;

import com.ikun.rpc.model.RpcResponse;
import org.junit.Test;

public class RetryStrategyTest {

    RetryStrategy noRetryStrategy = new NoRetryStrategy();
    RetryStrategy fixedIntervalRetryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = fixedIntervalRetryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("重试失败");
            });
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }

    }

}
