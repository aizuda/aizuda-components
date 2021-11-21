package com.aizuda.exception.handler;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 0:47
 * @description 异常自动处理类
 */
public interface IExceptionHandler {


    /**
     * 判断异常是否由框架处理
     *
     * @param e e
     * @return boolean
     */
    boolean canSupport(Exception e);


    /**
     * 执行自定义异常
     *
     * @param e e
     */
    void execute(Exception e);
}
