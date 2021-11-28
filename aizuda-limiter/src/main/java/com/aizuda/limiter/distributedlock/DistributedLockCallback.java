package com.aizuda.limiter.distributedlock;

/**
 * 分布式锁处理接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public interface DistributedLockCallback {

    /**
     * 成功获取锁后执行方法
     *
     * @return 实体
     * @throws InterruptedException 中断异常
     */
    Object onGetLock() throws InterruptedException;

    /**
     * 获取锁超时回调
     *
     * @return 实体
     * @throws InterruptedException 中断异常
     */
    Object onTimeout() throws InterruptedException;
}
