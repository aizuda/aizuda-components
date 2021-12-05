/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.distributedlock;

import java.util.concurrent.TimeUnit;


/**
 * 分布式锁模板类
 * 默认使用{@link com.aizuda.limiter.toolkit.RedisLockRegistry}实现分布式锁，
 * 如果您对分布式锁可靠性要求非常高，可以引入Redisson并实现此接口，引入到系统中
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public interface IDistributedLockTemplate {

    /**
     * 执行方法
     * 当获取到锁后，需要调用{@link DistributedLockCallback#onGetLock()}
     * 当获取锁超时后，需要调用{@link DistributedLockCallback#onTimeout()}
     *
     * @param lockId   锁id(对应业务唯一ID)
     * @param timeout  最大等待获取锁时间
     * @param unit     等待时间单位
     * @param callback 回调方法
     * @return 执行返回的数据
     */
    Object execute(String lockId, long timeout, TimeUnit unit, DistributedLockCallback callback);
}
