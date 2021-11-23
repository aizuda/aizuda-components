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

    /**
     * 飞书
     */
    private FeiShu feiShu;

    @Getter
    @Setter
    public static class DingTalk {
        private String accessToken;
        private String secret;

    }

    @Getter
    @Setter
    public static class FeiShu {

        /**
         * 飞书的webhook地址后半部分
         * 例如https://open.feishu.cn/open-apis/bot/v2/hook/5439ea7b-13ec-4522-a819-xxxxx
         * 5439ea7b-13ec-4522-a819-xxxxx为key
         */
        private String key;

        /**
         * 飞书的密钥
         * 安全设置中设置了加密的话这里是必填,不然会出错
         */
        private String secret;
    }
}
