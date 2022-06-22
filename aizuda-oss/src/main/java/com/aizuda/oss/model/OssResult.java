package com.aizuda.oss.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 存储返回结果对象
 *
 * @author hubin
 * @since 2022-06-09
 */
@Getter
@Setter
@Builder
public class OssResult implements Serializable {

    /**
     * 存储桶名
     */
    private String bucketName;

    /**
     * 对象名称
     */
    private String objectName;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 版本
     */
    private String versionId;

}
