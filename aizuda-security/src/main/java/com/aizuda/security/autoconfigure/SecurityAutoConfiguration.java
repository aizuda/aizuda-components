/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * 安全模块启动配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-12-08
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SecurityProperties.class})
@Import(value = {EncryptAutoConfiguration.class})
public class SecurityAutoConfiguration {

}
