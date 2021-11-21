package com.aizuda.exception.toolkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 1:07
 * @description 时间工具列
 */
public class DateUtil {
    public static final String COMMON_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 返回此时时间 日期格式
     *
     * @return {@link String}
     */
    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(COMMON_DATETIME_PATTERN));
    }
}
