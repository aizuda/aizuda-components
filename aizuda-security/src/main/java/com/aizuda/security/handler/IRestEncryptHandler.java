/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import com.aizuda.security.autoconfigure.SecurityProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.lang.reflect.Type;

/**
 * 接口加密处理类
 *
 * @author hubin
 * @since 2021-11-08
 */
public interface IRestEncryptHandler {

    HttpInputMessage request(SecurityProperties props, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                             Class<? extends HttpMessageConverter<?>> converterType);

    Object response(SecurityProperties props, Object body, MethodParameter returnType, MediaType selectedContentType,
                    Class<? extends HttpMessageConverter<?>> selectedConverterType,
                    ServerHttpRequest request, ServerHttpResponse response);

}
