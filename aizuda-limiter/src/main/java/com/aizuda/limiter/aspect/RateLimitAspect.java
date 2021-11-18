/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.aspect;

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
    private static Map<String, RateLimit> RATE_LIMIT_MAP = new ConcurrentHashMap<>();
    private final IRateLimitHandler rateLimitHandler;

    /**
     * 限流注解切面
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return {@link Object}
     * @throws Throwable
     */
    @Around("@annotation(com.aizuda.limiter.annotation.RateLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        final String classMethodName = this.getClassMethodName(method);
        RateLimit rateLimit = this.getRateLimit(method, classMethodName);
        if (rateLimitHandler.proceed(method, () -> pjp.getArgs(), classMethodName, rateLimit)) {
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
     * @return
     */
    public RateLimit getRateLimit(Method method, String classMethodName) {
        RateLimit rateLimit = RATE_LIMIT_MAP.get(classMethodName);
        if (null == rateLimit) {
            rateLimit = method.getAnnotation(RateLimit.class);
            RATE_LIMIT_MAP.put(classMethodName, rateLimit);
        }
        return rateLimit;
    }

    /**
     * 获取执行类方法名
     *
     * @param method 执行方法
     * @return
     */
    public String getClassMethodName(Method method) {
        return String.format("%s.%s", method.getDeclaringClass().getName(), method.getName());
    }
}
