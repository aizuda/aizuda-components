package com.aizuda.redislock.exception;

/**
 * 获取分布式锁失败异常，到时候可以全局捕获此异常，返回前端信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
public class AcquireDistributedLockFailException extends DistributedLimitException {

    public AcquireDistributedLockFailException(String message) {
        super(message);
    }

}
