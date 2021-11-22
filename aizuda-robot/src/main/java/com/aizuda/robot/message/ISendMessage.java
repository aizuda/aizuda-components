/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.enums.Robot;

/**
 * 发送消息接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
public interface ISendMessage {

    /**
     * 发送消息
     *
     * @param message 消息内容
     * @return true 发送成功 false 发送失败
     */
    boolean send(String message);

    /**
     * 机器人
     *
     * @return {@link Robot}
     */
    Robot robot();

    /**
     * 是否启用此方式
     *
     * @return boolean
     */
    boolean support();
}
