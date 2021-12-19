/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.strategy;

import com.aizuda.common.toolkit.RequestUtils;
import com.aizuda.limiter.metadata.MethodMetadata;

/**
 * IP 速率限制策略
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-16
 */
public class IpKeyGenerateStrategy implements IKeyGenerateStrategy {
    public final static String TYPE = "ip";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getKey(MethodMetadata methodMetadata, String parseKey) {
        return RequestUtils.getIp(this.getRequest());
    }

}
