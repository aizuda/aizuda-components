/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.SpringUtils;
import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.oss.autoconfigure.OssProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * aizuda 文件存储接口
 *
 * @author 青苗
 * @since 2022-06-09
 */
@Slf4j
public class OSS {
    /**
     * 存储平台
     */
    private String platform = OssProperties.DEFAULT_PLATFORM;

    public static OSS builder() {
        return new OSS();
    }

    public OSS platform(String platform) {
        if (StringUtils.hasLength(platform)) {
            this.platform = platform;
        } else {
            log.debug(" OSS platform is empty.");
        }
        return this;
    }

    /**
     * 根据平台选择文件存储实现实例
     *
     * @return 文件存储实现实例
     */
    private IFileStorage fileStorage() {
        return SpringUtils.getBean(platform, IFileStorage.class);
    }

}
