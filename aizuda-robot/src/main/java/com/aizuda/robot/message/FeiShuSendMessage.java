package com.aizuda.robot.message;

import com.aizuda.robot.autoconfigure.RobotProperties;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author LoveHuahua
 * @date 2021年11月23日 13:51
 * @description believe in yourself
 */
@AllArgsConstructor
public class FeiShuSendMessage extends AbstractRobotSendMessage {

    private RobotProperties robotProperties;


    private RestTemplate restTemplate;


    /**
     * 请求发送地址
     */
    @Override
    public String getUrl() throws Exception {
        RobotProperties.FeiShu feiShu = robotProperties.getFeiShu();

        StringBuilder url = new StringBuilder();
        url.append("https://open.feishu.cn/open-apis/bot/v2/hook/");
        final String key = feiShu.getKey();
        url.append(key);
        return url.toString();
    }

    /**
     * 发送消息
     *
     * @param message 消息内容
     * @return true 发送成功 false 发送失败
     * @throws Exception
     */
    @Override
    public boolean send(String message) throws Exception {
        return this.request(restTemplate, new HashMap<String, Object>(2) {{
            RobotProperties.FeiShu feiShu = robotProperties.getFeiShu();
            final String secret = feiShu.getSecret();
            if (StringUtils.hasLength(secret)) {
                final long currentTimeMillis = System.currentTimeMillis() /1000;
                String stringToSign = currentTimeMillis + "\n" + secret;
                //使用HmacSHA256算法计算签名
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(new byte[]{});
                put("timestamp",currentTimeMillis);
                put("sign",new String(Base64.encodeBase64(signData)));
            }

            put("msg_type", "text");
            put("content", new HashMap<String, Object>(2) {{
                put("text", message);
            }});
        }});
    }
}
