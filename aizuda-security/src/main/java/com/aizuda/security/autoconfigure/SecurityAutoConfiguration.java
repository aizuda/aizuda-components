package com.aizuda.security.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityAutoConfiguration {

}
