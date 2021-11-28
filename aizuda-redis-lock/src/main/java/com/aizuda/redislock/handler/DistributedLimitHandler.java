package com.aizuda.redislock.handler;

import com.aizuda.redislock.annotation.DistributedLimit;
import com.aizuda.redislock.distributedlock.DistributedLockCallback;
import com.aizuda.redislock.distributedlock.DistributedLockTemplate;
import com.aizuda.redislock.exception.DistributedLimitException;
import com.aizuda.redislock.extend.IDistributedLimitExtend;
import com.aizuda.redislock.strategy.IDistributedKeyBuilderStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationStyle;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁速率限制处理器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Slf4j
@RequiredArgsConstructor
public class DistributedLimitHandler implements IDistributedLimitHandler {
    private final RateLimitKeyParser rateLimitKeyParser;
    private final List<IDistributedKeyBuilderStrategy> distributedKeyBuilderStrategies;
    private final DistributedLockTemplate distributedLockTemplate;
    private final List<IDistributedLimitExtend> distributedLimitExtends;

    @Override
    public Object proceed(Method method, Supplier<Object[]> args, String classMethodName,
                          DistributedLimit distributedLimit, DistributedLockCallback callback) {
        // 间隔时间解析为秒
        long expire = DurationStyle.detectAndParse(distributedLimit.expire()).getSeconds();
        if (expire > Integer.MAX_VALUE) {
            throw new DistributedLimitException(String.format("the expire value[%s] you set in @DistributedLimit too large", distributedLimit.expire()));
        }
        final String key = this.buildKey(method, args, classMethodName, distributedLimit);
        if (log.isDebugEnabled()) {
            log.debug("start distributed lock[key:{},expire:{} S]", key, expire);
        }
        Optional.ofNullable(distributedLimitExtends)
                .ifPresent(distributedLimitExtends ->
                        distributedLimitExtends.forEach(distributedLimitExtend ->
                                // 在执行分布式锁之前需要执行的方法
                                distributedLimitExtend.beforeDistributedLock(method, args, classMethodName, distributedLimit, key)));
        return distributedLockTemplate.execute(key, (int) expire, TimeUnit.SECONDS, callback);
    }

    /**
     * 构建唯一标示 KEY
     */
    private String buildKey(Method method, Supplier<Object[]> args, String classMethodName, DistributedLimit distributedLimit) {
        StringBuilder key = new StringBuilder();
        key.append(classMethodName).append(":");
        // SpEL Key 解析
        final String limitKey = distributedLimit.key();
        String parserKey = Optional.of(limitKey)
                .filter(str -> !"".equals(limitKey))
                .map(str -> rateLimitKeyParser.parser(limitKey, method, args.get()))
                .orElse("");

        if (null != distributedKeyBuilderStrategies && !distributedKeyBuilderStrategies.isEmpty()) {
            // 先看是否有自定义key生成策略，否则使用默认策略
            String parserKeyCopy = parserKey;
            parserKey = distributedKeyBuilderStrategies.stream()
                    .filter(distributedKeyBuilderStrategy ->
                            distributedKeyBuilderStrategy.support(method, classMethodName, distributedLimit, parserKeyCopy))
                    .findAny()
                    .map(distributedKeyBuilderStrategy ->
                            distributedKeyBuilderStrategy.buildKey(method, classMethodName, distributedLimit, parserKeyCopy))
                    .orElse(parserKey);
        }
        if (!"".equals(parserKey)) {
            key.append(parserKey);
        }

        return key.toString();
    }
}
