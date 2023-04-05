/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.strategy;

import com.aizuda.common.toolkit.RequestUtils;
import com.aizuda.limiter.metadata.MethodMetadata;

import jakarta.servlet.http.HttpServletRequest;

/**
 * key生成策略接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-16
 */
public interface IKeyGenerateStrategy {

    /**
     * 策略类型
     *
     * @return 限流策略类型
     */
    String getType();

    /**
     * 唯一标示 key
     *
     * @param methodMetadata {@link MethodMetadata}
     * @param parseKey       解析spEL得到的Key
     * @return 包装的key
     */
    String getKey(MethodMetadata methodMetadata, String parseKey);

    /**
     * 当前请求
     *
     * @return 当前请求
     */
    default HttpServletRequest getRequest() {
        return RequestUtils.getRequest();
    }
}
