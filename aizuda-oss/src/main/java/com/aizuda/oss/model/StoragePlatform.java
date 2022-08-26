/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.oss.model;

import com.aizuda.oss.platform.AWSS3;
import com.aizuda.oss.platform.AliyunOss;
import com.aizuda.oss.platform.Local;
import com.aizuda.oss.platform.Minio;
import com.aizuda.oss.platform.TencentCos;
import lombok.Getter;

/**
 * 文件存储平台
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-29
 */
@Getter
public enum StoragePlatform {
    minio(Minio.class),
    aliyun(AliyunOss.class),
    tencentCos(TencentCos.class),
    awss3(AWSS3.class),
    local(Local.class);

    private final Class strategyClass;

    StoragePlatform(Class strategyClass) {
        this.strategyClass = strategyClass;
    }

}
