/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.robot.autoconfigure.RobotProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;

/**
 * 发送消息机器人【企业微信】
 * <p>
 * 文档 https://work.weixin.qq.com/api/doc/90000/90136/91770
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-21
 */
@AllArgsConstructor
@Slf4j
public class WeChatSendMessage extends AbstractRobotSendMessage {
    private RobotProperties robotProperties;
    private RestTemplate restTemplate;

    @Override
    public boolean send(String message) throws Exception {
        return this.request(restTemplate, new HashMap<String, Object>(2) {{
            put("msgtype", "text");
            put("text", new HashMap<String, Object>(2) {{
                put("mentioned_list", Collections.singleton("@all"));
                // 企业微信文本内容，最长不超过2048个字节，必须是utf8编码
                put("content", StringUtils.substringByBytes(message, 2048));
            }});
        }});
    }

    @Override
    public String getUrl() throws Exception {
        StringBuffer url = new StringBuffer();
        url.append("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=");
        url.append(robotProperties.getWeChat().getKey());
        return url.toString();
    }
}
