/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.distributedlock;

import com.aizuda.limiter.exception.DistributedLockException;
import com.aizuda.limiter.toolkit.RedisLockRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


/**
 * 默认的Redis执行分布式锁
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Slf4j
@Component
public class RedisDistributedLockTemplate implements IDistributedLockTemplate {

    private final RedisLockRegistry redisLockRegistry;

    public RedisDistributedLockTemplate(RedisTemplate<String, String> redisTemplate, String registryKey, long expireAfter) {
        this.redisLockRegistry = new RedisLockRegistry(redisTemplate, registryKey, expireAfter);
    }

    @Override
    public Object execute(String lockId, long timeout, TimeUnit unit, DistributedLockCallback callback) {

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
            throw new DistributedLockException(ex);
        } finally {
            if (getLock) {
                // 释放锁
                lock.unlock();
            }
        }
    }

    @Override
    public String completeLockKey(String lockId) {
        return redisLockRegistry.completeLockKey(lockId);
    }
}
