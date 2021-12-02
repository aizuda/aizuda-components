/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;


import com.aizuda.limiter.annotation.DistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * 分布式锁限制处理器接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua hubin
 * @since 2021-11-28
 */
public interface IDistributedLockHandler {

    /**
     * 继续执行
     *
     * @param pjp             {@link ProceedingJoinPoint}
     * @param method          {@link Method}
     * @param classMethodName 执行类方法名
     * @param distributedLock 分布式锁限制注解对象
     * @return Object
     */
    Object proceed(ProceedingJoinPoint pjp, Method method, String classMethodName, DistributedLock distributedLock) throws Throwable;
}
