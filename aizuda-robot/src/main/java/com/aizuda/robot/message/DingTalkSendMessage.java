/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.dto.MessageDTO;
import com.aizuda.robot.enums.Robot;
import com.aizuda.robot.autoconfigure.RobotProperties;
import com.aizuda.robot.vo.WechatSendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 发送消息机器人【钉钉】
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
@AllArgsConstructor
@Slf4j
public class DingTalkSendMessage implements ISendMessage {
    private RobotProperties robotProperties;
    @Override
    public boolean send(String message) {
        Robot robot = robot();
        String url = robot.getUrl() + robotProperties.getDingToken();
        RestTemplate restTemplate = new RestTemplate();
        //消息转换,转成钉钉需要的格式
        MessageDTO convert = MessageDTO.convert(message);
        final ResponseEntity<WechatSendResponse> responseEntity = restTemplate.postForEntity(url, convert, WechatSendResponse.class);
        final WechatSendResponse dingSendResponse = responseEntity.getBody();
        if (dingSendResponse == null) {
            return false;
        }
        if (dingSendResponse.getErrcode() != 0) {
            log.error("发送消息到钉钉失败,失败代码: {},失败原因:{}", dingSendResponse.getErrcode(), dingSendResponse.getErrmsg());
            return false;
        }
        return true;
    }

    @Override
    public Robot robot() {
        return Robot.DING_TALK;
    }
}
