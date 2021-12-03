/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 爱组搭安全配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-08
 */
@Getter
@Setter
@ConfigurationProperties(prefix = LimiterProperties.PREFIX)
public class LimiterProperties implements Serializable {

    /**
     * 配置前缀
     */
    public static final String PREFIX = "aizuda.limiter";

    /**
     * 开启限流
     */
    private boolean enableRateLimit;

    /**
     * 开启分布式锁
     */
    private boolean enableDistributedLock;

    public boolean isEnable() {
        return this.enableRateLimit || this.enableDistributedLock;
    }
}
