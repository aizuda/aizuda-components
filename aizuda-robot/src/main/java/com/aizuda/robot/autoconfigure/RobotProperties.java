/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.robot.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异常捕获切面处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author LoveHuahua 青苗
 * @since 2021-11-21
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RobotProperties.PREFIX)
public class RobotProperties {
    public static final String PREFIX = "aizuda.robot";

    /**
     * 企业微信
     */
    private WeChat weChat;

    /**
     * 钉钉
     */
    private DingTalk dingTalk;

    /**
     * 飞书
     */
    private FeiShu feiShu;

    @Getter
    @Setter
    public static class WeChat {
        private String key;

    }

    @Getter
    @Setter
    public static class DingTalk {
        private String accessToken;
        private String secret;

    }

    @Getter
    @Setter
    public static class FeiShu {
        private String key;
        private String secret;
    }
}
