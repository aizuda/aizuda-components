package com.aizuda.redislock.strategy;


import com.aizuda.redislock.annotation.DistributedLimit;

import java.lang.reflect.Method;

/**
 * 自定义key生成策略
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-28
 */
public interface IDistributedKeyBuilderStrategy {

    /**
     * 当前策略是否支持
     *
     * @param method              当前运行方法
     * @param classMethodName     当前方法名
     * @param distributedLimit    当前注解
     * @param distributedLimitKey 解析出来的key
     * @return 是否支持
     */
    boolean support(Method method, String classMethodName, DistributedLimit distributedLimit, String distributedLimitKey);

    /**
     * 构造当前key
     *
     * @param method              当前运行方法
     * @param classMethodName     当前方法名
     * @param distributedLimit    当前注解
     * @param distributedLimitKey 解析出来的key
     * @return 构造出来的key
     */
    String buildKey(Method method, String classMethodName, DistributedLimit distributedLimit, String distributedLimitKey);
}
