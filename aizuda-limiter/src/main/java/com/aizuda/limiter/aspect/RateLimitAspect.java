/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.aspect;

import com.aizuda.common.toolkit.MethodUtils;
import com.aizuda.limiter.annotation.RateLimit;
import com.aizuda.limiter.exception.RateLimitException;
import com.aizuda.limiter.handler.IRateLimitHandler;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 速率限制拦截切面处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
@Aspect
@AllArgsConstructor
public class RateLimitAspect {
    /**
     * 缓存方法上的源注解信息。减少反射的开销
     */
    private static final Map<String, RateLimit> RATE_LIMIT_MAP = new ConcurrentHashMap<>();
    private final IRateLimitHandler rateLimitHandler;

    /**
     * 限流注解切面
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return {@link Object}
     * @throws Throwable 限流异常
     */
    @Around("@annotation(com.aizuda.limiter.annotation.RateLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        final String classMethodName = MethodUtils.getClassMethodName(method);
        RateLimit rateLimit = this.getRateLimit(method, classMethodName);
        if (rateLimitHandler.proceed(method, pjp::getArgs, classMethodName, rateLimit)) {
            return pjp.proceed();
        } else {
            throw new RateLimitException(!"".equals(rateLimit.message()) ? rateLimit.message() :
                    "current limiting rule triggered");
        }
    }

    /**
     * 获取执行速率限制注解，缓存反射信息
     *
     * @param method          执行方法
     * @param classMethodName 执行类方法名
     * @return 方法对应的注解源信息，如果有，直接返回，如果无，获取放入缓存。
     */
    public RateLimit getRateLimit(Method method, String classMethodName) {
        return RATE_LIMIT_MAP.computeIfAbsent(classMethodName, k -> method.getAnnotation(RateLimit.class));
    }
}
