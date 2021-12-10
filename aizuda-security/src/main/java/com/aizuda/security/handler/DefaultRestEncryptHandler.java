/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler;

import com.aizuda.common.toolkit.JacksonUtils;

/**
 * 默认接口加密处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-08
 */
public class DefaultRestEncryptHandler extends AbstractRestEncryptHandler {

    @Override
    public String toJson(Object body) {
        return JacksonUtils.toJSONString(body);
    }
}
