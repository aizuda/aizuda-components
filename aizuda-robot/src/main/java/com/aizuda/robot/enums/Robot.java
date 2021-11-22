/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.enums;

import lombok.Getter;

/**
 * 自动发送消息机器人
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
@Getter
public enum Robot {
    /**
     * https://developers.dingtalk.com/document/robots/custom-robot-access
     */
    DING_TALK("钉钉", "https://oapi.dingtalk.com/robot/send?access_token="),
    /**
     * https://work.weixin.qq.com/api/doc/90000/90136/91770
     */
    QY_WX("企业微信", "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=");

    /**
     * 名称
     */
    private final String name;
    /**
     * 地址
     */
    private final String url;

    Robot(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl(String key) {
        return url + key;
    }
}
