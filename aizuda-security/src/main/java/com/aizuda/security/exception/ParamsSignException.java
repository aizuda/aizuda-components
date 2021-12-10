/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.exception;

/**
 * 请求参数签名异常
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-12-03
 */
public class ParamsSignException extends RuntimeException {

    public ParamsSignException(String message) {
        super(message);
    }
}
