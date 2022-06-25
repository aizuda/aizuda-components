/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.oss.autoconfigure.OssProperty;

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
}
