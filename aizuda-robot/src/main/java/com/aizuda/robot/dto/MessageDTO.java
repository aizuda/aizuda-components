package com.aizuda.robot.dto;

import com.aizuda.robot.enums.SendType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 12:27
 * @description 通用异常到微信消息接口
 */
@Data
public class MessageDTO {

    private String msgtype;

    private Text text;


    /**
     * 转换
     *
     * @param content 发送的正文
     * @return {@link MessageDTO} 返回微信能识别的格式
     */
    public static MessageDTO convert(String content) {
        MessageDTO wechatMessageDto = new MessageDTO();
        wechatMessageDto.setMsgtype(SendType.WechatMessageType.TEXT.getType());
        Text text = new Text(content);
        wechatMessageDto.setText(text);
        return wechatMessageDto;
    }

    @Data
    @AllArgsConstructor
    public static class Text {
        private String content;

    }
}
