/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.time.Duration;

/**
 * 爱组搭安全配置
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
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

    /**
     * 分布式锁的前缀名称：默认为'aizuda-redis-lock'
     */
    private String distributedRootKey;

    /**
     * RedisLock的key失效时间
     * 默认2分钟
     * <p>
     * 例如 5s 五秒，6m 六分钟，7h 七小时，8d 八天
     */
    private Duration expireAfter;

    public boolean isEnable() {
        return this.enableRateLimit || this.enableDistributedLock;
    }
}
