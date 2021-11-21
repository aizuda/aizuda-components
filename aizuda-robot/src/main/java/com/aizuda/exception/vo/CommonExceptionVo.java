package com.aizuda.exception.vo;

import com.aizuda.exception.toolkit.DateUtil;
import com.aizuda.exception.toolkit.ExceptionUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 1:04
 * @description believe in yourself
 */
@Data
@NoArgsConstructor
public class CommonExceptionVo {
    /**
     * 代码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 堆栈信息
     */
    private String stack;

    public CommonExceptionVo(Exception e) {
        CommonExceptionVo commonExceptionVo = new CommonExceptionVo();
        commonExceptionVo.setCode(500);
        commonExceptionVo.setMessage(e.getMessage());
        String exception = ExceptionUtil.exceptionToString(e);
        commonExceptionVo.setStack(exception);

    }

    @Override
    public String toString() {
        return "时间:" + DateUtil.now() + "\n"
                + "消息:" + message + "\n" +
                "堆栈信息:" + stack;
    }
}
