/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss;

import com.aizuda.common.toolkit.DateUtils;
import com.aizuda.common.toolkit.IoUtils;
import com.aizuda.common.toolkit.ObjectUtils;
import com.aizuda.common.toolkit.ZipUtils;
import com.aizuda.oss.model.OssResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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
     * 指定文件桶名称，为空使用默认桶
     *
     * @param bucketName 桶名称
     * @return
     */
    IFileStorage bucket(String bucketName);

    /**
     * 指定存储对象名称
     *
     * @param objectName 存储对象名称
     * @return
     */
    IFileStorage objectName(String objectName);

    /**
     * 允许媒体类型判断，该方法使用配置 allowMediaType 媒体类型
     *
     * @param is 文件夹流 {@link InputStream}
     * @return
     */
    default IFileStorage allowMediaType(InputStream is) throws Exception {
        return this.allowMediaType(is, null);
    }

    /**
     * 允许媒体类型判断
     *
     * @param is       文件夹流 {@link InputStream}
     * @param function 文件类型合法判断函数
     * @return
     */
    IFileStorage allowMediaType(InputStream is, Function<String, Boolean> function) throws Exception;

    /**
     * 上传
     *
     * @param file {@link MultipartFile}
     * @return {@link OssResult}
     */
    default OssResult upload(MultipartFile file) throws Exception {
        return this.upload(file.getInputStream(), file.getOriginalFilename());
    }


    /**
     * 上传
     *
     * @param filename 文件名
     * @param is       文件流 {@link InputStream}
     * @return {@link OssResult}
     */
    OssResult upload(InputStream is, String filename) throws Exception;

    /**
     * 文件后缀，从文件名中获取后缀
     *
     * @return 文件后缀
     */
    default String getFileSuffix(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 下载
     *
     * @param objectName 文件对象名
     * @return {@link InputStream}
     */
    InputStream download(String objectName) throws Exception;

    /**
     * 下载
     *
     * @param response   {@link HttpServletResponse}
     * @param objectName 文件对象名
     */
    default void download(HttpServletResponse response, String objectName) throws Exception {
        InputStream is = this.download(objectName);
        if (null != is) {
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(objectName, "UTF-8"));
            IoUtils.write(is, response.getOutputStream());
        }
    }

    /**
     * 下载、多个打包 zip 下载
     *
     * @param response       {@link HttpServletResponse}
     * @param objectNameList 文件对象名列表
     */
    default void download(HttpServletResponse response, List<String> objectNameList) throws Exception {
        if (ObjectUtils.isNotEmpty(objectNameList)) {
            if (objectNameList.size() == 1) {
                // 单个文件下载
                this.download(response, objectNameList.get(0));
            } else {
                List<ZipUtils.FileEntry> fileEntries = new ArrayList<>();
                for (String objectName : objectNameList) {
                    InputStream is = this.download(objectName);
                    if (null == is) {
                        throw new FileNotFoundException(objectName);
                    }
                    fileEntries.add(ZipUtils.FileEntry.builder().inputStream(is)
                            .zipEntry(new ZipEntry(objectName)).build());
                }
                // 批量下载
                ZipUtils.zipFile(response.getOutputStream(), fileEntries);
            }
        }
    }

    /**
     * 删除文件
     *
     * @param objectNameList 文件对象名列表
     * @throws Exception
     */
    boolean delete(List<String> objectNameList) throws Exception;

    /**
     * 删除文件
     *
     * @param objectName 文件对象名
     * @throws Exception
     */
    boolean delete(String objectName) throws Exception;

    /**
     * 获取文件地址
     *
     * @param objectName 文件对象名
     * @param duration   期间
     * @param unit       时间单位 {@link TimeUnit}
     * @return
     */
    String getUrl(String objectName, int duration, TimeUnit unit) throws Exception;

    /**
     * 获取文件地址，默认 3 小时有效期
     *
     * @param objectName 文件对象名
     * @return
     */
    default String getUrl(String objectName) throws Exception {
        return this.getUrl(objectName, 3, TimeUnit.HOURS);
    }
}
