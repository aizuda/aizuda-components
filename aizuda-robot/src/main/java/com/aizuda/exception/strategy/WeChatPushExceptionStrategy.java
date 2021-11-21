package com.aizuda.exception.strategy;

import com.aizuda.exception.autoconfigure.WechatPushProperties;
import com.aizuda.exception.dto.WechatMessageDto;
import com.aizuda.exception.vo.CommonExceptionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author LoveHuahua
 * @date 2021年11月21日 12:13
 * @description believe in yourself
 */
@AllArgsConstructor
@Slf4j
public class WeChatPushExceptionStrategy implements IPushExceptionStrategy {
    private WechatPushProperties wechatPushProperties;

    @Override
    public void push(CommonExceptionVo commonExceptionVo) {
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(wechatPushProperties.getUrl() + "?key=" + wechatPushProperties.getKey(), WechatMessageDto.convert(commonExceptionVo), String.class);
        log.debug(stringResponseEntity.getBody());
    }
}
