/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.oss.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * oss 存储配置属性
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2022-06-20
 */
@Setter
@Getter
@ConfigurationProperties(prefix = OssProperties.OSS_PREFIX)
public class OssProperties {

    public static final String OSS_PREFIX = "aizuda";

    protected static String DEFAULT_PLATFORM;

    public static String getDefaultPlatform() {
        return DEFAULT_PLATFORM;
    }

    /**
     * OSS 配置属性
     */
    private Map<String, OssProperty> oss = new LinkedHashMap<>();

}
