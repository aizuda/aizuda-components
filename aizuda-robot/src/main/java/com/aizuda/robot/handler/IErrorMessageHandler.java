/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.handler;

import org.aspectj.lang.JoinPoint;

/**
 * 异常消息处理器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-21
 */
public interface IErrorMessageHandler {

    /**
     * 异常消息内容
     *
     * @param joinPoint {@link JoinPoint}
     * @param e         {@link Exception}
     * @return 异常消息内容
     */
    String message(JoinPoint joinPoint, Exception e);
}
