/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.advice;

import com.aizuda.security.toolkit.RestEncryptHelper;
import com.aizuda.security.autoconfigure.SecurityProperties;
import com.aizuda.security.handler.IRestEncryptHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 接口响应加密处理切点
 *
 * @author hubin
 * @since 2021-11-08
 */
@Slf4j
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private SecurityProperties securityProperties;
    private IRestEncryptHandler restEncryptHandler;
    private boolean encrypt;

    public EncryptResponseBodyAdvice(SecurityProperties securityProperties, IRestEncryptHandler restEncryptHandler) {
        this.securityProperties = securityProperties;
        this.restEncryptHandler = restEncryptHandler;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        this.encrypt = RestEncryptHelper.isAnnotationEncrypt(returnType.getMethod());
        return this.encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return this.encrypt ? restEncryptHandler.response(securityProperties, body, returnType,
                selectedContentType, selectedConverterType, request, response) : body;
    }
}
