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

    private final String[] HEADERS_FOR_TRY = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP"};

    @Override
    public String getType() {
        return "ip";
    }

    @Override
    public String getKey() {
        HttpServletRequest request = this.getRequest();
        String ip = null;
        for (String header : HEADERS_FOR_TRY) {
            String _ip = request.getHeader(header);
            if (null != _ip && _ip.length() > 0 && !"unknown".equalsIgnoreCase(_ip)) {
                ip = _ip;
                break;
            }
        }
        if (null == ip) {
            ip = request.getRemoteAddr();
        }
        if (null == ip) {
            return "";
        }
        int index = ip.indexOf(",");
        return index > 0 ? ip.substring(0, index) : ip;
    }
}
