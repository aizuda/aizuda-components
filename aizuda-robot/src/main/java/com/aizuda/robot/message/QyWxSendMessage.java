/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.autoconfigure.RobotProperties;
import com.aizuda.robot.enums.Robot;
import com.aizuda.robot.model.dto.MessageDTO;
import com.aizuda.robot.model.vo.WebHookSendResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class QyWxSendMessage extends AbstractSendMessage {

    /**
     * 微信能一次发送的最大长度
     */
    private static final Integer WECHAT_MAX_LENGTH = 4096;

    /**
     * 配置
     */
    private RobotProperties robotProperties;


    /**
     * 测试字符串拼接
     *
     * @param args args
     */
    public static void main(String[] args) {
        final StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 4095 * 2; i++) {
            stringBuffer.append(1);
        }
        final List<MessageDTO> list = splitMessage(stringBuffer.toString());
        System.out.println(list);
        for (MessageDTO messageDTO : list) {
            System.out.println(messageDTO.getText().getContent().length());
        }
    }

    /**
     * 切分消息
     *
     * @param message 消息
     * @return {@link List}<{@link MessageDTO}>
     */
    private static List<MessageDTO> splitMessage(String message) {
        int length = message.length();
        int start = 0;
        List<MessageDTO> list = new ArrayList<>();
        while (start < length) {
            String temp;
            if (length - start > WECHAT_MAX_LENGTH) {
                temp = message.substring(start, start + WECHAT_MAX_LENGTH);
            } else {
                temp = message.substring(start, length);
            }
            //消息转换,转成微信需要的格式
            list.add(MessageDTO.convert(temp));

            start = start + WECHAT_MAX_LENGTH;
        }
        return list;
    }

    @Override
    public boolean send(String message) {
        if (!support()) {
            return false;
        }
        //微信限制长度为4096,需要切割
        List<MessageDTO> list = splitMessage(message);
        for (MessageDTO messageDTO : list) {
            WebHookSendResponse wechatSendResponse = sendRequest(robot().getUrl(robotProperties.getWechatKey()), messageDTO);
            if (wechatSendResponse == null || wechatSendResponse.getErrcode() != 0) {
                WebHookSendResponse webHookSendResponse = Optional.ofNullable(wechatSendResponse).orElse(new WebHookSendResponse());
                Integer errorCode = webHookSendResponse.getErrcode();
                String errorMessage = webHookSendResponse.getErrmsg();
                log.error("exception message send to wechat ,errorCode: {},errorMsg:{}", errorCode, errorMessage);
            }
        }
        return true;
    }

    @Override
    public Robot robot() {
        return Robot.QY_WX;
    }

    /**
     * 是否启用此方式
     *
     * @return boolean
     */
    @Override
    public boolean support() {
        return StringUtils.hasLength(robotProperties.getWechatKey());
    }
}
