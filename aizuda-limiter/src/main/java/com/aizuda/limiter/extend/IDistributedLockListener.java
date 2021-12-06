/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.extend;


import com.aizuda.limiter.metadata.DistributedLockMethodMetaData;
import com.aizuda.limiter.metadata.MethodMetadata;

/**
 * 分布式锁扩展
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-12-05
 */
public interface IDistributedLockListener {

    /**
     * 判断是否支持当前监听
     *
     * @param methodMetadata 当前运行时参数 {@link DistributedLockMethodMetaData}
     * @return 是否支持
     */
    boolean supports(MethodMetadata methodMetadata);

    /**
     * 在加锁之前需要运行
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param lockKey        自定义封装的key
     */
    void beforeDistributedLock(MethodMetadata methodMetadata, String lockKey);

    /**
     * 在加锁之后还未执行用户方法时运行
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param lockKey        自定义封装的key
     */
    void afterDistributedLock(MethodMetadata methodMetadata, String lockKey);


    /**
     * 在加锁之后并运行用户方法后运行
     * 注意：此方法有可能因执行用户方法后异常不运行
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param lockKey        自定义封装的key
     * @param result         结果
     */
    void afterExecute(MethodMetadata methodMetadata, String lockKey, Object result);

    /**
     * 一定会运行的方法，可能还未加锁成功
     *
     * @param methodMetadata 当前方法的运行时参数 {@link DistributedLockMethodMetaData}
     * @param lockKey        自定义封装的key
     */
    void distributedLockFinally(MethodMetadata methodMetadata, String lockKey);


}
