/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.autoconfigure;

import com.aizuda.security.advice.DecryptRequestBodyAdvice;
import com.aizuda.security.advice.EncryptResponseBodyAdvice;
import com.aizuda.security.handler.DefaultRestEncryptHandler;
import com.aizuda.security.handler.IRestEncryptHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 接口加密处理启动配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-08
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = SecurityProperties.PREFIX, name = "privateKey")
public class EncryptAutoConfiguration {

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
}
