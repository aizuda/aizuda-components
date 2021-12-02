/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
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
 * @since 2021-12-01
 */
public class ParamsSignHandler extends AbstractParamsSignHandler {

    private ISignHandler signHandler;

    public ParamsSignHandler(ISignHandler signHandler, SecurityProperties properties) {
        super(ParamsSignHelper.invalidTime(properties.getSign().getInvalidTime()));
        this.signHandler = signHandler;
    }

    @Override
    public boolean signGetRequest(HttpServletRequest request) {
        if (!this.doBefore(request)) return false;
        String requestJsonStr = this.getRequestJsonStr(request);
        System.out.println(signHandler.sign(requestJsonStr));
        return signHandler.sign(requestJsonStr).equals(this.getSign(request));
    }

    @Override
    public boolean signPostRequest(HttpServletRequest request) throws IOException {
        if (!this.doBefore(request)) return false;
        String requestJsonStr = this.postRequestJsonStr(request);
        return signHandler.sign(requestJsonStr).equals(this.getSign(request));
    }
}
