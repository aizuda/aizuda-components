package com.aizuda.robot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 23:26
 * @description 发送消息类型 枚举
 */
public class SendType {

    @AllArgsConstructor
    @Getter
    public enum WechatMessageType {

        /**
         * 微信的文本消息类型
         */
        TEXT("text","文本消息");

        /**
         * 类型代码
         */
        private String type;

        /**
         * 类型描述
         */
        private String typeDesc;

    }
}
