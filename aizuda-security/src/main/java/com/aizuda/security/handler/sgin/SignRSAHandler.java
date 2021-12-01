/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler.sgin;

import com.aizuda.security.autoconfigure.SecurityProperties;
import com.baomidou.kisso.common.encrypt.RSA;
import com.baomidou.kisso.common.encrypt.base64.Base64;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * RSA验签实现
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-01
 */
@AllArgsConstructor
public class SignRSAHandler extends AbstractParamsSignHandler {

    private SecurityProperties securityProperty;

    @Override
    public boolean signGetRequest(HttpServletRequest request) throws Exception {
        if (!this.doBefore(request)) {
            return false;
        }
        String requestJsonStr = this.getRequestJsonStr(request);
        return this.sign(requestJsonStr).equals(this.getSign(request));
    }

    @Override
    public boolean signPostRequest(HttpServletRequest request) throws Exception {
        if (!this.doBefore(request)) {
            return false;
        }
        String requestJsonStr = this.postRequestJsonStr(request);
        return this.sign(requestJsonStr).equals(this.getSign(request));
    }

    @Override
    public String sign(String str) throws Exception {
        return Base64.toBase64String(RSA.encryptByPrivateKey(str.getBytes(StandardCharsets.UTF_8),
                securityProperty.getPrivateKey()));
    }

}
