/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.strategy;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * IP 速率限制策略
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
public class IpRateLimitStrategy implements IRateLimitStrategy {

    @Override
    public String getType() {
        return IP;
    }

    @Override
    public String getKey() {
        HttpServletRequest request = this.getRequest();
        final AtomicReference<String> ip = new AtomicReference<>(request.getRemoteAddr());
        // 对配置好的用于尝试的header进行尝试
        Stream.of(HEADERS_FOR_TRY).forEach((String header) -> {
            if (headerNotMatch(ip)) {
                ip.set(request.getHeader(header));
            }
        });
        // 最后对获取到的ip进行处理
        Optional.of(ip).map(AtomicReference::get).filter(header -> header.length() > LEGAL_HEADER_LENGTH).filter(header -> header.indexOf(COMMA) > ZERO)
                .map(header -> header.substring(ZERO, header.indexOf(COMMA))).ifPresent(ip::set);
        return ip.get();
    }

    /**
     * 判断header是否 不满足条件
     *
     * @param header header
     * @return 不满足条件，返回true
     */
    private boolean headerNotMatch(AtomicReference<String> header) {
        return !Optional.of(header).map(AtomicReference::get).filter(ip -> ip.length() != ZERO).filter(ip -> !UNKNOWN.equalsIgnoreCase(ip)).isPresent();
    }
}
