/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.metadata;

import com.aizuda.common.toolkit.MethodUtils;
import com.aizuda.limiter.annotation.DistributedLock;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 当前分布式锁的运行时的信息
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 2021-12-04
 */
public class DistributedLockMethodMetaData implements MethodMetadata {
    /**
     * 方法
     */
    private final Method method;
    /**
     * 当前运行时方法中的参数
     */
    private final Supplier<Object[]> argsSupplier;
    /**
     * 当前方法的全称
     * 使用{@link MethodUtils#getClassMethodName(Method)}方法获得
     */
    private final String classMethodName;
    /**
     * 分布式锁所用到的注解
     */
    private final DistributedLock distributedLock;

    public DistributedLockMethodMetaData(Method method, Supplier<Object[]> argsSupplier, DistributedLock distributedLock) {
        Assert.notNull(method, "'method' cannot be null");
        Assert.notNull(argsSupplier, "'argsSupplier' cannot be null");
        Assert.notNull(distributedLock, "'distributedLock' cannot be null");
        this.method = method;
        this.argsSupplier = argsSupplier;
        this.distributedLock = distributedLock;
        this.classMethodName = MethodUtils.getClassMethodName(method);
    }

    @Override
    public String getClassMethodName() {
        return classMethodName;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArgs() {
        return argsSupplier.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation() {
        return (T) distributedLock;
    }
}
