/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.request;

import com.aizuda.security.exception.ParamsSignException;
import com.aizuda.security.handler.IParamsSignHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 用来重新包装request解决流只能读取一次问题
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-11-26
 */
@AllArgsConstructor
public class SignRequestFilter implements Filter {
    private IParamsSignHandler paramsSignHandler;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            SignRequestWrapper signRequest = new SignRequestWrapper(request);
            // 请求参数验签
            boolean isOk = false;
            if (Objects.equals(HttpMethod.GET.name(), signRequest.getMethod())) {
                isOk = this.checkSign(paramsSignHandler.doGet(signRequest));
            } else if (signRequest.getContentType().contains("application/json")) {
                // 请求body内容处理
                isOk = this.checkSign(paramsSignHandler.doPost(signRequest));
            }
            if (isOk) {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean checkSign(boolean result) {
        if (!result) {
            throw new ParamsSignException("Request parameter signature verification failed");
        }
        return true;
    }

    @Override
    public void destroy() {

    }
}