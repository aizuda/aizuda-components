/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.common.toolkit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * Throwable 工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @author L.cm
 * @since 2021-11-23
 */
public class ThrowableUtils {

    /**
     * 将CheckedException转换为UncheckedException.
     *
     * @param e Throwable
     * @return {RuntimeException}
     */
    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof Error) {
            throw (Error) e;
        } else if (e instanceof IllegalAccessException ||
                e instanceof IllegalArgumentException ||
                e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return runtime(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        return runtime(e);
    }

    /**
     * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
     *
     * @param throwable Throwable
     * @param <T>       泛型标记
     * @return Throwable
     * @throws T 泛型
     */
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T runtime(Throwable throwable) throws T {
        throw (T) throwable;
    }

    /**
     * 获取异常堆栈跟踪详情
     *
     * @param throwable 异常对象
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
