package com.aizuda.redislock.extend;


import com.aizuda.redislock.annotation.DistributedLimit;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 分布式锁扩展
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public interface IDistributedLimitExtend {

    /**
     * 在进行获取分布式锁之前运行，如果有的环境需要加锁，有的不用，可以扩展此方法
     *
     * @param method           当前运行方法
     * @param args             方法的运行时参数
     * @param classMethodName  当前方法名
     * @param distributedLimit 当前注解
     * @return 接下来是否不要加锁
     */
    default boolean notDistributedLock(Method method, Supplier<Object[]> args, String classMethodName,
                                       DistributedLimit distributedLimit) {
        return false;
    }

    /**
     * 在进行锁定之前需要干的事
     *
     * @param method           当前运行方法
     * @param args             方法的运行时参数
     * @param classMethodName  当前方法名
     * @param distributedLimit 当前注解
     * @param lockKey          分布式锁的key
     */
    void beforeDistributedLock(Method method, Supplier<Object[]> args, String classMethodName, DistributedLimit distributedLimit,
                               String lockKey);
}
