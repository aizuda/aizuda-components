package com.aizuda.limiter.strategy;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.limiter.metadata.MethodMetadata;

/**
 * 默认key策略
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 风尘
 * @since 2021-12-19
 */
public class DefaultKeyGenerateStrategy implements IKeyGenerateStrategy {
    public final static String TYPE = "aizuda-default";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getKey(MethodMetadata methodMetadata, String parseKey) {
        String result = methodMetadata.getClassMethodName();
        if (StringUtils.hasLength(parseKey)) {
            result += ":" + parseKey;
        }
        return result;
    }
}
