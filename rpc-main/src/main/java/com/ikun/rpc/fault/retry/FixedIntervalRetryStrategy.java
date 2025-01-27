package com.ikun.rpc.fault.retry;

import com.ikun.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{
    /**
     * 重试
     *
     * @param callable
     * @return
     * @throws ExecutionException
     * @throws
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception /*, RetryException*/{
//        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
//                .retryIfExceptionOfType(Exception.class)
//                .withWaitStrategy(WaitStrategies.fixedWait(2l, TimeUnit.SECONDS))
//                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
//                .withRetryListener(new RetryListener() {
//                    @Override
//                    public <V> void onRetry(Attempt<V> attempt) {
//                        log.info("重试次数：{}", attempt.getAttemptNumber());
//                    }
//                })
//                .build();
//
//        return retryer.call(callable);
        return callable.call();
    }
}
