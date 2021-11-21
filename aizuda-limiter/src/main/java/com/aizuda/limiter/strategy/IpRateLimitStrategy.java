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

    private final String[] HEADERS_FOR_TRY = {
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"};

    @Override
    public String getType() {
        return "ip";
    }

    @Override
    public String getKey() {
        HttpServletRequest request = this.getRequest();
        String ip = null;
        for (String header : HEADERS_FOR_TRY) {
            String currentIp = request.getHeader(header);
            if (isUnknown(currentIp)) {
                ip = currentIp;
                break;
            }
        }
        if (null == ip) {
            ip = request.getRemoteAddr();
        }
        String localAddr = "0:0:0:0:0:0:0:1";
        if (localAddr.equals(ip)) {
            return "127.0.0.1";
        }
        if (null == ip) {
            return "";
        }
        return getMultistageReverseProxyIp(ip);
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    private String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        String delimiter = ",";
        if (ip != null && ip.indexOf(delimiter) > 0) {
            String[] ips = ip.trim().split(delimiter);
            for (String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    private boolean isUnknown(String checkIp) {
        return null != checkIp && checkIp.length() > 0 && !"unknown".equalsIgnoreCase(checkIp);
    }
}
