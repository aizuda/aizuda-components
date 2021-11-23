/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.common.toolkit.AlgorithmUtils;
import com.aizuda.robot.autoconfigure.RobotProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 发送消息机器人【钉钉】
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-21
 */
@Slf4j
@AllArgsConstructor
public class DingTalkSendMessage extends AbstractRobotSendMessage {
    private RobotProperties robotProperties;
    private RestTemplate restTemplate;

    @Override
    public boolean send(String message) throws Exception {
        return this.request(restTemplate, new HashMap<String, Object>(3) {{
            put("msgtype", "text");
            put("at", new HashMap<String, Object>(1) {{
                put("isAtAll", true);
            }});
            put("text", new HashMap<String, Object>(1) {{
                put("content", message);
            }});
        }});
    }

    @Override
    public String getUrl() throws Exception {
        RobotProperties.DingTalk dingTalk = robotProperties.getDingTalk();
        StringBuffer url = new StringBuffer();
        url.append("https://oapi.dingtalk.com/robot/send?access_token=");
        url.append(dingTalk.getAccessToken());
        String secret = dingTalk.getSecret();
        if (null != secret && !"".equals(secret)) {
            Long timestamp = System.currentTimeMillis();
            url.append("&timestamp=").append(timestamp);
            String sign = AlgorithmUtils.encodeBase64HmacSHA256(secret, timestamp + "\n" + secret);
            url.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
        }
        return url.toString();
    }
}
