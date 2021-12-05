/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;


import com.aizuda.limiter.metadata.DistributedLockMethodMetaData;
import com.aizuda.limiter.metadata.MethodMetadata;
import org.aspectj.lang.ProceedingJoinPoint;

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
     * @param pjp            {@link ProceedingJoinPoint}
     * @param methodMetadata {@link DistributedLockMethodMetaData}
     * @return Object
     */
    Object proceed(ProceedingJoinPoint pjp, MethodMetadata methodMetadata) throws Throwable;
}
