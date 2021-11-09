/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import com.aizuda.security.autoconfigure.SecurityProperties;
import com.baomidou.kisso.common.encrypt.RSA;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * 接口加密处理抽象类
 *
 * @author hubin
 * @since 2021-11-08
 */
public abstract class AbstractRestEncryptHandler implements IRestEncryptHandler {

    @Override
    public HttpInputMessage request(SecurityProperties props, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                    Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            return new EncryptHttpInputMessage(props, inputMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputMessage;
    }

    @Override
    public Object response(SecurityProperties props, Object body, MethodParameter returnType, MediaType selectedContentType,
                           Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            /**
             * 返回 base64 加密后的 rsa 密文内容
             */
            return encryptByPrivateKey(props.getPrivateKey(), toJson(body));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class EncryptHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;
        private InputStream body;

        public EncryptHttpInputMessage(SecurityProperties props, HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            String content = new BufferedReader(new InputStreamReader(inputMessage.getBody()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            // content 待处理
            this.body = new ByteArrayInputStream(content.getBytes());
        }

        @Override
        public InputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

    /**
     * 私钥加密
     *
     * @param plaintext  明文
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public String encryptByPrivateKey(String plaintext, String privateKey) throws Exception {
        return Base64.getEncoder().encodeToString(RSA.encryptByPrivateKey(plaintext.getBytes(StandardCharsets.UTF_8), privateKey));
    }

    /**
     * 对象转 json 字符串方法
     *
     * @param body 请求对象
     * @return
     */
    public abstract String toJson(Object body);
}
