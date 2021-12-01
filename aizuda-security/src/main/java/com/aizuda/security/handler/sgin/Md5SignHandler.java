package com.aizuda.security.handler.sgin;

import org.springframework.util.DigestUtils;

/**
 * 加密基础类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author imantou
 * @since 2021-12-1
 */
public class Md5SignHandler implements ISignHandler {

    @Override
    public String sign(String str) {
        // 重排序md5
        return DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();
    }

}
