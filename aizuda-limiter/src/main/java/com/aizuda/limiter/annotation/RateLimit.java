/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 速率限制注解类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 唯一标示，支持SpEL表达式
     */
    String key() default "";

    /**
     * 限定阈值
     * <p>
     * 时间间隔 interval 范围内超过该数量会触发锁
     */
    long count();

    /**
     * 时间间隔，默认 3 分钟
     * <p>
     * 例如 5s 五秒，6m 六分钟，7h 七小时，8d 八天
     */
    String interval() default "3m";

    /**
     * 限制策略
     */
    String[] strategy() default {};

    /**
     * 提示消息，非必须
     */
    String message() default "";
}
