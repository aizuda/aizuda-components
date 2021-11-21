package com.aizuda.exception.advice;

import com.aizuda.exception.handler.IExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @author LoveHuahua
 * 全局异常捕获处理
 */
@ControllerAdvice
@AllArgsConstructor
public class ExceptionHandlerAdvice {

    private List<IExceptionHandler> handlers;

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e) throws Exception {
        for (IExceptionHandler handler : handlers) {
            if (handler.canSupport(e)) {
                handler.execute(e);
            }
        }
        throw e;
    }
}
