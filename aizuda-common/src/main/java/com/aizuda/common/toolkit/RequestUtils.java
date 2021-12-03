/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.common.toolkit;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Http 请求工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-08
 */
public class RequestUtils {

    public final static String LOCAL_IP = "127.0.0.1";

    public final static String LOCAL_ADDRESS = "0:0:0:0:0:0:0:1";

    /**
     * 当前 HttpServletRequest 请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private static final String[] HEADERS = {
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
            "X-Real-IP"
    };

    /**
     * 获取 request 请求 IP 地址
     *
     * @return IP 地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = null;
        for (String header : HEADERS) {
            String currentIp = request.getHeader(header);
            if (isNotUnknown(currentIp)) {
                ip = currentIp;
                break;
            }
        }
        if (null == ip) {
            ip = request.getRemoteAddr();
        }
        if (null == ip) {
            return "";
        }
        if (LOCAL_ADDRESS.equals(ip)) {
            return LOCAL_IP;
        }
        return getMultistageReverseProxyIp(ip);
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    private static String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        String delimiter = ",";
        if (ip != null && ip.indexOf(delimiter) > 0) {
            String[] ips = ip.trim().split(delimiter);
            for (String subIp : ips) {
                if (isNotUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    private static boolean isNotUnknown(String checkIp) {
        return StringUtils.hasLength(checkIp) && !"unknown".equalsIgnoreCase(checkIp);
    }
}
