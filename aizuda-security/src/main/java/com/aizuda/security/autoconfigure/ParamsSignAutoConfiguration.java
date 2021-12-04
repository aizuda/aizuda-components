/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.autoconfigure;

import com.aizuda.security.aspect.ParamSignAspect;
import com.aizuda.security.handler.IParamsSignHandler;
import com.aizuda.security.handler.Md5ParamsSignHandler;
import com.aizuda.security.request.SignRequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 接口加密处理启动配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-08
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SecurityProperties.class})
public class ParamsSignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Md5ParamsSignHandler md5ParamsSignHandler(SecurityProperties securityProperties) {
        return new Md5ParamsSignHandler(securityProperties);
    }

    @Bean
    public ParamSignAspect paramSignAspect(IParamsSignHandler paramsSignHandler) {
        return new ParamSignAspect(paramsSignHandler);
    }

    @Bean
    public SignRequestFilter signRequestFilter() {
        return new SignRequestFilter();
    }
}
