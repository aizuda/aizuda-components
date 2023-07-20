/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.oss.platform;

import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.oss.AbstractFileStorage;
import com.aizuda.oss.MultipartUploadResponse;
import com.aizuda.oss.autoconfigure.OssProperty;
import com.aizuda.oss.model.OssResult;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * aliyun oss 存储
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2022-06-09
 */
public class AliyunOss extends AbstractFileStorage {

    private OSSClient ossClient;

    public AliyunOss(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(ossProperty.getConnectionTimeout());
        ossClient = new OSSClient(ossProperty.getEndpoint(), new DefaultCredentialProvider(ossProperty.getAccessKey(),
                ossProperty.getSecretKey()), clientConfiguration);
    }

    public OSSClient getOssClient() {
        return ossClient;
    }

    @Override
    public OssResult upload(InputStream is, String filename, String objectName) throws Exception {
        String bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        String _objectName = this.getObjectName(suffix, objectName);
        PutObjectResult por = ossClient.putObject(bucketName, _objectName, is);
        return null == por ? null : OssResult.builder().bucketName(bucketName)
                .objectName(_objectName)
                .versionId(por.getVersionId())
                .filename(filename)
                .suffix(suffix)
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        String bucketName = this.getBucketName();
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        return null == ossObject ? null : ossObject.getObjectContent();
    }

    @Override
    public boolean delete(List<String> objectNameList) throws Exception {
        if (ObjectUtils.isEmpty(objectNameList)) {
            return false;
        }
        String bucketName = this.getBucketName();
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setKeys(objectNameList);
        return null != ossClient.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public boolean delete(String objectName) throws Exception {
        String bucketName = this.getBucketName();
        return null != ossClient.deleteObject(bucketName, objectName);
    }

    @Override
    public String getUrl(String objectName, int duration, TimeUnit unit) throws Exception {
        String bucketName = this.getBucketName();
        Date expiration = this.getExpiration(unit.toSeconds(duration));
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration, HttpMethod.GET);
        return null == url ? null : url.toString();
    }

    protected Date getExpiration(long seconds) {
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(seconds);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public MultipartUploadResponse getUploadSignedUrl(String filename) {
        String bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        String objectName = this.getObjectName(suffix, null);
        Date expiration = this.getExpiration(TimeUnit.HOURS.toSeconds(12));
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration, HttpMethod.PUT);
        return null == url ? null : MultipartUploadResponse.builder().bucketName(bucketName).objectName(objectName)
                .uploadUrl(url.toString()).build();
    }
}
