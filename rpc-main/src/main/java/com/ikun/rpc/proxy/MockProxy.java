package com.ikun.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke: {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    private Object getDefaultObject(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == int.class){
                return 0;
            } else if (type == Long.class){
                return 0L;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == boolean.class){
                return false;
            }
        }
        return null;
    }
}
