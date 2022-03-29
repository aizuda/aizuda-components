/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.exception;

import com.aizuda.robot.handler.IErrorMessageHandler;
import com.aizuda.robot.message.ISendMessage;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.util.List;

/**
 * 机器人发送异常消息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-21
 */
@AllArgsConstructor
public class RobotSendException implements ISendException {
    /**
     * 允许多端发送
     */
    private List<ISendMessage> sendMessageList;
    private IErrorMessageHandler errorMessageHandler;

    @Override
    public boolean send(JoinPoint joinPoint, Exception e) {
        try {
            String message = errorMessageHandler.message(joinPoint, e);
            for (ISendMessage sendMessage : sendMessageList) {
                sendMessage.send(message);
            }
            return true;
        } catch (Throwable t) {
            /**
             * 捕获可能异常，切面记录日志
             */
            return false;
        }
    }

    @Override
    public boolean send(Exception e) {
        try {
            String message = errorMessageHandler.message(e);
            for (ISendMessage sendMessage : sendMessageList) {
                sendMessage.send(message);
            }
            return true;
        } catch (Throwable t) {
            /**
             * 捕获可能异常，切面记录日志
             */
            return false;
        }
    }

    @Override
    public boolean send(String msg) {
        try {
            for (ISendMessage sendMessage : sendMessageList) {
                sendMessage.send(msg);
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
