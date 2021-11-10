/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.toolkit;

import com.aizuda.security.annotation.RestEncrypt;

import java.lang.reflect.Method;

/**
 * 接口加密辅助类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-08
 */
public class RestEncryptHelper {

    /**
     * 判断方法是否注解加密请求响应数据
     *
     * @param method 控制器方法
     * @return
     */
    public static boolean isAnnotationEncrypt(Method method) {
        RestEncrypt restEncrypt = getRestEncrypt(method);
        return null == restEncrypt ? false : restEncrypt.encrypt();
    }

    /**
     * 判断方法是否注解解密请求 json 参数
     *
     * @param method 控制器方法
     * @return
     */
    public static boolean isAnnotationDecrypt(Method method) {
        RestEncrypt restEncrypt = getRestEncrypt(method);
        return null == restEncrypt ? false : restEncrypt.decrypt();
    }

    private static RestEncrypt getRestEncrypt(Method method) {
        return method.isAnnotationPresent(RestEncrypt.class) ?
                method.getAnnotation(RestEncrypt.class) : null;
    }
}
