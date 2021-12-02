/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.aspect;

import com.aizuda.common.toolkit.MethodUtils;
import com.aizuda.limiter.annotation.DistributedLock;
import com.aizuda.limiter.handler.IDistributedLockHandler;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式锁拦截切面处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua hubin
 * @since 2021-11-28
 */
@Aspect
@RequiredArgsConstructor
public class DistributedLockAspect {
    private final Map<String, DistributedLock> REDIS_LOCK_CACHE = new ConcurrentHashMap<>();
    private final IDistributedLockHandler redisLockHandler;

    /**
     * 分布式锁注解切面
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return {@link Object}
     * @throws Throwable 限流异常
     */
    @Around("@annotation(com.aizuda.limiter.annotation.DistributedLock)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        final String classMethodName = MethodUtils.getClassMethodName(method);
        final DistributedLock redisLock = this.getDistributedLock(method, classMethodName);
        return redisLockHandler.proceed(pjp, method, classMethodName, redisLock);
    }

    /**
     * 获取分布式锁注解，缓存反射信息
     *
     * @param method          执行方法
     * @param classMethodName 执行类方法名
     * @return 方法对应的注解源信息，如果有，直接返回，如果无，获取放入缓存。
     */
    public DistributedLock getDistributedLock(Method method, String classMethodName) {
        return REDIS_LOCK_CACHE.computeIfAbsent(classMethodName, k -> method.getAnnotation(DistributedLock.class));
    }
}
