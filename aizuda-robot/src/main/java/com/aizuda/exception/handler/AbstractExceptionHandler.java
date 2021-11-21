package com.aizuda.exception.handler;

import com.aizuda.exception.strategy.IPushExceptionStrategy;
import com.aizuda.exception.vo.CommonExceptionVo;

import java.util.List;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 0:52
 * @description believe in yourself
 */
public abstract class AbstractExceptionHandler implements IExceptionHandler {

    protected IPushExceptionStrategy pushExecute;

    public AbstractExceptionHandler(IPushExceptionStrategy pushExecute) {
        this.pushExecute = pushExecute;
    }

    /**
     * 判断此异常是否由框架处理
     *
     * @param e e
     * @return boolean
     */
    @Override
    public boolean canSupport(Exception e) {
        return true;
    }

    /**
     * 默认的异常逻辑 发送到各个机器人
     *
     * @param e 异常
     */
    @Override
    public void execute(Exception e) {

    }
}