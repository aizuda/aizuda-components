/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.autoconfigure.RobotProperties;
import com.aizuda.robot.dto.MessageDTO;
import com.aizuda.robot.enums.Robot;
import com.aizuda.robot.vo.WechatSendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送消息机器人【企业微信】
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
@AllArgsConstructor
@Slf4j
public class QyWxSendMessage implements ISendMessage {

    private static final Integer wechatMaxLength = 4096;
    private RobotProperties robotProperties;

    /**
     * 测试字符串拼接
     *
     * @param args arg游戏
     */
    public static void main(String[] args) {
        final StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 4095 * 2 ; i++) {
            stringBuffer.append(1);
        }
        final List<MessageDTO> list = splitMessage(stringBuffer.toString());
        System.out.println(list);
        for (MessageDTO messageDTO : list) {
            System.out.println(messageDTO.getText().getContent().length());
        }
    }

    private static List<MessageDTO> splitMessage(String message) {
        int length = message.length();
        int start = 0;
        List<MessageDTO> list = new ArrayList<>();
        while (start < length) {
            if (length  - start > wechatMaxLength){
                String temp = message.substring(start,start +  wechatMaxLength);
                list.add(MessageDTO.convert(temp));
            }else{
                String temp = message.substring(start,length);
                list.add(MessageDTO.convert(temp));
            }

            start = start + wechatMaxLength;
        }
        return list;
    }

    @Override
    public boolean send(String message) {
        Robot robot = robot();
        String url = robot.getUrl() + robotProperties.getWechatKey();
        RestTemplate restTemplate = new RestTemplate();
        //消息转换,转成微信需要的格式
        //微信限制长度为4096
        final List<MessageDTO> list = splitMessage(message);
        for (MessageDTO messageDTO : list) {
            final ResponseEntity<WechatSendResponse> responseEntity = restTemplate.postForEntity(url, messageDTO, WechatSendResponse.class);
            final WechatSendResponse wechatSendResponse = responseEntity.getBody();
            if (wechatSendResponse == null) {
                return false;
            }
            if (wechatSendResponse.getErrcode() != 0) {
                log.error("发送消息到微信失败,失败代码: {},失败原因:{}", wechatSendResponse.getErrcode(), wechatSendResponse.getErrmsg());
                return false;
            }
        }

        return true;
    }

    @Override
    public Robot robot() {
        return Robot.QY_WX;
    }
}
