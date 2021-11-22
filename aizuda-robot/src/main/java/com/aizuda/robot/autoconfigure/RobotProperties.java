/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异常捕获切面处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author LoveHuahua hubin
 * @since 2021-11-21
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RobotProperties.PREFIX)
public class RobotProperties {
    public static final String PREFIX = "aizuda.robot";

    /**
     * 企业微信 key
     */
    private String wechatKey;
    /**
     * 钉钉
     */
    private DingTalk dingTalk;

    @Getter
    @Setter
    public static class DingTalk {
        private String accessToken;
        private String secret;

    }
}
