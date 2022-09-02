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
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 腾讯云对象存储（Cloud Object Storage，COS）
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author tugenhao
 * @since 2022-08-15 01:09
 */
public class TencentCos extends AbstractFileStorage {
    private COSClient cosClient;

    public TencentCos(OssProperty ossProperty) {
        this.ossProperty = ossProperty;
        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID和SECRETKEY请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        COSCredentials cred = new BasicCOSCredentials(ossProperty.getAccessKey(), ossProperty.getSecretKey());
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(ossProperty.getEndpoint());
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        clientConfig.setConnectionTimeout(ossProperty.getConnectionTimeout());
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }

    public COSClient getCosClient() {
        return cosClient;
    }

    @Override
    public OssResult upload(InputStream is, String filename, String objectName) throws Exception {

        String bucketName = this.getBucketName();
        String suffix = this.getFileSuffix(filename);
        String _objectName = this.getObjectName(suffix, objectName);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, _objectName, is, null);
        PutObjectResult por = cosClient.putObject(putObjectRequest);

        return null == por ? null : OssResult.builder().bucketName(bucketName)
                .objectName(_objectName)
                .versionId(por.getVersionId())
                .filename(filename)
                .suffix(suffix)
                .build();

    }

    @Override
    public InputStream download(String objectName) throws Exception {
        // 获取下载输入流
        GetObjectRequest getObjectRequest = new GetObjectRequest(this.getBucketName(), objectName);
        COSObject cosObject = cosClient.getObject(getObjectRequest);
        return null == cosObject ? null : cosObject.getObjectContent();
    }

    @Override
    public boolean delete(List<String> objectNameList) throws Exception {
        if (ObjectUtils.isEmpty(objectNameList)) {
            return false;
        }
        String bucketName = this.getBucketName();
        // 设置要删除的key列表, 最多一次删除1000个
        ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
        objectNameList.forEach(e -> keyList.add(new DeleteObjectsRequest.KeyVersion(e)));
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setKeys(keyList);
        // 批量删除文件
        cosClient.deleteObjects(deleteObjectsRequest);
        return true;
    }

    @Override
    public boolean delete(String objectName) throws Exception {
        cosClient.deleteObject(this.getBucketName(), objectName);
        return true;
    }

    @Override
    public String getUrl(String objectName, int duration, TimeUnit unit) {
        String bucketName = this.getBucketName();
        return cosClient.getObjectUrl(bucketName, objectName).toString();
    }


    @Override
    public MultipartUploadResponse getUploadSignedUrl(String filename) {
        return null;
    }
}
