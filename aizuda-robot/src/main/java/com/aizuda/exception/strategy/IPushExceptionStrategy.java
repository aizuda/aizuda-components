package com.aizuda.exception.strategy;

import com.aizuda.exception.vo.CommonExceptionVo;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 0:55
 * @description believe in yourself
 */
public interface IPushExceptionStrategy {

    void push(CommonExceptionVo commonExceptionVo);
}
