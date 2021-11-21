package com.aizuda.exception.autoconfigure;

import com.aizuda.exception.advice.ExceptionHandlerAdvice;
import com.aizuda.exception.handler.IExceptionHandler;
import com.aizuda.exception.handler.WechatExceptionHandler;
import com.aizuda.exception.strategy.IPushExceptionStrategy;
import com.aizuda.exception.strategy.WeChatPushExceptionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 0:45
 * @description believe in yourself
 * 异常处理的自动配置类
 */
@Configuration
@EnableConfigurationProperties({WechatPushProperties.class})
public class ExceptionAutoConfiguration {

    @Autowired
    WechatPushProperties wechatPushProperties;

    @Bean
    public ExceptionHandlerAdvice exceptionHandlerAdvice(List<IExceptionHandler> handlers) {
        return new ExceptionHandlerAdvice(handlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public IExceptionHandler defaultExceptionHandler(IPushExceptionStrategy iPushExceptionStrategy) {
        return new WechatExceptionHandler(iPushExceptionStrategy);
    }

    @Bean
    @ConditionalOnMissingBean
    public IPushExceptionStrategy defaultPushExceptionStrategy() {
        return new WeChatPushExceptionStrategy(wechatPushProperties);
    }
}
