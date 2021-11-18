/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.autoconfigure;

import com.aizuda.limiter.aspect.RateLimitAspect;
import com.aizuda.limiter.handler.IRateLimitHandler;
import com.aizuda.limiter.handler.RateLimitKeyParser;
import com.aizuda.limiter.handler.RedisRateLimitHandler;
import com.aizuda.limiter.strategy.IRateLimitStrategy;
import com.aizuda.limiter.strategy.IpRateLimitStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
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
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RateLimitAutoConfiguration {

    @Bean
    public IpRateLimitStrategy ipRateLimitStrategy() {
        return new IpRateLimitStrategy();
    }

    @Bean
    public RateLimitKeyParser rateLimitKeyParser() {
        return new RateLimitKeyParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public IRateLimitHandler rateLimitHandler(ObjectProvider<RedisTemplate<String, String>> redisTemplate,
                                              List<IRateLimitStrategy> rateLimitStrategyList,
                                              RateLimitKeyParser rateLimitKeyParser) {
        return new RedisRateLimitHandler(redisTemplate.getIfAvailable(), rateLimitStrategyList, rateLimitKeyParser);
    }

    @Bean
    public RateLimitAspect rateLimitAspect(IRateLimitHandler rateLimitHandler) {
        return new RateLimitAspect(rateLimitHandler);
    }
}
