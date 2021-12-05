package com.aizuda.common.toolkit;

import java.util.Collection;

/**
 * 集合操作工具类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-12-05
 */
public class CollectionUtils {

    public static boolean isNotEmpty(Collection<?> collection) {
        return null != collection && !collection.isEmpty();
    }

}
