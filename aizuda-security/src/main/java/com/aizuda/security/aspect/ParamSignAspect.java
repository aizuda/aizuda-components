package com.aizuda.security.aspect;

import com.aizuda.security.handler.sgin.IParamsSignHandler;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 验签切面
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-1
 */
@Aspect
@AllArgsConstructor
public class ParamSignAspect {

    private IParamsSignHandler paramsSignHandler;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void paramSignCut() {
    }

    @Around("paramSignCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 验签
        if ("GET".equals(request.getMethod())) {
            paramsSignHandler.signGetRequest(request);
        } else {
            //获取请求body
            paramsSignHandler.signPostRequest(request);
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}