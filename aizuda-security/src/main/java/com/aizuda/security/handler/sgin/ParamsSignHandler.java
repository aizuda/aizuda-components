package com.aizuda.security.handler.sgin;

import com.aizuda.security.autoconfigure.SecurityProperties;
import com.baomidou.kisso.common.util.StringUtils;
import lombok.AllArgsConstructor;

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
@AllArgsConstructor
public class ParamsSignHandler extends AbstractParamsSignHandler {

    private ISignHandler signHandler;

    private SecurityProperties.Sign sign;

    @Override
    public boolean signGetRequest(HttpServletRequest request) {
        if (!this.doBefore(request,sign.getInvalidTime())) return false;
        String requestJsonStr = this.getRequestJsonStr(request);
        return signHandler.sign(requestJsonStr).equals(this.getSign(request));
    }

    @Override
    public boolean signPostRequest(HttpServletRequest request) throws IOException {
        if (!this.doBefore(request,sign.getInvalidTime())) return false;
        String requestJsonStr = this.postRequestJsonStr(request);
        return signHandler.sign(requestJsonStr).equals(this.getSign(request));
    }


}
