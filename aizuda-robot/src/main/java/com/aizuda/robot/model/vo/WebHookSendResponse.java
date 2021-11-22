package com.aizuda.robot.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 23:35
 * @description believe in yourself
 */
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class WebHookSendResponse {

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
