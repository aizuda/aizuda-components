/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.autoconfigure;

import com.aizuda.limiter.aspect.DistributedLockAspect;
import com.aizuda.limiter.aspect.RateLimitAspect;
import com.aizuda.limiter.handler.*;
import com.aizuda.limiter.strategy.IRateLimitStrategy;
import com.aizuda.limiter.strategy.IpRateLimitStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 速率限制配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-18
 */
@Configuration
@EnableConfigurationProperties({LimiterProperties.class})
public class LimiterAutoConfiguration {

    @Bean
    public IpRateLimitStrategy ipRateLimitStrategy() {
        return new IpRateLimitStrategy();
    }

    @Bean
    public RateLimitKeyParser rateLimitKeyParser(List<IRateLimitStrategy> rateLimitStrategyList) {
        return new RateLimitKeyParser(rateLimitStrategyList);
    }


    /**  -------------------- 限流相关配置  --------------------  */

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


    /**  -------------------- 分布式锁相关配置  --------------------  */

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableDistributedLock", havingValue = "true")
    public IDistributedLockHandler distributedLockHandler(ObjectProvider<RedisTemplate<String, String>> redisTemplate,
                                              RateLimitKeyParser rateLimitKeyParser) {
        return new RedisDistributedLockHandler(redisTemplate.getIfAvailable(), rateLimitKeyParser);
    }

    @Bean
    @ConditionalOnProperty(prefix = LimiterProperties.PREFIX, name = "enableDistributedLock", havingValue = "true")
    public DistributedLockAspect distributedLockAspect(IDistributedLockHandler distributedLockHandler) {
        return new DistributedLockAspect(distributedLockHandler);
    }
}
