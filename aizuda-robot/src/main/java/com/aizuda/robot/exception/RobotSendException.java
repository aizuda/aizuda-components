/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.exception;

import com.aizuda.common.toolkit.DateUtils;
import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.common.toolkit.RequestUtils;
import com.aizuda.robot.message.ISendMessage;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 机器人发送异常消息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
@AllArgsConstructor
public class RobotSendException implements ISendException {
    /**
     * 允许多端发送
     */
    private List<ISendMessage> sendMessageList;
    /**
     * 换行
     */
    private final String LINE_BREAK = "\n";

    @Override
    public boolean send(JoinPoint joinPoint, Exception e) {
        try {
            StringBuffer error = new StringBuffer();
            error.append("Time: ").append(DateUtils.nowTime());
            HttpServletRequest request = RequestUtils.getRequest();
            error.append(LINE_BREAK).append("IP: ").append(RequestUtils.getIp(request));
            Signature signature = joinPoint.getSignature();
            error.append(LINE_BREAK).append("Method: ").append(signature.getDeclaringTypeName()).append(".").append(signature.getName());
            error.append(LINE_BREAK).append("Args: ").append(JacksonUtils.toJSONString(joinPoint.getArgs()));
            error.append(LINE_BREAK).append("Exception: ").append(this.getStackTrace(e));
            for (ISendMessage sendMessage : sendMessageList) {
                sendMessage.send(error.toString());
            }
            return true;
        } catch (Throwable t) {
            /**
             * 捕获可能异常，切面记录日志
             */
            return false;
        }
    }
}
