/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.redislock.handler;


import com.aizuda.redislock.annotation.DistributedLimit;
import com.aizuda.redislock.distributedlock.DistributedLockCallback;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 分布式锁限制处理器接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public interface IDistributedLimitHandler {

    /**
     * 继续执行
     *
     * @param method           {@link Method}
     * @param args             Object[]
     * @param classMethodName  执行类方法名
     * @param distributedLimit 分布式锁限制注解对象
     * @param callback         回调策略
     * @return true 继续执行 false 限流不执行
     */
    Object proceed(Method method, Supplier<Object[]> args, String classMethodName, DistributedLimit distributedLimit,
                   DistributedLockCallback callback);
}
