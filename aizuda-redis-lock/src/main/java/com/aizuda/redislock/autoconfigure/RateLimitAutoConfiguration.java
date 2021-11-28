/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.redislock.autoconfigure;

import com.aizuda.redislock.aspect.DistributedLimitAspect;
import com.aizuda.redislock.distributedlock.DistributedLockTemplate;
import com.aizuda.redislock.distributedlock.RedisDistributedLockTemplate;
import com.aizuda.redislock.extend.IDistributedLimitExtend;
import com.aizuda.redislock.handler.DistributedLimitHandler;
import com.aizuda.redislock.handler.IDistributedLimitHandler;
import com.aizuda.redislock.handler.RateLimitKeyParser;
import com.aizuda.redislock.strategy.IDistributedKeyBuilderStrategy;
import com.aizuda.redislock.toolkit.RedisLockRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.List;
import java.util.Optional;

/**
 * 速率限制配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-18
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RateLimitAutoConfiguration {

    @Bean
    public DistributedLimitAspect distributedLimitAspect(IDistributedLimitHandler distributedLimitHandler,
                                                         Optional<List<IDistributedLimitExtend>> distributedLimitExtendsOptional) {
        return new DistributedLimitAspect(distributedLimitHandler, distributedLimitExtendsOptional.orElse(null));
    }

    @Bean
    @ConditionalOnMissingBean
    public IDistributedLimitHandler distributedLimitHandler(RateLimitKeyParser rateLimitKeyParser,
                                                            Optional<List<IDistributedKeyBuilderStrategy>> distributedKeyBuilderStrategiesOptional,
                                                            DistributedLockTemplate distributedLockTemplate,
                                                            Optional<List<IDistributedLimitExtend>> distributedLimitExtendsOptional) {
        return new DistributedLimitHandler(rateLimitKeyParser, distributedKeyBuilderStrategiesOptional.orElse(null),
                distributedLockTemplate, distributedLimitExtendsOptional.orElse(null));
    }


    @Bean
    public DistributedLockTemplate distributedLockTemplate(RedisLockRegistry redisLockRegistry) {
        return new RedisDistributedLockTemplate(redisLockRegistry);
    }

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        // 设置失效时间为2分钟
        return new RedisLockRegistry(redisConnectionFactory, "aizuda-redis-lock", 120 * 1000);
    }


    @Bean
    public RateLimitKeyParser rateLimitKeyParser() {
        return new RateLimitKeyParser();
    }

}
