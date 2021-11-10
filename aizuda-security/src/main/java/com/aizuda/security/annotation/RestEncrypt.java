/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.annotation;

import java.lang.annotation.*;

/**
 * 接口加密注解类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestEncrypt {

    /**
     * 加密响应
     *
     * @return 默认 true 设置 false 响应请求对象不加密
     */
    boolean encrypt() default true;

    /**
     * 解密参数
     *
     * @return 默认 true 设置 false 请求参数不解密
     */
    boolean decrypt() default true;

}
