package com.aizuda.limiter.distributedlock;

import java.util.concurrent.TimeUnit;


/**
 * 分布式锁模板类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public interface DistributedLockTemplate {

    /**
     * 执行方法
     *
     * @param lockId   锁id(对应业务唯一ID)
     * @param timeout  最大等待获取锁时间
     * @param unit     等待时间单位
     * @param callback 回调方法
     * @return 执行返回的数据
     */
    Object execute(String lockId, Integer timeout, TimeUnit unit, DistributedLockCallback callback);
}
