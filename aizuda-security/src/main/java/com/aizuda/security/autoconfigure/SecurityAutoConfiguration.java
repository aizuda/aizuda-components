/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.security.autoconfigure;

import com.aizuda.security.advice.DecryptRequestBodyAdvice;
import com.aizuda.security.advice.EncryptResponseBodyAdvice;
import com.aizuda.security.handler.DefaultRestEncryptHandler;
import com.aizuda.security.handler.IRestEncryptHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 安全模块启动配置
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2021-12-08
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IRestEncryptHandler restEncryptHandler() {
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
        return new EncryptResponseBodyAdvice(securityProperties, restEncryptHandler);
    }
}
