package com.aizuda.security.toolkit;

import com.aizuda.security.annotation.RestEncrypt;

import java.lang.reflect.Method;

/**
 * 接口加密辅助类
 *
 * @author hubin
 * @since 2021-11-08
 */
public class RestEncryptHelper {

    /**
     * 判断方法是否存在加密注解
     *
     * @param method 控制器方法
     * @return
     */
    public static boolean isAnnotationEncrypt(Method method) {
        return method.isAnnotationPresent(RestEncrypt.class);
    }
}
