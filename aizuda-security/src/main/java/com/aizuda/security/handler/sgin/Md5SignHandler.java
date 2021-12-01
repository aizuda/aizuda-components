/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.handler.sgin;

import org.springframework.util.DigestUtils;

/**
 * 加密基础类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-01
 */
public class Md5SignHandler implements ISignHandler {

    @Override
    public String sign(String str) {
        // 重排序md5
        return DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
    }

}
