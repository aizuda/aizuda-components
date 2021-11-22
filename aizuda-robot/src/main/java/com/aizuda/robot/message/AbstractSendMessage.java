package com.aizuda.robot.message;

import com.aizuda.robot.model.dto.MessageDTO;
import com.aizuda.robot.model.vo.WebHookSendResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author LoveHuahua
 * @date 2021年11月22日 13:30
 * @description believe in yourself
 */
public abstract class AbstractSendMessage implements ISendMessage {

    /**
     * 是否启用此方式
     *
     * @return boolean
     */
    @Override
    public boolean support() {
        return true;
    }

    /**
     * 发送webhook请求
     *
     * @param message 消息
     * @return boolean
     */
    protected WebHookSendResponse sendRequest(String url, MessageDTO message) {
        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<WebHookSendResponse> responseEntity = restTemplate.postForEntity(url, message, WebHookSendResponse.class);
        return responseEntity.getBody();
    }

}
