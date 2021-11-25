/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.autoconfigure.RobotProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;

/**
 * 发送消息机器人【企业微信】
 * <p>
 * 文档 https://work.weixin.qq.com/api/doc/90000/90136/91770
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
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
                put("content", subBytes(message, Math.min(message.length(), 2048)));
            }});
        }});
    }

    // TODO: 2021/11/25  挪到工具类
    private String subBytes(String str, int subLength) {
        int tempSubLength = subLength;
        String subStr = str.substring(0, subLength);
        int subStrBytesLength = subStr.getBytes(StandardCharsets.UTF_8).length;
        // 如果截取的字符串中包含有汉字
        while (subStrBytesLength > tempSubLength) {
            int subSLengthTemp = --subLength;
            subStr = str.substring(0, Math.min(subSLengthTemp, str.length()));
            subStrBytesLength = subStr.getBytes(StandardCharsets.UTF_8).length;
        }
        return subStr;
    }

    @Override
    public String getUrl() throws Exception {
        StringBuffer url = new StringBuffer();
        url.append("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=");
        url.append(robotProperties.getWeChat().getKey());
        return url.toString();
    }
}
