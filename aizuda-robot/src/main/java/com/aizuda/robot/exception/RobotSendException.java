/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.exception;

import com.aizuda.common.toolkit.JacksonUtils;
import com.aizuda.robot.message.ISendMessage;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean send(JoinPoint joinPoint, Exception e) {
        try {
            StringBuffer error = new StringBuffer();
            Signature signature = joinPoint.getSignature();
            error.append("<br>Time: ").append(LocalDateTime.now().format(DATE_TIME_FORMATTER));
            error.append("<br>Method: ").append(signature.getDeclaringTypeName()).append(".").append(signature.getName());
            error.append("<br>Args: ").append(JacksonUtils.toJSONString(joinPoint.getArgs()));
            error.append("<br>Exception: ").append(this.getStackTrace(e));
            sendMessageList.forEach(t -> t.send(error.toString()));
            return true;
        } catch (Throwable t) {
            /**
             * 捕获可能异常，切面记录日志
             */
            return false;
        }
    }
}
