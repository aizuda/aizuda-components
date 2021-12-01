/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler.sgin;

import com.aizuda.common.toolkit.JacksonUtils;
import com.baomidou.kisso.common.util.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验签抽类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-01
 */
public abstract class AbstractParamsSignHandler implements IParamsSignHandler {

    protected boolean doBefore(HttpServletRequest request) {
        String sign = this.getSign(request);
        if (StringUtils.isEmpty(sign)) return false;
        String timestamp = this.getTimestamp(request);
        if (StringUtils.isEmpty(timestamp)) return false;
        return true;
    }

    protected String getRequestJsonStr(HttpServletRequest request) {
        Map<String, String> parameterMap = new HashMap<>(8);
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        parameterMap.put("timestamp", this.getTimestamp(request));
        return JacksonUtils.toJSONString(parameterMap);
    }

    protected String postRequestJsonStr(HttpServletRequest request) throws IOException {
        byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        String jsonStr = new String(bodyBytes, request.getCharacterEncoding());
        Map parameterMap = JacksonUtils.parse(jsonStr, Map.class);
        parameterMap.put("timestamp", this.getTimestamp(request));
        return JacksonUtils.toJSONString(parameterMap);
    }

    protected String getTimestamp(HttpServletRequest request) {
        return this.getSignParam(request, "timestamp");
    }

    protected String getSign(HttpServletRequest request) {
        return this.getSignParam(request, "sign");
    }

    protected String getSignParam(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StringUtils.isEmpty(value)) {
            value = request.getParameter(name);
        }
        return value;
    }

}
