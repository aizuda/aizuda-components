/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss.platform;

import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.oss.AbstractFileStorage;
import com.aizuda.oss.MultipartUploadResponse;
import com.aizuda.oss.autoconfigure.OssProperty;
import com.aizuda.oss.model.OssResult;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Minio extends AbstractFileStorage {

    private MinioClient minioClient;

    public Minio(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
        this.minioClient = MinioClient.builder().endpoint(ossProperty.getEndpoint())
                .credentials(ossProperty.getAccessKey(), ossProperty.getSecretKey())
                .build();
    }

    public MinioClient getMinioClient() {
        return minioClient;
    }

    @Override
    public OssResult upload(InputStream is, String filename, String objectName) throws Exception {
        if (null == is || null == filename) {
            return null;
        }
        String bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        String _objectName = this.getObjectName(suffix, objectName);
        ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName).object(_objectName)
                .stream(is, is.available(), -1).build());
        return null == response ? null : OssResult.builder().bucketName(bucketName)
                .objectName(_objectName)
                .versionId(response.versionId())
                .filename(filename)
                .suffix(suffix)
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        String bucketName = this.getBucketName();
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName).object(objectName)
                .build());
    }

    @Override
    public boolean delete(List<String> objectNameList) throws Exception {
        if (ObjectUtils.isEmpty(objectNameList)) {
            return false;
        }
        String bucketName = this.getBucketName();
        minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName)
                .objects(objectNameList.stream().map(k -> new DeleteObject(k)).collect(Collectors.toList()))
                .build());
        return true;
    }

    @Override
    public boolean delete(String objectName) throws Exception {
        String bucketName = this.getBucketName();
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
                .object(objectName).build());
        return true;
    }

    @Override
    public String getUrl(String objectName, int duration, TimeUnit unit) throws Exception {
        String bucketName = this.getBucketName();
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET).expiry(duration, unit)
                .bucket(bucketName)
                .extraQueryParams(this.queryParams(objectName))
                .object(objectName).build());
    }

    protected Map<String, String> queryParams(String filename) {
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("response-content-type", this.getContentType(filename));
        return reqParams;
    }

    @Override
    public MultipartUploadResponse getUploadSignedUrl(String filename) {
        try {
            String bucketName = this.getBucketName();
            String suffix = this.getFileSuffix(filename);
            String objectName = this.getObjectName(suffix, null);
            return MultipartUploadResponse.builder().bucketName(bucketName).objectName(objectName)
                    .uploadUrl(minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(12, TimeUnit.HOURS)
                            .extraQueryParams(this.queryParams(filename))
                            .build())
                    ).build();
        } catch (Exception e) {
            log.error("minio getPresignedObjectUrl error.", e);
            return null;
        }
    }
}
