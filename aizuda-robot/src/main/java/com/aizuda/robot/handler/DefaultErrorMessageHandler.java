/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.robot.handler;

import com.aizuda.common.toolkit.DateUtils;
import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.common.toolkit.RequestUtils;
import com.aizuda.common.toolkit.ThrowableUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 默认异常消息处理器
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2021-11-23
 */
public class DefaultErrorMessageHandler implements IErrorMessageHandler {
    /**
     * 换行
     */
    private final String LINE_BREAK = "\n";

    @Override
    public String message(JoinPoint joinPoint, Exception e) {
        StringBuffer error = new StringBuffer();
        error.append("Time: ").append(DateUtils.nowTime());
        HttpServletRequest request = RequestUtils.getRequest();
        error.append(LINE_BREAK).append("IP: ").append(RequestUtils.getIp(request));
        Signature signature = joinPoint.getSignature();
        error.append(LINE_BREAK).append("Method: ").append(signature.getDeclaringTypeName()).append(".").append(signature.getName());
        error.append(LINE_BREAK).append("Args: ").append(JacksonUtils.toJSONString(joinPoint.getArgs()));
        error.append(LINE_BREAK).append("Exception: ").append(ThrowableUtils.getStackTrace(e));
        return error.toString();
    }

    @Override
    public String message(Exception e) {
        StringBuffer error = new StringBuffer();
        error.append("Time: ").append(DateUtils.nowTime());
        error.append(LINE_BREAK).append("Exception: ").append(ThrowableUtils.getStackTrace(e));
        return error.toString();
    }
}
