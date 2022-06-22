/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss.platform;

import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.common.toolkit.SpringUtils;
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
 *
 * @author 青苗
 * @since 2022-06-09
 */
public class AliyunOss implements IFileStorage {

    private OSSClient ossClient;

    /**
     * 默认桶
     */
    private String bucketName;

    public static AliyunOss getInstance() {
        return SpringUtils.getBean(AliyunOss.class);
    }

    public AliyunOss(OssProperty ossProperty) {
        this.bucketName = ossProperty.getBucketName();
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(ossProperty.getConnectionTimeout());
        ossClient = new OSSClient(ossProperty.getEndpoint(), new DefaultCredentialProvider(ossProperty.getAccessKey(),
                ossProperty.getSecretKey()), clientConfiguration);
    }

    private String getBucketName(String bucketName) {
        return null == bucketName ? this.bucketName : bucketName;
    }

    @Override
    public OssResult upload(String bucketName, InputStream is, String filename) throws Exception {
        bucketName = this.getBucketName(bucketName);
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        String objectName = getPath() + suffix;
        PutObjectResult por = ossClient.putObject(bucketName, objectName, is);
        return null == por ? null : OssResult.builder().bucketName(bucketName)
                .objectName(objectName)
                .versionId(por.getVersionId())
                .filename(filename)
                .suffix(suffix)
                .build();
    }

    @Override
    public InputStream download(String bucketName, String objectName) throws Exception {
        OSSObject ossObject = ossClient.getObject(this.getBucketName(bucketName), objectName);
        return null == ossObject ? null : ossObject.getObjectContent();
    }

    @Override
    public boolean delete(String bucketName, List<String> objectNameList) throws Exception {
        if (ObjectUtils.isEmpty(objectNameList)) {
            return false;
        }
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(this.getBucketName(bucketName));
        deleteObjectsRequest.setKeys(objectNameList);
        return null != ossClient.deleteObjects(deleteObjectsRequest);
    }

    @Override
    public boolean delete(String bucketName, String objectName) throws Exception {
        return null != ossClient.deleteObject(this.getBucketName(bucketName), objectName);
    }

    @Override
    public String getUrl(String bucketName, String objectName, int duration, TimeUnit unit) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(unit.toSeconds(duration));
        Date expiration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        URL url = ossClient.generatePresignedUrl(this.getBucketName(bucketName), objectName, expiration, HttpMethod.GET);
        return null == url ? null : url.toString();
    }
}
