/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import com.aizuda.security.autoconfigure.SecurityProperties;
import com.aizuda.security.exception.DecryptRequestException;
import com.baomidou.kisso.common.encrypt.RSA;
import com.baomidou.kisso.common.encrypt.base64.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 接口加解密处理抽象类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-08
 */
@Slf4j
public abstract class AbstractRestEncryptHandler implements IRestEncryptHandler {

    @Override
    public HttpInputMessage request(SecurityProperties props, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                    Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            /**
             * 请求数据 RSA 公钥解密
             */
            String publicKey = this.getPublicKey(props, inputMessage);
            DecryptRequestException.isEmpty(publicKey, "not found rsa publicKey");
            log.debug("request publicKey={}", publicKey);
            String content = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
            byte[] decryptBytes = RSA.decryptByPublicKey(Base64.decode(content.getBytes(StandardCharsets.UTF_8)), publicKey);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptBytes);
            return new MappingJacksonInputMessage(inputStream, inputMessage.getHeaders());
        } catch (Exception e) {
            return requestException(e);
        }
    }

    /**
     * 公钥
     */
    public String getPublicKey(SecurityProperties props, HttpInputMessage inputMessage) {
        return props.getPublicKey();
    }

    /**
     * 预留子类处理抛出请求解密异常逻辑
     *
     * @param e {@link Exception}
     * @return
     */
    public HttpInputMessage requestException(Exception e) {
        throw new DecryptRequestException("Decrypt request error", e);
    }

    @Override
    public Object response(SecurityProperties props, Object body, MethodParameter returnType, MediaType selectedContentType,
                           Class<? extends HttpMessageConverter<?>> selectedConverterType,
                           ServerHttpRequest request, ServerHttpResponse response) {
        try {
            /**
             * 返回 base64 加密后的 RSA 密文内容
             */
            String privateKey = this.getPrivateKey(props, request);
            DecryptRequestException.isEmpty(privateKey, "not found rsa privateKey");
            log.debug("response privateKey={}", privateKey);
            byte[] plaintextBytes = this.toJson(body).getBytes(StandardCharsets.UTF_8);
            return Base64.toBase64String(RSA.encryptByPrivateKey(plaintextBytes, privateKey));
        } catch (Exception e) {
            return responseException(e);
        }
    }

    /**
     * 私钥
     */
    public String getPrivateKey(SecurityProperties props, ServerHttpRequest request) {
        return props.getPrivateKey();
    }

    /**
     * 响应解密异常逻辑
     *
     * @param e {@link Exception}
     * @return
     */
    public Object responseException(Exception e) {
        throw new DecryptRequestException("Decrypt response error", e);
    }

    /**
     * 对象转 json 字符串方法
     *
     * @param body 请求对象
     * @return 对象 json 格式化字符串
     */
    public abstract String toJson(Object body);
}
