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

import java.util.Optional;

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
public class DingTalkSendMessage extends AbstractSendMessage {


    private RobotProperties robotProperties;


    @Override
    public boolean send(String message) {
        if (!support()) {
            return false;
        }
        //消息转换,转成钉钉需要的格式
        MessageDTO convert = MessageDTO.convert(message);
        WebHookSendResponse dingSendResponse = sendRequest(robot().getUrl(robotProperties.getDingToken()), convert);
        if (dingSendResponse == null || dingSendResponse.getErrcode() != 0) {
            final WebHookSendResponse sendResponse = Optional.ofNullable(dingSendResponse).orElse(new WebHookSendResponse());
            Integer errorCode = sendResponse.getErrcode();
            String errorMsg = sendResponse.getErrmsg();
            log.error("exception message send to dingTask ,errorCode: {},errorMsg:{}", errorCode, errorMsg);
            return false;
        }
        return true;
    }

    @Override
    public Robot robot() {
        return Robot.DING_TALK;
    }

    /**
     * 是否启用此方式
     *
     * @return boolean
     */
    @Override
    public boolean support() {
        return StringUtils.hasLength(robotProperties.getDingToken());
    }
}
