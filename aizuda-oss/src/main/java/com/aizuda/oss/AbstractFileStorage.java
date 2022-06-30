/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.oss.autoconfigure.OssProperty;
import com.aizuda.oss.exception.MediaTypeException;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

/**
 * aizuda 抽象文件存储类
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-22
 */
public abstract class AbstractFileStorage implements IFileStorage {

    /**
     * 配置属性
     */
    protected OssProperty ossProperty;

    /**
     * 存储桶名称
     */
    protected String bucketName;

    /**
     * 存储桶名称
     */
    protected String getBucketName() {
        String _bucketName = ossProperty.getBucketName();
        if (null != this.bucketName) {
            _bucketName = this.bucketName;
            // 清空设置桶
            this.bucketName = null;
        }
        return _bucketName;
    }

    @Override
    public IFileStorage bucket(String bucketName) {
        if (StringUtils.hasLength(bucketName)) {
            this.bucketName = bucketName;
        }
        return this;
    }

    @Override
    public IFileStorage allowMediaType(InputStream is, Function<String, Boolean> function) throws Exception {
        boolean legal = false;
        String mediaType = MediaType.detect(is);
        if (null == function) {
            List<String> allowMediaType = this.ossProperty.getAllowMediaType();
            if (null != allowMediaType) {
                legal = allowMediaType.stream().anyMatch(t -> mediaType.startsWith(t));
            }
        } else {
            legal = function.apply(mediaType);
        }
        // 不合法媒体类型抛出异常
        if (!legal) {
            throw new MediaTypeException("Illegal file type");
        }
        return this;
    }
}
