/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.exception;

/**
 * 获取分布式锁超时
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 2021-12-04
 */
public class AcquireLockTimeoutException extends DistributedLockException{

    public AcquireLockTimeoutException(String message) {
        super(message);
    }
}
