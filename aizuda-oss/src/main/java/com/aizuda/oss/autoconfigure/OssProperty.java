/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss.autoconfigure;

import com.aizuda.oss.model.StoragePlatform;
import lombok.Getter;
import lombok.Setter;

/**
 * oss 存储配置属性
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-06-23
 */
@Getter
@Setter
public class OssProperty {

    /**
     * 存储平台
     */
    private StoragePlatform platform;

    /**
     * 域名
     */
    private String endpoint;

    /**
     * ACCESS_KEY
     */
    private String accessKey;

    /**
     * SECRET_KEY
     */
    private String secretKey;

    /**
     * 存储空间名
     */
    private String bucketName;

    /**
     * 连接超时时间，默认设置一分钟
     */
    private int connectionTimeout = 60000;

}
