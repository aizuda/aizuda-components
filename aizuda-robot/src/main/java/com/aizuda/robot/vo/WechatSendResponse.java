package com.aizuda.robot.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 23:35
 * @description believe in yourself
 */
@NoArgsConstructor
@Data
public class WechatSendResponse {

    /**
     * 错误代码
     */
    @JsonProperty("errcode")
    private Integer errcode;
    /**
     * 错误消息
     */
    @JsonProperty("errmsg")
    private String errmsg;
}
