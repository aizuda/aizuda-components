/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求参数验签处理器接口类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-12-08
 */
public interface IParamsSignHandler {

    /**
     * Get 请求验签
     *
     * @param request {@link HttpServletRequest}
     * @return
     */
    boolean doGet(HttpServletRequest request);

    /**
     * Post 请求验签
     *
     * @param request {@link HttpServletRequest}
     * @return
     */
    boolean doPost(HttpServletRequest request);
}
