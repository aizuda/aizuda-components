package com.aizuda.exception.handler;

import com.aizuda.exception.strategy.IPushExceptionStrategy;
import com.aizuda.exception.vo.CommonExceptionVo;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 12:06
 * @description believe in yourself
 */
public class WechatExceptionHandler extends AbstractExceptionHandler {

    public WechatExceptionHandler(IPushExceptionStrategy pushExecute) {
        super(pushExecute);
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
        final CommonExceptionVo commonExceptionVo = new CommonExceptionVo(e);
        pushExecute.push(commonExceptionVo);
    }
}
