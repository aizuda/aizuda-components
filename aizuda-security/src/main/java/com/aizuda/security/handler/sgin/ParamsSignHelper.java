/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler.sgin;

import com.baomidou.kisso.common.util.StringUtils;

/**
 * 验签辅助类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-01
 */
public class ParamsSignHelper{

    public static long invalidTime(String invalidTime) {
        long failureTime = 0L;
        if (StringUtils.isNotEmpty(invalidTime)) {
            try {
                if (invalidTime.endsWith("ms")) {
                    failureTime = Long.parseLong(invalidTime.substring(0, invalidTime.length() - 2).trim());
                } else if (invalidTime.endsWith("s")) {
                    failureTime = Long.parseLong(invalidTime.substring(0, invalidTime.length() - 1).trim()) * 1000L;
                } else if (invalidTime.substring(invalidTime.length() - 1).matches("[0-9]*")) {
                    failureTime = Long.parseLong(invalidTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("sign config error: unsupported time configuration");
            }
        }
        return failureTime;
    }

}
