/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.security.advice;

import com.aizuda.security.annotation.Encrypted;
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
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2021-11-08
 */
@Slf4j
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final SecurityProperties securityProperties;
    private final IRestEncryptHandler restEncryptHandler;

    public EncryptResponseBodyAdvice(SecurityProperties securityProperties, IRestEncryptHandler restEncryptHandler) {
        this.securityProperties = securityProperties;
        this.restEncryptHandler = restEncryptHandler;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Encrypted.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return restEncryptHandler.response(securityProperties, body, returnType, selectedContentType,
                selectedConverterType, request, response);
    }
}
