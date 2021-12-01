package com.aizuda.security.handler.sgin;

import com.baomidou.kisso.common.encrypt.MD5;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * MD5验签实现
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-1
 */
public class SignMd5Handler extends AbstractParamsSignHandler {

    @Override
    public boolean signGetRequest(HttpServletRequest request) {
        if (!this.doBefore(request)) {
            return false;
        }
        String requestJsonStr = this.getRequestJsonStr(request);
        return this.sign(requestJsonStr).equals(this.getSign(request));
    }

    @Override
    public boolean signPostRequest(HttpServletRequest request) throws IOException {
        if (!this.doBefore(request)) {
            return false;
        }
        String requestJsonStr = this.postRequestJsonStr(request);
        return this.sign(requestJsonStr).equals(this.getSign(request));
    }

    @Override
    public String sign(String str) {
        return MD5.toMD5(str);
    }
}
