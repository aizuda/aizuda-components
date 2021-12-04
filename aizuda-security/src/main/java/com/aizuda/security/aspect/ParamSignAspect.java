/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.aspect;

import com.aizuda.security.exception.ParamsSignException;
import com.aizuda.security.handler.IParamsSignHandler;
import com.aizuda.security.request.SignRequestWrapper;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 请求参数验签切面
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou hubin
 * @since 2021-12-01
 */
@Aspect
@AllArgsConstructor
public class ParamSignAspect {

    private IParamsSignHandler paramsSignHandler;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void paramSignCut() {
    }

    @Around("paramSignCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SignRequestWrapper requestWrapper = (SignRequestWrapper) request;
        // 请求参数验签
        boolean isOk = false;
        if (Objects.equals(HttpMethod.GET.name(), requestWrapper.getMethod())) {
            isOk = this.checkSign(paramsSignHandler.doGet(requestWrapper));
        } else if (!requestWrapper.getContentType().toLowerCase().startsWith("multipart")) {
            // 请求body内容处理
            isOk = this.checkSign(paramsSignHandler.doPost(requestWrapper));
        }
        return isOk ? pjp.proceed() : null;
    }

    private boolean checkSign(boolean result) {
        if (!result) {
            throw new ParamsSignException("Request parameter signature verification failed");
        }
        return true;
    }
}