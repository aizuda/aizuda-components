package com.aizuda.security.handler.sgin;
/**
 * 验签接口类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-1
 */

import javax.servlet.http.HttpServletRequest;

public interface IParamsSignHandler {

    boolean signGetRequest(HttpServletRequest request) throws Exception;

    boolean signPostRequest(HttpServletRequest request) throws Exception;

}
