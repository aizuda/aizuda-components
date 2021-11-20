/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.strategy;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 速率限制策略
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
public class IpRateLimitStrategy implements IRateLimitStrategy {

    public final String[] HEADERS_FOR_TRY = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP"};

    @Override
    public String getType() {
        return "ip";
    }

    @Override
    public String getKey() {
        HttpServletRequest request = this.getRequest();
        String ip = null;
        // 对配置好的用于尝试的header进行尝试
        for (String header : HEADERS_FOR_TRY) {
            if (headerNotMatch(ip)) {
                ip = request.getHeader(header);
            }
        }
        if (headerNotMatch(ip)) {
            ip = request.getRemoteAddr();
        }
        // 最后对获取到的ip进行处理
        if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    /**
     * 判断ip是否 不满足条件
     *
     * @param ip ip
     * @return 不满足条件，返回true
     */
    private boolean headerNotMatch(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
