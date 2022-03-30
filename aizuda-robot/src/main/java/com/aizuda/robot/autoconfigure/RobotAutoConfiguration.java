/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.autoconfigure;

import com.aizuda.robot.aspect.ExceptionAspect;
import com.aizuda.robot.exception.ISendException;
import com.aizuda.robot.exception.RobotSendException;
import com.aizuda.robot.handler.DefaultErrorMessageHandler;
import com.aizuda.robot.handler.IErrorMessageHandler;
import com.aizuda.robot.message.DingTalkSendMessage;
import com.aizuda.robot.message.FeiShuSendMessage;
import com.aizuda.robot.message.ISendMessage;
import com.aizuda.robot.message.WeChatSendMessage;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 速率限制配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-21
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({RobotProperties.class})
public class RobotAutoConfiguration {
    private RobotProperties robotProperties;

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public IErrorMessageHandler errorMessageHandler() {
        return new DefaultErrorMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public RobotSendException robotSendException(List<ISendMessage> sendMessageList, IErrorMessageHandler errorMessageHandler) {
        return new RobotSendException(sendMessageList, errorMessageHandler);
    }

    @Bean
    @ConditionalOnProperty(prefix = RobotProperties.PREFIX,name = "enableDefaultRobot",havingValue = "true",matchIfMissing = true)
    public ExceptionAspect exceptionAspect(ISendException sendException) {
        return new ExceptionAspect(sendException);
    }

    /**
     * 钉钉
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = RobotProperties.PREFIX, name = "dingTalk.accessToken")
    public DingTalkSendMessage dingTalkSendMessage(RestTemplate restTemplate) {
        return new DingTalkSendMessage(robotProperties, restTemplate);
    }

    /**
     * 企业微信
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = RobotProperties.PREFIX, name = "weChat.key")
    public WeChatSendMessage weChatSendMessage(RestTemplate restTemplate) {
        return new WeChatSendMessage(robotProperties, restTemplate);
    }

    /**
     * 飞书
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = RobotProperties.PREFIX, name = "feiShu.key")
    public FeiShuSendMessage feiShuSendMessage(RestTemplate restTemplate) {
        return new FeiShuSendMessage(robotProperties, restTemplate);
    }
}
