/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import com.aizuda.limiter.annotation.RateLimit;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 速率限制处理器接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
public interface IRateLimitHandler {

    /**
     * 继续执行
     *
     * @param method          {@link Method}
     * @param args            Object[]
     * @param classMethodName 执行类方法名
     * @param rateLimit       速率限制注解对象
     * @return true 继续执行 false 限流不执行
     */
    boolean proceed(Method method, Supplier<Object[]> args, String classMethodName, RateLimit rateLimit);
}
