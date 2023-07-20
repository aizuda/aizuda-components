/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.toolkit;

import com.aizuda.limiter.exception.CannotAcquireLockException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现基本高可用的分布式锁
 * 基本原理是Redis使用lua脚本执行时会将脚本作为一个整体执行，中间不会被其他命令插入，这就保证了原子性
 * <p>
 * 注意：此实现最大的缺点是只作用在一个Redis节点上，即使Redis通过Sentinel(哨岗、哨兵)保证高可用，
 * 如果master节点由于某些原因发生了主从切换，那么就有可能出现锁丢失的情况
 * 比如：
 * 1. 客户端在Redis的master节点中获取了分布式锁
 * 2. 此时分布式锁的key还未同步到slave节点
 * 3. master故障，slave节点升级为master节点
 * 4. 此时master节点的分布式锁的key就会丢失
 * <p>
 * 如果您对Redis分布式锁 '高可用' 要求非常高，可以使用Redisson实现分布式锁功能并插入到此模块中，相关文档如下
 * 中文文档：http://www.redis.cn/topics/distlock.html
 * 英文文档：https://redis.io/topics/distlock
 *
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 2021-12-04
 */
@Slf4j
public class RedisLockRegistry {

    private static final long DEFAULT_EXPIRE_AFTER = 60000L;

    /**
     * 作为Redis分布式锁对应key中的value存放到Redis中，保证同一个线程操作同一个key可重用
     * 具体表现为：当需要获取分布式锁时，先计算当前clientId是否和Redis中对应key的value相等，如果相等，说明是同一个实例，即是同一个线程。
     * 此时只要重新设置失效时间即可
     */
    private final String clientId = UUID.randomUUID().toString();

    /**
     * 获取分布式锁的lua脚本
     * <p>
     * 解释：先获取分布式key对应的value和当前clientId比较，如果相等，重新设置失效时间，之后没有value就设置值，否则获取不到值
     * <p>
     * 这里设置分布式锁使用set命令而没用setnx是因为Redis 2.6.12版本后，set命令整合了setex的功能，并且set本身就已经包含了设置过期时间，
     * 因此常说的set命令加上选项已经完全可以取代setnx命令，并且在将来的版本中，Redis可能不推荐并最终抛弃
     * 官方文档如下：
     * 中文文档：http://www.redis.cn/commands/set.html
     * 英文文档：https://redis.io/commands/set
     */
    private static final String OBTAIN_LOCK_SCRIPT =
            "local lockClientId = redis.call('GET', KEYS[1])\n" +
                    "if lockClientId == ARGV[1] then\n" +
                    "  redis.call('PEXPIRE', KEYS[1], ARGV[2])\n" +
                    "  return true\n" +
                    "elseif not lockClientId then\n" +
                    "  redis.call('SET', KEYS[1], ARGV[1], 'PX', ARGV[2])\n" +
                    "  return true\n" +
                    "end\n" +
                    "return false";

    /**
     * 使用lua脚本保证原子删除
     * <p>
     * 解释：如果对应的value等于传入的uuid，执行删除，不成功返回0
     */
    private static final String DELETE_LOCK_SCRIPT =
            "       if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "  return redis.call('del', KEYS[1])" +
                    "else return 0 end";

    /**
     * 每一个key对应一把锁
     */
    private final Map<String, RedisLock> locks = new ConcurrentHashMap<>();


    /**
     * 为每一个key加上一个前缀，
     * 1.到时候使用scan 0 match [registryKey]*可以查看当前正在使用的分布式锁
     * 2.有时候一个多个项目的key有可能相同，有前缀也减少重复的概率
     */
    private final String registryKey;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisScript<Boolean> obtainLockScript;
    private final RedisScript<Long> deleteLockScript;

    /**
     * 该分布式锁失效的时间
     */
    private final long expireAfter;


    public RedisLockRegistry(RedisTemplate<String, String> redisTemplate, String registryKey) {

        this(redisTemplate, registryKey, DEFAULT_EXPIRE_AFTER);
    }

    public RedisLockRegistry(RedisTemplate<String, String> redisTemplate, String registryKey, long expireAfter) {
        Assert.notNull(redisTemplate, "'redisTemplate' cannot be null");
        Assert.notNull(registryKey, "'registryKey' cannot be null");
        this.redisTemplate = redisTemplate;
        this.obtainLockScript = RedisScript.of(OBTAIN_LOCK_SCRIPT, Boolean.class);
        this.deleteLockScript = RedisScript.of(DELETE_LOCK_SCRIPT, Long.class);
        this.registryKey = registryKey;
        this.expireAfter = expireAfter;
    }


