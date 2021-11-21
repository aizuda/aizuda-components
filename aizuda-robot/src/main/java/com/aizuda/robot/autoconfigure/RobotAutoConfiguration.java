/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.autoconfigure;

import com.aizuda.robot.aspect.ExceptionAspect;
import com.aizuda.robot.exception.ISendException;
import com.aizuda.robot.exception.RobotSendException;
import com.aizuda.robot.message.ISendMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 速率限制配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
@Configuration
public class RobotAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RobotSendException robotSendException(List<ISendMessage> sendMessageList) {
        return new RobotSendException(sendMessageList);
    }

    @Bean
    public ExceptionAspect exceptionAspect(ISendException sendException) {
        return new ExceptionAspect(sendException);
    }
}
