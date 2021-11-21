package com.aizuda.robot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 23:16
 * @description believe in yourself
 */
@ConfigurationProperties(prefix = RobotProperties.PREFIX)
@Data
public class RobotProperties {
    public static final String PREFIX = "aizuda.robot";

    /**
     * 企业微信机器人key
     */
    private String wechatKey;

    /**
     * 钉钉token
     */
    private String dingToken;


}
