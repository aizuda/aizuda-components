package com.aizuda.limiter.distributedlock;

import com.aizuda.limiter.exception.DistributedLimitException;
import com.aizuda.limiter.toolkit.RedisLockRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


/**
 * redis执行分布式锁
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDistributedLockTemplate implements DistributedLockTemplate {

    private final RedisLockRegistry redisLockRegistry;

    @Override
    public Object execute(String lockId, Integer timeout, TimeUnit unit, DistributedLockCallback callback) {

        Lock lock = null;
        boolean getLock = false;
        try {
            lock = redisLockRegistry.obtain(lockId);
            getLock = lock.tryLock(timeout, unit);
            if (getLock) {
                // 拿到锁
                return callback.onGetLock();
            } else {
                // 未拿到锁
                return callback.onTimeout();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new DistributedLimitException(ex);
        } finally {
            if (getLock) {
                // 释放锁
                lock.unlock();
            }
        }
    }
}
