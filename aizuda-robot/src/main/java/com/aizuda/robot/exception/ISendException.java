/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.exception;

import org.aspectj.lang.JoinPoint;

/**
 * 异常发送接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-21
 */
public interface ISendException {

    /**
     * 发送异常内容
     *
     * @param joinPoint {@link JoinPoint}
     * @param e         {@link Exception}
     * @return true 发送成功  false 发送失败
     */
    boolean send(JoinPoint joinPoint, Exception e);
}
