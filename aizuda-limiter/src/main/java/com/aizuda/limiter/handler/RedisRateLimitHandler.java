/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import com.aizuda.limiter.annotation.RateLimit;
import com.aizuda.limiter.metadata.MethodMetadata;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

/**
 * Redis 速率限制处理器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-16
 */
@Slf4j
@AllArgsConstructor
public class RedisRateLimitHandler implements IRateLimitHandler {
    private static RedisScript<Long> REDIS_SCRIPT_RATE_LIMIT = null;
    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitKeyParser rateLimitKeyParser;

    @Override
    public boolean proceed(MethodMetadata methodMetadata) {
        RateLimit rateLimit = methodMetadata.getAnnotation();
        if (null == REDIS_SCRIPT_RATE_LIMIT) {
            REDIS_SCRIPT_RATE_LIMIT = RedisScript.of(this.luaScript(), Long.class);
        }
        // 间隔时间解析为秒
        long interval = DurationStyle.detectAndParse(rateLimit.interval()).getSeconds();
        final String key = rateLimitKeyParser.buildKey(methodMetadata, rateLimit.key(), rateLimit.strategy(), true);
        if (log.isDebugEnabled()) {
            log.debug("rate.limit.key = {}", key);
        }
        Long currentCount = redisTemplate.execute(REDIS_SCRIPT_RATE_LIMIT, Collections.singletonList(key),
                String.valueOf(rateLimit.count()), String.valueOf(interval));
        if (null != currentCount) {
            long count = currentCount;
            if (count > 0 && count <= rateLimit.count()) {
                if (log.isDebugEnabled()) {
                    log.debug("The {}-th visit within the current limit period", count);
                }
                return true;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("current limiting rule triggered");
        }
        return false;
    }


    /**
     * Lua 限流脚本
     */
    public String luaScript() {
        return "local key = KEYS[1];" +
                "local count = tonumber(ARGV[1]);" +
                "local interval = tonumber(ARGV[2]);" +
                "local current = tonumber(redis.call('get', key) or \"0\") " +
                "if current + 1 > count then return 0 " +
                "else redis.call(\"INCRBY\", key, \"1\") redis.call(\"expire\", key, interval) return current + 1 end";
    }
}
