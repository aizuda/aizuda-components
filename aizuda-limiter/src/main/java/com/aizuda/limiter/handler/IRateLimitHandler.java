/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import com.aizuda.limiter.metadata.MethodMetadata;
import com.aizuda.limiter.metadata.RateLimitMethodMetaData;

/**
 * 速率限制处理器接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-16
 */
public interface IRateLimitHandler {

    /**
     * 继续执行
     *
     * @param methodMetadata {@link RateLimitMethodMetaData}
     * @return true 继续执行 false 限流不执行
     */
    boolean proceed(MethodMetadata methodMetadata);
}
