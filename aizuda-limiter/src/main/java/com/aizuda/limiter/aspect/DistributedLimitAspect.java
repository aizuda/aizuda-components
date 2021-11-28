package com.aizuda.limiter.aspect;

import com.aizuda.common.toolkit.MethodUtils;
import com.aizuda.limiter.annotation.DistributedLimit;
import com.aizuda.limiter.distributedlock.DistributedLockCallback;
import com.aizuda.limiter.exception.AcquireDistributedLockFailException;
import com.aizuda.limiter.extend.IDistributedLimitExtend;
import com.aizuda.limiter.handler.IDistributedLimitHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式锁拦截切面处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Aspect
@RequiredArgsConstructor
public class DistributedLimitAspect {
    private final Map<String, DistributedLimit> DISTRIBUTED_LIMIT_CACHE = new ConcurrentHashMap<>();
    private final IDistributedLimitHandler distributedLimitHandler;
    private final List<IDistributedLimitExtend> distributedLimitExtends;

    /**
     * 限流注解切面
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @return {@link Object}
     * @throws Throwable 限流异常
     */
    @Around("@annotation(com.aizuda.limiter.annotation.DistributedLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        final String classMethodName = MethodUtils.getClassMethodName(method);
        DistributedLimit distributedLimit = this.getDistributedLimit(method, classMethodName);

        Boolean notAddDistributedLock = Optional.ofNullable(distributedLimitExtends)
                .map(distributedLimitExtendList -> distributedLimitExtendList.stream()
                        .map(distributedLimitExtend -> distributedLimitExtend.notDistributedLock(method, pjp::getArgs, classMethodName, distributedLimit))
                        .filter(bool -> bool)
                        .findAny()
                        .orElse(Boolean.FALSE)
                ).orElse(Boolean.FALSE);
        if (notAddDistributedLock) {
            return pjp.proceed();
        }

        DistributedLockCallback callback = new DistributedLockCallback() {
            @SneakyThrows
            @Override
            public Object onGetLock() throws InterruptedException {
                return pjp.proceed();
            }

            @Override
            public Object onTimeout() throws InterruptedException {
                String errorMessage = Optional.of(distributedLimit.message())
                        .filter(str -> !"".equals(str))
                        .orElse(String.format("method [%s] acquire distributed lock fail", classMethodName));
                throw new AcquireDistributedLockFailException(errorMessage);
            }
        };
        return distributedLimitHandler.proceed(method, pjp::getArgs, classMethodName, distributedLimit, callback);
    }

    /**
     * 获取执行速率限制注解，缓存反射信息
     *
     * @param method          执行方法
     * @param classMethodName 执行类方法名
     * @return 方法对应的注解源信息，如果有，直接返回，如果无，获取放入缓存。
     */
    public DistributedLimit getDistributedLimit(Method method, String classMethodName) {
        return DISTRIBUTED_LIMIT_CACHE.computeIfAbsent(classMethodName, k -> method.getAnnotation(DistributedLimit.class));
    }
}
