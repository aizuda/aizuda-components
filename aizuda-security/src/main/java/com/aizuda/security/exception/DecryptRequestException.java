/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.exception;

/**
 * 请求参数解密签名异常
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-12-08
 */
public class DecryptRequestException extends RuntimeException {

    public DecryptRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
