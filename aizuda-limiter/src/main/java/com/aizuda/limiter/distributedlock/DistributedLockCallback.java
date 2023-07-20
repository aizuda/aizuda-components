/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.distributedlock;

/**
 * 分布式锁处理接口
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public interface DistributedLockCallback {

    /**
     * 成功获取分布式锁后执行方法
     *
     * @return 实体
     * @throws InterruptedException 中断异常
     */
    Object onGetLock() throws InterruptedException;

    /**
     * 获取分布式锁超时回调
     *
     * @return 实体
     * @throws InterruptedException 中断异常
     */
    Object onTimeout() throws InterruptedException;

}
