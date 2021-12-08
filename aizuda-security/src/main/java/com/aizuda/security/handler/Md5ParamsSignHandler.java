/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.security.autoconfigure.SecurityProperties;
import com.baomidou.kisso.common.util.StringPool;
import lombok.AllArgsConstructor;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
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

    /**
     * 失效时间
     */
    private static Long FAILURE_TIME = null;

    @Override
    public boolean checkSign(SortedMap<String, String> parameterMap, String timestamp, String sign) {
        StringBuffer sb = new StringBuffer();
        // 加密参数内容
        String paramEncrypt = parameterMap.get(this.getParamEncryptKey());
        if (StringUtils.hasLength(paramEncrypt)) {
            sb.append(this.toMd5(paramEncrypt));
        } else {
            sb.append(this.toMd5(JacksonUtils.toJSONString(parameterMap)));
        }
        // 混淆时间戳
        sb.append(timestamp);
        return Objects.equals(this.toMd5(sb.toString()), sign);
    }

    private String toMd5(String string) {
        return DigestUtils.md5DigestAsHex(string.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public SortedMap<String, String> parse(String jsonStr) {
        SortedMap<String, String> parameterMap = null;
        if(null != jsonStr && jsonStr.contains(StringPool.LEFT_BRACE)) {
            try {
                parameterMap = JacksonUtils.parseThrows(jsonStr, TreeMap.class);
            } catch (Exception e) {
                // 加密导致异常逻辑
            }
        }

        // 处理加密逻辑
        if (null == parameterMap) {
            parameterMap = new TreeMap<>();
            parameterMap.put(this.getParamEncryptKey(), jsonStr);
        }
        return parameterMap;
    }

    private String getParamEncryptKey() {
        return "AZD_PARAMS_SIGN_";
    }

    @Override
    public String getSignParam() {
        return securityProperties.getParamsSign().getSign();
    }

    @Override
    public String getTimestampParam() {
        return securityProperties.getParamsSign().getTimestamp();
    }

    @Override
    public Long getFailureTime() {
        if (null == FAILURE_TIME) {
            FAILURE_TIME = DurationStyle.detectAndParse(securityProperties.getParamsSign()
                    .getInvalidTime()).getSeconds() * 1000;
        }
        return FAILURE_TIME;
    }
}
