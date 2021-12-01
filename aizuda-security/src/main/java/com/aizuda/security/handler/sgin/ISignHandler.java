package com.aizuda.security.handler.sgin;

/**
 * 加密基础类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-1
 */
public interface ISignHandler {

    default String sign(String jsonStr) throws Exception {
        return null;
    }

}
