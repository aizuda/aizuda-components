package com.aizuda.exception.dto;

import com.aizuda.exception.vo.CommonExceptionVo;
import lombok.Data;

import java.util.Optional;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 12:27
 * @description 通用异常到微信消息接口
 */
@Data
public class WechatMessageDto {
    /*
    * {
        "msgtype": "text",
        "text": {
            "content": "你好，我是程序员内点事"
        }
   }
   */
    private String msgtype = "text";

    private Text text = new Text();

    @Data
    private static class Text {
        private String content;

    }

    public static WechatMessageDto convert(CommonExceptionVo commonExceptionVo){
        final WechatMessageDto wechatMessageDto = new WechatMessageDto();
        final Text text = new Text();
        final CommonExceptionVo exceptionVo = Optional.ofNullable(commonExceptionVo).orElse(new CommonExceptionVo());
        text.setContent(exceptionVo.toString());
        wechatMessageDto.setText(text);
        return wechatMessageDto;
    }
}
