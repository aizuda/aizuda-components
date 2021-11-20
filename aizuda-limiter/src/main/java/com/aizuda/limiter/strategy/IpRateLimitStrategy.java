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

    public static final String UNKNOWN = "unknown";
    public static final String[] HEADERS_FOR_TRY = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
    public static final Integer LEGAL_HEADER_LENGTH = 15;

    @Override
    public String getType() {
        return "ip";
    }

    @Override
    public String getKey() {
        HttpServletRequest request = this.getRequest();
        final AtomicReference<String> ip = new AtomicReference<>();
        // 对配置好的用于尝试的header进行尝试
        Stream.of(HEADERS_FOR_TRY).forEach((String header) -> {
            if (headerNotMatch(ip)) {
                ip.set(request.getHeader(header));
            }
        });
        if (headerNotMatch(ip)) {
            ip.set(request.getRemoteAddr());
        }
        // 最后对获取到的ip进行处理
        Optional.of(ip).map(AtomicReference::get).filter(header -> header.length() > LEGAL_HEADER_LENGTH).filter(header -> header.indexOf(",") > 0)
                .map(header -> header.substring(0, header.indexOf(","))).ifPresent(ip::set);
        return ip.get();
    }

    /**
     * 判断header是否 不满足条件
     *
     * @param header header
     * @return 不满足条件，返回true
     */
    private boolean headerNotMatch(AtomicReference<String> header) {
        return !Optional.of(header).map(AtomicReference::get).filter(ip -> ip.length() != 0).filter(ip -> !UNKNOWN.equalsIgnoreCase(ip)).isPresent();
    }
}
