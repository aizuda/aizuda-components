/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * 机器人消息发送抽象类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author LoveHuahua hubin
 * @since 2021-11-21
 */
public abstract class AbstractRobotSendMessage implements ISendMessage {

    /**
     * 消息请求发送
     *
     * @param restTemplate {@link RestTemplate}
     * @param objectMap    请求map对象
     * @return
     */
    public boolean request(RestTemplate restTemplate, Map<String, Object> objectMap) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> response = restTemplate.postForEntity(this.getUrl(),
                new HttpEntity<>(objectMap, headers), Map.class).getBody();
        if(null != response && Objects.equals(response.get("errcode"), 0)) {
            return true;
        }
        /**
         * 发送异常消息
         */
        throw new RuntimeException((String) response.get("errmsg"));
    }

    /**
     * 请求发送地址
     *
     * @throws Exception
     */
    public abstract String getUrl() throws Exception;
}
