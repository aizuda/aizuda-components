/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.DateUtils;
import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.common.toolkit.ZipUtils;
import com.aizuda.oss.model.OssResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;

/**
 * aizuda 文件存储接口
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-22
 */
public interface IFileStorage {

    /**
     * 上传
     *
     * @param file {@link MultipartFile}
     * @return {@link OssResult}
     */
    default OssResult upload(MultipartFile file) throws Exception {
        return this.upload(null, file);
    }

    /**
     * 上传
     *
     * @param bucketName 存储桶名
     * @param file       {@link MultipartFile}
     * @return {@link OssResult}
     */
    default OssResult upload(String bucketName, MultipartFile file) throws Exception {
        return this.upload(bucketName, file.getInputStream(), file.getOriginalFilename());
    }

    /**
     * 上传
     *
     * @param filename 文件名
     * @param is       文件流 {@link InputStream}
     * @return {@link OssResult}
     */
    default OssResult upload(InputStream is, String filename) throws Exception {
        return this.upload(null, is, filename);
    }

    /**
     * 上传
     *
     * @param bucketName 存储桶名
     * @param filename   文件名
     * @param is         文件流 {@link InputStream}
     * @return {@link OssResult}
     */
    OssResult upload(String bucketName, InputStream is, String filename) throws Exception;

    /**
     * 生成日期文件路径，按年月目录存储
     *
     * @return 文件路径
     */
    default String getPath() {
        String uuid = UUID.randomUUID().toString();
        return DateUtils.nowTimeFormat("yyyyMM") + "/" + uuid;
    }

    /**
     * 下载
     *
     * @param bucketName 存储桶名
     * @param objectName 文件对象名
     * @return {@link InputStream}
     */
    InputStream download(String bucketName, String objectName) throws Exception;

    /**
     * 下载
     *
     * @param objectName 文件对象名
     * @return {@link InputStream}
     */
    default InputStream download(String objectName) throws Exception {
        return this.download(null, objectName);
    }

    /**
     * 下载、多个打包 zip 下载
     *
     * @param os             {@link OutputStream}
     * @param objectNameList 文件对象名列表
     */
    default void download(OutputStream os, List<String> objectNameList) throws Exception {
        this.download(os, null, objectNameList);
    }

    /**
     * 下载、多个打包 zip 下载
     *
     * @param os             {@link OutputStream}
     * @param bucketName     存储桶名
     * @param objectNameList 文件对象名列表
     */
    default void download(OutputStream os, String bucketName, List<String> objectNameList) throws Exception {
        if (ObjectUtils.isNotEmpty(objectNameList)) {
            if (objectNameList.size() == 1) {
                // 单个文件下载
                this.download(bucketName, objectNameList.get(0));
            } else {
                List<ZipUtils.FileEntry> fileEntries = new ArrayList<>();
                for (String objectName : objectNameList) {
                    InputStream is = this.download(bucketName, objectName);
                    if (null == is) {
                        throw new FileNotFoundException(objectName);
                    }
                    fileEntries.add(ZipUtils.FileEntry.builder().inputStream(is)
                            .zipEntry(new ZipEntry(objectName)).build());
                }
                // 批量下载
                ZipUtils.zipFile(os, fileEntries);
            }
        }
    }

    /**
     * 删除文件
     *
     * @param objectNameList 文件对象名列表
     * @throws Exception
     */
    default void delete(List<String> objectNameList) throws Exception {
        this.delete(null, objectNameList);
    }

    /**
     * 删除文件
     *
     * @param bucketName     存储桶名
     * @param objectNameList 文件对象名列表
     * @throws Exception
     */
    boolean delete(String bucketName, List<String> objectNameList) throws Exception;

    /**
     * 删除文件
     *
     * @param objectName 文件对象名
     * @throws Exception
     */
    default boolean delete(String objectName) throws Exception {
        return this.delete(null, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名
     * @param objectName 文件对象名
     * @throws Exception
     */
    boolean delete(String bucketName, String objectName) throws Exception;

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名
     * @param objectName 文件对象名
     * @param duration   期间
     * @param unit       时间单位 {@link TimeUnit}
     * @return
     */
    String getUrl(String bucketName, String objectName, int duration, TimeUnit unit) throws Exception;

    /**
     * 获取文件地址，默认 3 小时有效期
     *
     * @param bucketName 存储桶名
     * @param objectName 文件对象名
     * @return
     */
    default String getUrl(String bucketName, String objectName) throws Exception {
        return this.getUrl(bucketName, objectName, 3, TimeUnit.HOURS);
    }

    /**
     * 获取文件地址，默认 3 小时有效期
     *
     * @param objectName 文件对象名
     * @return
     */
    default String getUrl(String objectName) throws Exception {
        return this.getUrl(null, objectName);
    }
}
