/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.aspect;

import com.aizuda.security.handler.sgin.IParamsSignHandler;
import com.aizuda.security.request.CustomHttpServletRequestWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 验签切面
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-01
 */
@Aspect
@AllArgsConstructor
@Slf4j
public class ParamSignAspect {

    private IParamsSignHandler paramsSignHandler;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void paramSignCut() {
    }

    @Around("paramSignCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        CustomHttpServletRequestWrapper requestWrapper = (CustomHttpServletRequestWrapper) request;
        // 验签
        if ("GET".equals(requestWrapper.getMethod())) {
            if (!paramsSignHandler.signGetRequest(requestWrapper)) {
                log.error("Illegal tampering with data");
                return null;
            }
        } else if (!requestWrapper.getContentType().toLowerCase().startsWith("multipart")) {
            //获取请求body
            if (!paramsSignHandler.signPostRequest(requestWrapper)) {
                log.error("Illegal tampering with data");
                return null;
            }
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}