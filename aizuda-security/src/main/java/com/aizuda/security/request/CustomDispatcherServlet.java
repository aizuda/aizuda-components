package com.aizuda.security.request;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义 DispatcherServlet 来分派 CustomDispatcherServlet
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-11-26
 */
public class CustomDispatcherServlet extends DispatcherServlet {

    /**
     * 包装成我们自定义的request
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(new CustomHttpServletRequestWrapper(request), response);
    }
}