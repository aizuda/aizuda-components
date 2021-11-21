/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.Robot;

/**
 * 发送消息机器人【钉钉】
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
public class DingTalkSendMessage implements ISendMessage {

    @Override
    public boolean send(String message) {
        return false;
    }

    @Override
    public Robot robot() {
        return Robot.DING_TALK;
    }
}
