/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.advice;

import com.aizuda.security.annotation.RestEncrypt;
import com.aizuda.security.autoconfigure.SecurityProperties;
import com.aizuda.security.handler.IRestEncryptHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;

/**
 * 接口请求加密处理切点
 *
 * @author hubin
 * @since 2021-11-08
 */
@Slf4j
@RestControllerAdvice
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {
    private SecurityProperties securityProperties;
    private IRestEncryptHandler restEncryptHandler;
    private boolean encrypt;

    public EncryptRequestBodyAdvice(SecurityProperties securityProperties, IRestEncryptHandler restEncryptHandler) {
        this.securityProperties = securityProperties;
        this.restEncryptHandler = restEncryptHandler;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        if (methodParameter.getMethod().isAnnotationPresent(RestEncrypt.class)) {
            encrypt = true;
        }
        return encrypt;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        return encrypt ? restEncryptHandler.request(securityProperties, inputMessage,
                parameter, targetType, converterType) : inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
