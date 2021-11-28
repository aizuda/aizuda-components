package com.aizuda.limiter.toolkit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis分布式锁实例
 * 参考自Redission，感谢Redission
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Slf4j
public class RedisLockRegistry implements DisposableBean {

    /**
     * 默认失效时间，60s
     */
    private static final long DEFAULT_EXPIRE_AFTER = 60000L;

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
     * 每一个key对应一个锁，增强并发
     */
    private final Map<String, RedisLock> locks = new ConcurrentHashMap<>();

    private final String clientId = UUID.randomUUID().toString();

    private final String registryKey;

    private final StringRedisTemplate redisTemplate;

    private final RedisScript<Boolean> obtainLockScript;

    private final long expireAfter;

    /**
     * {@link RedisLock#unlock()}
     * 清除redis 锁的时候使用
     */
    private Executor executor =
            Executors.newCachedThreadPool(new CustomizableThreadFactory("redis-lock-registry-"));

    /**
     * 是否回收{@link RedisLockRegistry#executor}的标记
     * 当使用{@link RedisLockRegistry#setExecutor(Executor)}指定线程池时，不进行回收
     */
    private boolean executorExplicitlySet;


    private volatile boolean unlinkAvailable = true;

    public RedisLockRegistry(RedisConnectionFactory connectionFactory, String registryKey) {
        this(connectionFactory, registryKey, DEFAULT_EXPIRE_AFTER);
    }

    public RedisLockRegistry(RedisConnectionFactory connectionFactory, String registryKey, long expireAfter) {
        Assert.notNull(connectionFactory, "'connectionFactory' cannot be null");
        Assert.notNull(registryKey, "'registryKey' cannot be null");
        this.redisTemplate = new StringRedisTemplate(connectionFactory);
        this.obtainLockScript = new DefaultRedisScript<>(OBTAIN_LOCK_SCRIPT, Boolean.class);
        this.registryKey = registryKey;
        this.expireAfter = expireAfter;
    }


    public void setExecutor(Executor executor) {
        this.executor = executor;
        // 标记不再进行回收线程池
        this.executorExplicitlySet = true;
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

    public void expireUnusedOlderThan(long age) {
        long now = System.currentTimeMillis();
        this.locks.entrySet()
                .removeIf((entry) -> {
                    RedisLock lock = entry.getValue();
                    return now - lock.getLockedAt() > age && !lock.isAcquiredInThisProcess();
                });
    }

    @Override
    public void destroy() {
        if (!this.executorExplicitlySet) {
            ((ExecutorService) this.executor).shutdown();
        }
    }

    private final class RedisLock implements Lock {

        private final String lockKey;

        private final ReentrantLock localLock = new ReentrantLock();

        private volatile long lockedAt;

        private RedisLock(String path) {
            this.lockKey = constructLockKey(path);
        }

        private String constructLockKey(String path) {
            return RedisLockRegistry.this.registryKey + ':' + path;
        }

        public long getLockedAt() {
            return this.lockedAt;
        }

        /**
         * 长时间获取锁，忽略中断，有可能会非常耗时
         * 建议使用{@link RedisLock#tryLock(long, TimeUnit)}
         */
        @Override
        public void lock() {
            this.localLock.lock();
            while (true) {
                try {
                    while (!obtainLock()) {
                        // 每100毫秒检测一次
                        Thread.sleep(100);
                    }
                    break;
                } catch (InterruptedException e) {
                    /*
                     * 这里必须是不可中断的，
                     * 只有在获得锁时才跳出 while 循环。
                     */
                } catch (Exception e) {
                    this.localLock.unlock();
                    rethrowAsLockException(e);
                }
            }
        }

        private void rethrowAsLockException(Exception e) {
            throw new CannotAcquireLockException("Failed to lock mutex at " + this.lockKey, e);
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
                this.localLock.unlock();
                return;
            }
            try {
                if (!isAcquiredInThisProcess()) {
                    throw new IllegalStateException("Lock was released in the store due to expiration. " +
                            "The integrity of data protected by this lock may have been compromised.");
                }

                if (Thread.currentThread().isInterrupted()) {
                    RedisLockRegistry.this.executor.execute(this::removeLockKey);
                } else {
                    removeLockKey();
                }

                if (log.isDebugEnabled()) {
                    log.debug("Released lock; " + this);
                }
            } catch (Exception e) {
                ReflectionUtils.rethrowRuntimeException(e);
            } finally {
                this.localLock.unlock();
            }
        }

        private void removeLockKey() {
            if (RedisLockRegistry.this.unlinkAvailable) {
                try {
                    RedisLockRegistry.this.redisTemplate.unlink(this.lockKey);
                } catch (Exception ex) {
                    RedisLockRegistry.this.unlinkAvailable = false;
                    if (log.isDebugEnabled()) {
                        log.debug("The UNLINK command has failed (not supported on the Redis server?); " +
                                "falling back to the regular DELETE command", ex);
                    } else {
                        log.warn("The UNLINK command has failed (not supported on the Redis server?); " +
                                "falling back to the regular DELETE command: " + ex.getMessage());
                    }
                    RedisLockRegistry.this.redisTemplate.delete(this.lockKey);
                }
            } else {
                RedisLockRegistry.this.redisTemplate.delete(this.lockKey);
            }
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Conditions are not supported");
        }

        public boolean isAcquiredInThisProcess() {
            return RedisLockRegistry.this.clientId.equals(
                    RedisLockRegistry.this.redisTemplate.boundValueOps(this.lockKey).get());
        }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss.SSS");
            return "RedisLock [lockKey=" + this.lockKey
                    + ",lockedAt=" + dateFormat.format(new Date(this.lockedAt))
                    + ", clientId=" + RedisLockRegistry.this.clientId
                    + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((this.lockKey == null) ? 0 : this.lockKey.hashCode());
            result = prime * result + (int) (this.lockedAt ^ (this.lockedAt >>> 32)); // NOSONAR magic number
            result = prime * result + RedisLockRegistry.this.clientId.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RedisLock other = (RedisLock) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (!this.lockKey.equals(other.lockKey)) {
                return false;
            }
            return this.lockedAt == other.lockedAt;
        }

        private RedisLockRegistry getOuterType() {
            return RedisLockRegistry.this;
        }

    }
}
