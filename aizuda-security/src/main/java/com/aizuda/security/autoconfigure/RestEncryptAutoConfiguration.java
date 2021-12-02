/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.autoconfigure;

import com.aizuda.security.advice.DecryptRequestBodyAdvice;
import com.aizuda.security.advice.EncryptResponseBodyAdvice;
import com.aizuda.security.aspect.ParamSignAspect;
import com.aizuda.security.handler.DefaultRestEncryptHandler;
import com.aizuda.security.handler.IRestEncryptHandler;
import com.aizuda.security.handler.sgin.IParamsSignHandler;
import com.aizuda.security.handler.sgin.Md5SignHandler;
import com.aizuda.security.handler.sgin.ParamsSignHandler;
import com.aizuda.security.request.HttpServletRequestReplacedFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
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
public class RestEncryptAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultRestEncryptHandler restEncryptHandler() {
        return new DefaultRestEncryptHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptRequestBodyAdvice decryptRequestBodyAdvice(SecurityProperties securityProperties,
                                                             IRestEncryptHandler restEncryptHandler) {
        return new DecryptRequestBodyAdvice(securityProperties, restEncryptHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice(SecurityProperties securityProperties,
                                                               IRestEncryptHandler restEncryptHandler) {
        securityProperties.checkValid();
        return new EncryptResponseBodyAdvice(securityProperties, restEncryptHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "sign.invalidTime")
    public Md5SignHandler md5SignHandler() {
        return new Md5SignHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "sign.invalidTime")
    public ParamsSignHandler paramsSignHandler(Md5SignHandler md5SignHandler, SecurityProperties securityProperties) {
        return new ParamsSignHandler(md5SignHandler, securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "sign.invalidTime")
    public ParamSignAspect paramSignAspect(IParamsSignHandler paramsSignHandler) {
        return new ParamSignAspect(paramsSignHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "sign.invalidTime")
    public HttpServletRequestReplacedFilter customDispatcherServlet() {
        return new HttpServletRequestReplacedFilter();
    }
}
