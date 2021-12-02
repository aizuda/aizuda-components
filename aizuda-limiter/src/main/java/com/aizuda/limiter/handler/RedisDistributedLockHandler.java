/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import com.aizuda.limiter.annotation.DistributedLock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;

/**
 * 分布式锁处理器 Redis 实现
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Slf4j
@AllArgsConstructor
public class RedisDistributedLockHandler implements IDistributedLockHandler {
    private static RedisScript<Long> REDIS_SCRIPT_LOCK = null;
    private static RedisScript<Long> REDIS_SCRIPT_UNLOCK = null;
    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitKeyParser rateLimitKeyParser;

    @Override
    public Object proceed(ProceedingJoinPoint pjp, Method method, String classMethodName, DistributedLock distributedLock) throws Throwable {
        long tid = Thread.currentThread().getId();
        StringBuffer value = new StringBuffer();
        value.append(tid).append(Math.random());
        final String lockKey = rateLimitKeyParser.buildKey(method, pjp::getArgs, classMethodName, distributedLock.key(), distributedLock.strategy());
        boolean locked = false;
        try {
            // 失效时间解析为秒
            long expire = DurationStyle.detectAndParse(distributedLock.expire()).getSeconds();
            if (null == REDIS_SCRIPT_LOCK) {
                REDIS_SCRIPT_LOCK = RedisScript.of("if redis.call('setNx',KEYS[1],ARGV[1]) == 1 then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end", Long.class);
            }
            if (Objects.equals(1L, redisTemplate.execute(REDIS_SCRIPT_LOCK, Collections.singletonList(lockKey), value.toString(), String.valueOf(expire)))) {
                if (log.isDebugEnabled()) {
                    log.debug("Thread:{} Lock acquisition succeeded", tid);
                }
                locked = true;
                return pjp.proceed();
            }
        } finally {
            if (locked) {
                // 释放分布式锁
                if (null == REDIS_SCRIPT_UNLOCK) {
                    REDIS_SCRIPT_UNLOCK = RedisScript.of("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.class);
                }
                redisTemplate.execute(REDIS_SCRIPT_UNLOCK, Collections.singletonList(lockKey), value.toString());
                log.info("Thread:{} Release lock", tid);
            } else if (log.isDebugEnabled()) {
                log.debug("Thread:{} Failed to acquire lock", tid);
            }
        }
        return null;
    }
}
