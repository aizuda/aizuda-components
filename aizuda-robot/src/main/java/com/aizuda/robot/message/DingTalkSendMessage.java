/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.robot.message;

import com.aizuda.robot.autoconfigure.RobotProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            url.append("&sign=").append(sign);
        }
        return url.toString();
    }
}
