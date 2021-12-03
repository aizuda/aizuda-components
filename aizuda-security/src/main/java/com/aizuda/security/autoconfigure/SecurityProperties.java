/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.security.autoconfigure;

import com.baomidou.kisso.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 爱组搭安全配置
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-08
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties implements Serializable {
    /**
     * 配置前缀
     */
    public static final String PREFIX = "aizuda.security";
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 开启验签
     */
    private ParamsSign paramsSign;

    /**
     * 配置有效性检查
     */
    public void checkValid() {
        if (StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(publicKey)) {
            throw new RuntimeException("not found rsa privateKey or publicKey.");
        }
    }

    @Getter
    @Setter
    public static class ParamsSign {

        /**
         * 签名
         */
        private String sign = "sign";

        /**
         * 时间戳
         */
        private String timestamp = "timestamp";

        /**
         * 时间戳有效时间 不配置默认 30秒
         * <p>
         * 例如 5s 五秒，6m 六分钟，7h 七小时，8d 八天
         */
        private String invalidTime = "30s";

    }
}
