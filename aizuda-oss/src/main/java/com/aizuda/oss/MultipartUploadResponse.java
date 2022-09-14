/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传返回对象
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-07-28
 */
@Setter
@Getter
@Builder
public class MultipartUploadResponse {
    /**
     * 存储桶名
     */
    private String bucketName;
    /**
     * 对象名
     */
    private String objectName;
    /**
     * 上传地址
     */
    private String uploadUrl;

}
