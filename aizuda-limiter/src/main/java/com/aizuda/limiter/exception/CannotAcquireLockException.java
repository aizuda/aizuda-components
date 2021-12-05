/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.exception;

import lombok.Getter;

/**
 * 不能获取到分布式锁异常，
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-12-04
 */
public class CannotAcquireLockException extends DistributedLockException {
    /**
     * 分布式锁的key
     */
    @Getter
    private final String lockKey;


    public CannotAcquireLockException(String lockKey, String message, Throwable cause) {
        super(message, cause);
        this.lockKey = lockKey;
    }
}
