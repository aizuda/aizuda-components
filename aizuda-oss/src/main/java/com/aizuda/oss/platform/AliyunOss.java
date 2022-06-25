/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss.platform;

import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.oss.IFileStorage;
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
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-06-09
 */
public class AliyunOss implements IFileStorage {

    private OSSClient ossClient;

    /**
     * 配置属性
     */
    private OssProperty ossProperty;

    /**
     * 默认桶
     */
    private String bucketName;

    public AliyunOss(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(ossProperty.getConnectionTimeout());
        ossClient = new OSSClient(ossProperty.getEndpoint(), new DefaultCredentialProvider(ossProperty.getAccessKey(),
                ossProperty.getSecretKey()), clientConfiguration);
    }

    @Override
    public IFileStorage bucket(String bucketName) {
        if (StringUtils.hasLength(bucketName)) {
            this.bucketName = bucketName;
        }
        return this;
    }

    private String getBucketName() {
        return null == bucketName ? ossProperty.getBucketName() : bucketName;
    }

    @Override
    public OssResult upload(InputStream is, String filename) throws Exception {
        bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        String objectName = this.getObjectName(suffix);
        PutObjectResult por = ossClient.putObject(bucketName, objectName, is);
        return null == por ? null : OssResult.builder().bucketName(bucketName)
                .objectName(objectName)
                .versionId(por.getVersionId())
                .filename(filename)
                .suffix(suffix)
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        OSSObject ossObject = ossClient.getObject(this.getBucketName(), objectName);
        return null == ossObject ? null : ossObject.getObjectContent();
    }

    @Override
    public boolean delete(List<String> objectNameList) throws Exception {
        if (ObjectUtils.isEmpty(objectNameList)) {
            return false;
        }
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(this.getBucketName());
        deleteObjectsRequest.setKeys(objectNameList);
        return null != ossClient.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public boolean delete(String objectName) throws Exception {
        return null != ossClient.deleteObject(this.getBucketName(), objectName);
    }

    @Override
    public String getUrl(String objectName, int duration, TimeUnit unit) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(unit.toSeconds(duration));
        Date expiration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        URL url = ossClient.generatePresignedUrl(this.getBucketName(), objectName, expiration, HttpMethod.GET);
        return null == url ? null : url.toString();
    }
}
