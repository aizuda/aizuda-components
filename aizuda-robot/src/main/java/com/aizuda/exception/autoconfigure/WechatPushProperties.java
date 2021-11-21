package com.aizuda.exception.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 1:14
 * @description believe in yourself
 */
@ConfigurationProperties(prefix = WechatPushProperties.PREFIX)
@Getter
@Setter
public class WechatPushProperties {
    public static final String PREFIX  = "aizuda.push.wechat";

    /**
     * 微信webhook的key
     */
    private String key;


    /**
     * webhook地址
     */
    private String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send";
}
