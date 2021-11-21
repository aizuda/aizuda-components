package com.aizuda.exception.toolkit;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 11:59
 * @description 异常工具类
 */
public class ExceptionUtil {

    /**
     * 异常转字符串形式
     *
     * @param e e
     * @return {@link String}
     */
    public static String exceptionToString(Exception e){
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
