/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.annotation;

import com.aizuda.limiter.metadata.MethodMetadata;
import com.aizuda.limiter.strategy.DefaultKeyGenerateStrategy;
import com.aizuda.limiter.strategy.IKeyGenerateStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁限制注解类
 * <p>
 * 支持可重入
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua 青苗
 * @since 2021-11-28
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 唯一标示，支持SpEL表达式
     */
    String key() default "";

    /**
     * 获取分布式锁超时失败时间，默认 10 秒
     * <p>
     * 例如 5s 五秒，6m 六分钟，7h 七小时，8d 八天
     */
    String tryAcquireTimeout() default "10s";

    /**
     * 加key策略
     * 需要实现{@link IKeyGenerateStrategy}接口注入到spring中
     * 通过{@link IKeyGenerateStrategy#getKey(MethodMetadata, String)}返回的key将会作为后缀加在默认得到的key后
     */
    String[] strategy() default {};

    /**
     * 获取分布式锁超时提示
     */
    String acquireTimeoutMessage() default "";

    /**
     * 是否使用默认的key生成策略{@link DefaultKeyGenerateStrategy}作为前缀
     */
    boolean useDefaultStrategy() default true;
}
