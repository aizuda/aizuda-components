/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler.sgin;

/**
 * 验签接口类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-01
 */
import javax.servlet.http.HttpServletRequest;

public interface IParamsSignHandler extends ISignHandler {

    boolean signGetRequest(HttpServletRequest request) throws Exception;

    boolean signPostRequest(HttpServletRequest request) throws Exception;

}
