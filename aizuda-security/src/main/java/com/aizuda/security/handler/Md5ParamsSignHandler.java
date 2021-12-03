/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.security.autoconfigure.SecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 请求参数验签处理器 MD5验签实现
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou hubin
 * @since 2021-12-01
 */
@AllArgsConstructor
public class Md5ParamsSignHandler extends AbstractParamsSignHandler {

    private SecurityProperties securityProperties;

    @Override
    public String sign(SortedMap<String, String> parameterMap) {
        return DigestUtils.md5DigestAsHex(JacksonUtils.toJSONString(parameterMap).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public SortedMap<String, String> parse(String jsonStr) {
        return JacksonUtils.parse(jsonStr, TreeMap.class);
    }

    @Override
    public String getSignParam() {
        return securityProperties.getSign().getSign();
    }

    @Override
    public String getTimestampParam() {
        return securityProperties.getSign().getTimestamp();
    }

    @Override
    public Long getFailureTime() {
        return DurationStyle.detectAndParse(securityProperties.getSign().getInvalidTime()).getSeconds();
    }
}
