/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.exception;

import org.aspectj.lang.JoinPoint;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常发送接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
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

    /**
     * 获取异常堆栈跟踪详情
     *
     * @param e 异常信息
     * @return 异常堆栈跟踪详情
     */
    default String getStackTrace(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String str = sw.toString();
            sw.close();
            pw.close();
            return str;
        } catch (Exception ex) {
            return e.getMessage();
        }
    }
}
