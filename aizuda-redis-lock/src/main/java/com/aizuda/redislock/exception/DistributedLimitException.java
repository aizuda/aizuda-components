package com.aizuda.redislock.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分布式锁限制异常
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@ToString
@Setter
@Getter
public class DistributedLimitException extends RuntimeException {

    public DistributedLimitException(String message) {
        super(message);
    }

    public DistributedLimitException(String message, Throwable throwable) {
        super(message, throwable);
    }


    public DistributedLimitException(Throwable throwable) {
        super(throwable);
    }
}