    /**
     * 获取当前锁
     *
     * @param lockKey 锁id(对应业务唯一ID)
     * @return RedisLock
     */
    public Lock obtain(Object lockKey) {
        Assert.isInstanceOf(String.class, lockKey);
        String path = (String) lockKey;
        return this.locks.computeIfAbsent(path, RedisLock::new);
    }

    public String completeLockKey(String path) {
        Assert.notNull(path, "'path' cannot be null");
        return this.registryKey + ':' + path;
    }

    private final class RedisLock implements Lock {

        /**
         * Redis key
         */
        private final String lockKey;

        private final ReentrantLock localLock = new ReentrantLock();

        @Getter
        private volatile long lockedAt;

        private RedisLock(String path) {
            this.lockKey = completeLockKey(path);
        }


        @Override
        public void lock() {
            throw new UnsupportedOperationException("method not supported,you can use tryLock() to replace");
        }

        private void rethrowAsLockException(Exception e) {
            throw new CannotAcquireLockException(lockKey, "Failed to lock mutex at " + this.lockKey, e);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            this.localLock.lockInterruptibly();
            try {
                while (!obtainLock()) {
                    // 每100毫秒检测一次
                    Thread.sleep(100);
                }
            } catch (InterruptedException ie) {
                this.localLock.unlock();
                Thread.currentThread().interrupt();
                throw ie;
            } catch (Exception e) {
                this.localLock.unlock();
                rethrowAsLockException(e);
            }
        }

        @Override
        public boolean tryLock() {
            try {
                return tryLock(0, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        /**
         * 在指定时间内获取锁，超时则退出并返回获取失败
         *
         * @param time 最大等待获取锁时间
         * @param unit 等待时间单位
         * @return 是否获取成功
         * @throws InterruptedException 中断异常
         */
        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            long now = System.currentTimeMillis();
            if (!this.localLock.tryLock(time, unit)) {
                return false;
            }
            try {
                long expire = now + TimeUnit.MILLISECONDS.convert(time, unit);
                boolean acquired;
                while (!(acquired = obtainLock()) && System.currentTimeMillis() < expire) {
                    // 每100毫秒检测一次
                    Thread.sleep(100);
                }
                if (!acquired) {
                    this.localLock.unlock();
                }
                return acquired;
            } catch (Exception e) {
                this.localLock.unlock();
                rethrowAsLockException(e);
            }
            return false;
        }

        private boolean obtainLock() {
            Boolean success =
                    RedisLockRegistry.this.redisTemplate.execute(RedisLockRegistry.this.obtainLockScript,
                            Collections.singletonList(this.lockKey), RedisLockRegistry.this.clientId,
                            String.valueOf(RedisLockRegistry.this.expireAfter));

            boolean result = Boolean.TRUE.equals(success);

            if (result) {
                this.lockedAt = System.currentTimeMillis();
            }
            return result;
        }

        @Override
        public void unlock() {
            if (!this.localLock.isHeldByCurrentThread()) {
                throw new IllegalStateException("You do not own lock at " + this.lockKey);
            }
            if (this.localLock.getHoldCount() > 1) {
                // 说明线程重入
                this.localLock.unlock();
                return;
            }
            try {
                removeLockKey();
            } catch (Exception e) {
                ReflectionUtils.rethrowRuntimeException(e);
            } finally {
                this.localLock.unlock();
            }
        }

        private void removeLockKey() {
            Long result = RedisLockRegistry.this.redisTemplate.execute(RedisLockRegistry.this.deleteLockScript,
                    Collections.singletonList(this.lockKey), RedisLockRegistry.this.clientId);
            if (log.isDebugEnabled()) {
                if (Objects.equals(1L, result)) {
                    log.debug("Released lock; " + this);
                } else {
                    // 应该可以做个埋点，比如释放锁的时候发现Redis key已失效
                    log.debug("Released lock; " + this + "\tfailed,may be lock was released due to expire");
                }
            }
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Conditions are not supported");
        }


        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss.SSS");
            return "RedisLock [lockKey=" + this.lockKey
                    + ",lockedAt=" + dateFormat.format(new Date(this.lockedAt))
                    + ", clientId=" + RedisLockRegistry.this.clientId
                    + "]";
        }

    }
}
