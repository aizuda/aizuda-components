/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.strategy;

import com.aizuda.common.toolkit.RequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 速率限制策略接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
public interface IRateLimitStrategy {

    /**
     * 策略类型
     *
     * @return 限流策略类型
     */
    String getType();

    /**
     * 唯一标示 key
     *
     * @return 限流key
     */
    String getKey();

    /**
     * 当前请求
     *
     * @return 当前请求
     */
    default HttpServletRequest getRequest() {
        return RequestUtils.getRequest();
    }
}
