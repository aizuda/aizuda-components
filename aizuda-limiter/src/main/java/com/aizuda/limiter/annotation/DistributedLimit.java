package com.aizuda.limiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁限制注解类
 * 支持可重入
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-11-28
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DistributedLimit {
    /**
     * 唯一标示，支持SpEL表达式
     */
    String key() default "";

    /**
     * 当前分布式锁失效时间，默认 10 秒
     * <p>
     * 例如 5s 五秒，6m 六分钟，7h 七小时，8d 八天
     */
    String expire() default "10s";

    /**
     * 提示消息，非必须
     */
    String message() default "";
}
