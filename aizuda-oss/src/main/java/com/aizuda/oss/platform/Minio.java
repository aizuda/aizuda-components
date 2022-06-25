/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss.platform;

import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.common.toolkit.SpringUtils;
import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.oss.IFileStorage;
import com.aizuda.oss.autoconfigure.OssProperty;
import com.aizuda.oss.model.OssResult;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Minio 存储
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-06-09
 */
@Slf4j
public class Minio implements IFileStorage {

    private MinioClient minioClient;

    /**
     * 配置属性
     */
    private OssProperty ossProperty;

    /**
     * 存储桶名称
     */
    private String bucketName;

    public Minio(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
        this.minioClient = MinioClient.builder().endpoint(ossProperty.getEndpoint())
                .credentials(ossProperty.getAccessKey(), ossProperty.getSecretKey())
                .build();
    }

    private String getBucketName() {
        return null == bucketName ? ossProperty.getBucketName() : bucketName;
    }

    @Override
    public IFileStorage bucket(String bucketName) {
        if (StringUtils.hasLength(bucketName)) {
            this.bucketName = bucketName;
        }
        return this;
    }

    @Override
    public OssResult upload(InputStream is, String filename) throws Exception {
        if (null == is || null == filename) {
            return null;
        }
        bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        String objectName = this.getObjectName(suffix);
        ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName).object(objectName)
                .stream(is, is.available(), -1).build());
        return null == response ? null : OssResult.builder().bucketName(bucketName)
                .objectName(objectName)
                .versionId(response.versionId())
                .filename(filename)
                .suffix(suffix)
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(this.getBucketName())
                .object(objectName).build());
    }

    @Override
    public boolean delete(List<String> objectNameList) throws Exception {
        if (ObjectUtils.isEmpty(objectNameList)) {
            return false;
        }
        minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(this.getBucketName())
                .objects(objectNameList.stream().map(k -> new DeleteObject(k)).collect(Collectors.toList()))
                .build());
        return true;
    }

    @Override
    public boolean delete(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(this.getBucketName())
                .object(objectName).build());
        return true;
    }

    @Override
    public String getUrl(String objectName, int duration, TimeUnit unit) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET).expiry(duration, unit)
                .bucket(this.getBucketName())
                .object(objectName).build());
    }
}
