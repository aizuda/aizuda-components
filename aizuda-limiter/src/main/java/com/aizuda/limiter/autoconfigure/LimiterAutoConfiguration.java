/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.autoconfigure;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.limiter.aspect.DistributedLockAspect;
import com.aizuda.limiter.aspect.RateLimitAspect;
import com.aizuda.limiter.context.DefaultDistributedContext;
import com.aizuda.limiter.context.DistributedContext;
import com.aizuda.limiter.distributedlock.IDistributedLockTemplate;
import com.aizuda.limiter.distributedlock.RedisDistributedLockTemplate;
import com.aizuda.limiter.extend.IAcquireLockTimeoutHandler;
import com.aizuda.limiter.extend.IDistributedLockListener;
import com.aizuda.limiter.handler.*;
import com.aizuda.limiter.strategy.IKeyGenerateStrategy;
import com.aizuda.limiter.strategy.IpKeyGenerateStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * 速率限制配置
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2021-11-18
 */
@Configuration
@EnableConfigurationProperties({LimiterProperties.class})
public class LimiterAutoConfiguration {

    @Bean
    public IpKeyGenerateStrategy ipRateLimitStrategy() {
        return new IpKeyGenerateStrategy();
    }

    @Bean
    public RateLimitKeyParser rateLimitKeyParser(List<IKeyGenerateStrategy> rateLimitStrategyList) {
        return new RateLimitKeyParser(rateLimitStrategyList);
    }


    /*  -------------------- 限流相关配置  --------------------  */

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableRateLimit", havingValue = "true")
    public IRateLimitHandler rateLimitHandler(ObjectProvider<RedisTemplate<String, String>> redisTemplate,
                                              RateLimitKeyParser rateLimitKeyParser) {
        return new RedisRateLimitHandler(redisTemplate.getIfAvailable(), rateLimitKeyParser);
    }

    @Bean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableRateLimit", havingValue = "true")
    public RateLimitAspect rateLimitAspect(IRateLimitHandler rateLimitHandler) {
        return new RateLimitAspect(rateLimitHandler);
    }


    /*  -------------------- 分布式锁相关配置  --------------------  */

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableDistributedLock", havingValue = "true")
    public IDistributedLockHandler distributedLockHandler(DistributedContext distributedContext,
                                                          RateLimitKeyParser rateLimitKeyParser) {
        return new RedisDistributedLockHandler(rateLimitKeyParser, distributedContext);
    }

    @Bean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableDistributedLock", havingValue = "true")
    public DistributedLockAspect distributedLockAspect(IDistributedLockHandler distributedLockHandler) {
        return new DistributedLockAspect(distributedLockHandler);
    }

    @Bean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableDistributedLock", havingValue = "true")
    public DistributedContext distributedContext(IDistributedLockTemplate distributedLockTemplate,
                                                 Optional<List<IAcquireLockTimeoutHandler>> acquireLockTimeoutHandlersOptional,
                                                 Optional<List<IDistributedLockListener>> distributedLimitListenersOptional) {
        return new DefaultDistributedContext(distributedLockTemplate, acquireLockTimeoutHandlersOptional.orElse(null),
                distributedLimitListenersOptional.orElse(null));
    }


    @Bean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableDistributedLock", havingValue = "true")
    @ConditionalOnMissingBean(IDistributedLockTemplate.class)
    public IDistributedLockTemplate iDistributedLockTemplate(LimiterProperties limiterProperties,
                                                             ObjectProvider<RedisTemplate<String, String>> redisTemplate) {
        String rootKey = Optional.ofNullable(limiterProperties.getDistributedRootKey())
                .filter(StringUtils::hasLength)
                .orElse("aizuda-redis-lock");
        Duration expireAfter = Optional.ofNullable(limiterProperties.getExpireAfter())
                .orElse(Duration.ofMinutes(2));
        return new RedisDistributedLockTemplate(redisTemplate.getIfAvailable(), rootKey, expireAfter.toMillis());
    }

}
