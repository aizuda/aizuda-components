/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.security.handler;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.security.exception.ParamsSignException;
import org.springframework.util.StreamUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * 验签抽类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author imantou 青苗
 * @since 2021-12-01
 */
public abstract class AbstractParamsSignHandler implements IParamsSignHandler {

    /**
     * 执行验签
     *
     * @param request   {@link HttpServletRequest}
     * @param paramFunc 待验签参数返回函数
     * @return
     */
    protected boolean doCheck(HttpServletRequest request, Supplier<SortedMap<String, String>> paramFunc) {
        String signParam = this.getSignParam();
        String signValue = this.getHeaderOrParameter(request, signParam);
        if (!StringUtils.hasLength(signValue)) {
            return false;
        }

        String timestampParam = this.getTimestampParam();
        String timestampValue = this.getHeaderOrParameter(request, timestampParam);
        if (!StringUtils.hasLength(timestampValue)) {
            return false;
        }

        // 转换为毫秒对比时间戳
        if (this.getFailureTime() < System.currentTimeMillis() - Long.parseLong(timestampValue)) {
            return false;
        }

        SortedMap<String, String> parameterMap = paramFunc.get();

        // 移除 时间戳、签名
        if (null != parameterMap.get(signParam)) {
            parameterMap.remove(signParam);
        }
        if (null != parameterMap.get(timestampParam)) {
            parameterMap.remove(timestampParam);
        }

        // 验证签名
        return this.checkSign(parameterMap, timestampValue, signValue);
    }

    /**
     * 请求头或者请求中查询指定参数
     *
     * @param request {@link HttpServletRequest}
     * @param name    参数名称
     * @return
     */
    protected String getHeaderOrParameter(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        return StringUtils.hasLength(value) ? value : request.getParameter(name);
    }

    @Override
    public boolean doGet(HttpServletRequest request) {
        // get check sign
        return this.doCheck(request, () -> {
            SortedMap<String, String> parameterMap = new TreeMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                parameterMap.put(name, request.getParameter(name));
            }
            return parameterMap;
        });
    }

    @Override
    public boolean doPost(HttpServletRequest request) {
        // post check sign
        return this.doCheck(request, () -> {
            try {
                byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
                return this.parse(new String(bodyBytes, request.getCharacterEncoding()));
            } catch (IOException e) {
                throw new ParamsSignException("request body copy error");
            }
        });
    }

    /**
     * 验证参数签名
     *
     * @param parameterMap {@link SortedMap}
     * @param timestamp    时间戳
     * @param sign         签名值
     * @return
     */
    public abstract boolean checkSign(SortedMap<String, String> parameterMap, String timestamp, String sign);

    /**
     * 解析 json 字符串转换为 SortedMap
     *
     * @param jsonStr JSON字符串
     * @return
     */
    public abstract SortedMap<String, String> parse(String jsonStr);

    /**
     * 签名
     *
     * @return
     */
    public abstract String getSignParam();

    /**
     * 时间戳
     *
     * @return
     */
    public abstract String getTimestampParam();

    /**
     * 失效时间
     *
     * @return
     */
    public abstract Long getFailureTime();
}
